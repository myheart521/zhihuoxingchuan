<template>
  <div class="practice-article-list">
    <!-- 列表主体 -->
    <div v-if="list.length" class="article-grid">
      <div
        v-for="item in list"
        :key="item.id"
        class="article-card flip"
        @click="toDetail(item.id)"
      >
        <!-- front -->
        <div
          class="front"
          :style="{ backgroundImage: `url(${item.frontCover})` }"
        >
          <!-- 置顶标识 -->
          <div v-if="item.isTop" class="top-badge">
            <svg class="top-icon" viewBox="0 0 24 24">
              <path d="M14,3V5H17.59L7.76,14.83L9.17,16.24L19,6.41V10H21V3M19,19H5V5H12V3H5C3.89,3 3,3.89 3,5V19A2,2 0 0,0 5,21H19A2,2 0 0,0 21,19V12H19V19Z"/>
            </svg>
            置顶
          </div>
          
          <!-- 分类标签 -->
          <div v-if="item.category" class="category-tag">
            {{ item.category.name }}
          </div>
          
          <h3 class="title text-shadow">{{ item.title }}</h3>
        </div>

        <!-- back -->
        <div class="back">
          <div class="back-header">
            <h3 class="back-title">{{ item.title }}</h3>
            <!-- 置顶标识（背面） -->
            <div v-if="item.isTop" class="top-badge-small">
              <svg class="top-icon-small" viewBox="0 0 24 24">
                <path d="M14,3V5H17.59L7.76,14.83L9.17,16.24L19,6.41V10H21V3M19,19H5V5H12V3H5C3.89,3 3,3.89 3,5V19A2,2 0 0,0 5,21H19A2,2 0 0,0 21,19V12H19V19Z"/>
              </svg>
            </div>
          </div>
          
          <p class="summary">{{ item.summary }}</p>
          
          <!-- 标签列表 -->
          <div v-if="item.tags && item.tags.length > 0" class="tags-section">
            <div class="tags-list">
              <span 
                v-for="tag in item.tags.slice(0, 3)" 
                :key="tag.id" 
                class="tag-item"
              >
                {{ tag.name }}
              </span>
              <span v-if="item.tags.length > 3" class="more-tags">
                +{{ item.tags.length - 3 }}
              </span>
            </div>
          </div>
          
          <!-- 分类信息 -->
          <div v-if="item.category" class="category-info">
            <span class="category-label">分类:</span>
            <span class="category-name">{{ item.category.name }}</span>
          </div>
          
          <div class="meta">
            <span>{{ item.viewCount }} 浏览</span>
            <span>{{ item.likeCount }} 点赞</span>
            <span>{{ dayjs(item.createTime).format('YYYY-MM-DD') }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 空数据占位 -->
    <div v-else class="empty">暂无相关文章</div>

    <!-- 自定义分页控件 -->
    <div v-if="total > pageSize" class="pagination-wrapper">
      <nav
        class="pagination-nav"
        aria-label="Pagination"
      >
        <button
          class="page-button"
          :disabled="pageNo === 1"
          @click="prevPage"
          title="Previous Page"
        >
          <svg class="icon" viewBox="0 0 256 512">
            <path
              d="M238.475 475.535l7.071-7.07c4.686-4.686 4.686-12.284 0-16.971L50.053 256 245.546 60.506c4.686-4.686 4.686-12.284 0-16.971l-7.071-7.07c-4.686-4.686-12.284-4.686-16.97 0L10.454 247.515c-4.686 4.686-4.686 12.284 0 16.971l211.051 211.05c4.686 4.686 12.284 4.686 16.97-.001z"
            />
          </svg>
        </button>

        <button
          v-for="n in pagesToShow"
          :key="n"
          class="page-number"
          :class="{ active: n === pageNo }"
          @click="gotoPage(n)"
          :disabled="n === pageNo"
        >
          {{ n }}
        </button>

        <button
          class="page-button"
          :disabled="pageNo === totalPages"
          @click="nextPage"
          title="Next Page"
        >
          <svg class="icon" viewBox="0 0 256 512">
            <path
              d="M17.525 36.465l-7.071 7.07c-4.686 4.686-4.686 12.284 0 16.971L205.947 256 10.454 451.494c-4.686 4.686-4.686 12.284 0 16.971l7.071 7.07c4.686 4.686 12.284 4.686 16.97 0l211.051-211.05c4.686-4.686 4.686-12.284 0-16.971L34.495 36.465c-4.686-4.687-12.284-4.687-16.97 0z"
            />
          </svg>
        </button>
      </nav>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue'
import { PracticeColumnApi } from '@/api/blog/practice-column'
import dayjs from 'dayjs'
import router from '@/router'

interface QueryParams {
  pageNo: number
  pageSize: number
  keyword?: string
  categoryId?: number
  tagId?: number
}

interface Tag {
  id: number
  name: string
}

interface Category {
  id: number
  name: string
}

interface Author {
  id: number
  username: string
  role: string | null
}

interface ArticleItem {
  id: number
  title: string
  summary: string
  frontCover: string
  viewCount: number
  createTime: string
  likeCount: number
  isTop: number
  category: Category | null
  tags: Tag[]
  author: Author
}

const props = defineProps<{ queryParams: QueryParams }>()

const list = ref<ArticleItem[]>([])
const total = ref(0)
const loading = ref(false)

// 当前页与每页条数取自 props.queryParams 里的 pageNo/pageSize
const pageNo = ref(props.queryParams.pageNo)
const pageSize = ref(props.queryParams.pageSize)

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...props.queryParams,
      pageNo: pageNo.value,
      pageSize: pageSize.value
    }
    // 调用接口
    const { list: dataList, total: totalCount } = await PracticeColumnApi.getPracticePage(params)
    list.value = dataList
    total.value = totalCount
  } catch (err) {
    console.error('获取文章列表失败', err)
  } finally {
    loading.value = false
  }
}

// 页面变化
const handlePageChange = (page: number) => {
  pageNo.value = page
  fetchData()
}

// 自定义分页逻辑
const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

const pagesToShow = computed(() => {
  const range: number[] = []
  const maxShow = 5
  let start = Math.max(1, pageNo.value - 2)
  let end = Math.min(totalPages.value, start + maxShow - 1)
  if (end - start < maxShow - 1) {
    start = Math.max(1, end - maxShow + 1)
  }
  for (let i = start; i <= end; i++) range.push(i)
  return range
})

const prevPage = () => {
  if (pageNo.value > 1) {
    pageNo.value--
    fetchData()
  }
}

const nextPage = () => {
  if (pageNo.value < totalPages.value) {
    pageNo.value++
    fetchData()
  }
}

const gotoPage = (n: number) => {
  if (n !== pageNo.value) {
    pageNo.value = n
    fetchData()
  }
}

const toDetail = (id: number) => {
  router.push(`/blog/detail/${id}`)
}

onMounted(fetchData)

// 监听查询条件变化
watch(
  () => ({ ...props.queryParams }),
  () => {
    pageNo.value = 1 // 重置页码
    fetchData()
  },
  { deep: true }
)
</script>

<style scoped lang="scss">
.practice-article-list {
  .article-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 24px;
    justify-content: flex-start;
  }

  .article-card {
    perspective: 1000px;
    cursor: pointer;
    width: 280px;
    height: 220px;
    position: relative;
    display: inline-block;
    border-radius: 12px;
    margin-bottom: 8px;

    > .front,
    > .back {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      border-radius: inherit;
      backface-visibility: hidden;
      transition: transform 0.6s cubic-bezier(.175,.885,.32,1.275), opacity 0.6s ease;
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      padding: 1em 1.5em;
      color: #fff;
    }

    > .front {
      background-size: cover;
      background-position: center;
      transform: rotateY(0deg);
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
      position: relative;

      /* 置顶标识 */
      .top-badge {
        position: absolute;
        top: 12px;
        right: 12px;
        background: linear-gradient(135deg, #ff6b6b, #ee5a24);
        color: white;
        padding: 4px 8px;
        border-radius: 12px;
        font-size: 11px;
        font-weight: bold;
        display: flex;
        align-items: center;
        gap: 2px;
        box-shadow: 0 2px 8px rgba(255, 107, 107, 0.3);
        z-index: 2;

        .top-icon {
          width: 12px;
          height: 12px;
          fill: currentColor;
        }
      }

      /* 分类标签 */
      .category-tag {
        position: absolute;
        top: 12px;
        left: 12px;
        background: rgba(214, 40, 40, 0.9);
        color: white;
        padding: 4px 8px;
        border-radius: 8px;
        font-size: 11px;
        font-weight: 500;
        backdrop-filter: blur(4px);
        z-index: 2;
      }

      /* 玻璃背景遮罩 */
      &::after {
        content: "";
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 60px;
        background: rgba(0, 0, 0, 0.35);
        backdrop-filter: blur(6px);
        -webkit-backdrop-filter: blur(6px);
        border-bottom-left-radius: inherit;
        border-bottom-right-radius: inherit;
      }

      .title {
        position: relative;
        font-size: 18px;
        font-weight: 700;
        top: 8px;
        z-index: 1;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }

    > .back {
      background: #313131;
      transform: rotateY(-180deg);
      opacity: 0;
      padding: 1.2em 1.5em;
      justify-content: flex-start;

      .back-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 8px;

        .back-title {
          font-size: 16px;
          margin: 0;
          color: #d62828;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          flex: 1;
          margin-right: 8px;
        }

        .top-badge-small {
          background: linear-gradient(135deg, #ff6b6b, #ee5a24);
          color: white;
          padding: 2px 4px;
          border-radius: 6px;
          font-size: 9px;
          display: flex;
          align-items: center;
          flex-shrink: 0;

          .top-icon-small {
            width: 10px;
            height: 10px;
            fill: currentColor;
          }
        }
      }

      .summary {
        font-size: 13px;
        color: #ddd;
        margin-bottom: 10px;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
        line-height: 1.4;
      }

      .tags-section {
        margin-bottom: 8px;

        .tags-list {
          display: flex;
          flex-wrap: wrap;
          gap: 4px;
          align-items: center;

          .tag-item {
            background: rgba(214, 40, 40, 0.2);
            color: #ff7979;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 10px;
            font-weight: 500;
            border: 1px solid rgba(214, 40, 40, 0.3);
          }

          .more-tags {
            color: #aaa;
            font-size: 10px;
            font-style: italic;
          }
        }
      }

      .category-info {
        margin-bottom: 8px;
        font-size: 11px;

        .category-label {
          color: #aaa;
          margin-right: 4px;
        }

        .category-name {
          color: #d62828;
          font-weight: 500;
        }
      }

      .meta {
        font-size: 11px;
        color: #aaa;
        display: flex;
        justify-content: space-between;
        margin-top: auto;
      }
    }

    &:hover {
      > .front {
        transform: rotateY(180deg);
      }
      > .back {
        transform: rotateY(0deg);
        opacity: 1;
      }
    }
  }

  .pagination-wrapper {
    margin-top: 24px;
    text-align: center;

    .pagination-nav {
      display: inline-flex;
      align-items: center;
      gap: 4px;
    }

    .page-button,
    .page-number {
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      border: 1px solid #e5e7eb;
      background: #fff;
      color: #000;
      cursor: pointer;
      transition: all 0.2s ease;

      &:hover:not(:disabled) {
        border-color: #d1d5db;
      }

      &:disabled {
        cursor: not-allowed;
        opacity: 0.4;
      }
    }

    .page-number.active {
      background: #d62828;
      color: #fff;
      border-color: #d62828;
      pointer-events: none;
    }

    .icon {
      width: 14px;
      height: 14px;
      fill: currentColor;
    }
  }

  .empty {
    text-align: center;
    color: #999;
    padding: 40px 0;
  }
}
</style> 
