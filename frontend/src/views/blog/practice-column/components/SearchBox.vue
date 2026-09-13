<template>
  <div class="search-container">
    <!-- 搜索框区域 -->
    <div class="search-box-wrapper">
      <div class="search-box">
        <div class="search-icon">
          <el-icon><Search /></el-icon>
        </div>
        <form class="search-form" @submit.prevent="handleSearch">
          <input
            style="margin-left: 20px"
            type="text" 
            v-model="queryParams.keyword"
            placeholder="搜索实践故事..." 
            id="search" 
            autocomplete="off"
            @focus="handleFocus"
            @blur="handleBlur"
            @input="handleInput"
          />
        </form>
        <svg 
          class="search-border" 
          :class="{ 'border-searching': isFocused }" 
          version="1.1" 
          xmlns="http://www.w3.org/2000/svg" 
          viewBox="0 0 671 111"
        >
          <path class="border" d="M335.5,108.5h-280c-29.3,0-53-23.7-53-53v0c0-29.3,23.7-53,53-53h280"/>
          <path class="border" d="M335.5,108.5h280c29.3,0,53-23.7,53-53v0c0-29.3-23.7-53-53-53h-280"/>
        </svg>
        <div class="go-icon" :class="{ 'go-in': hasInput }" @click="handleSearch">
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </div>

    <!-- 分类选择 -->
    <div class="filter-section">
      <div class="filter-title">
        <el-icon><Collection /></el-icon>
        <span>主题分类</span>
      </div>
      <div class="filter-content">
        <el-space wrap :size="10">
          <el-tag
            v-for="item in categoryOptions"
            :key="item.id"
            :class="['category-tag', { active: queryParams.categoryId === item.id }]"
            @click="handleCategoryClick(item.id)"
            :effect="queryParams.categoryId === item.id ? 'dark' : 'plain'"
            :type="queryParams.categoryId === item.id ? 'danger' : 'info'"
            round
          >
            {{ item.name }}
          </el-tag>
        </el-space>
      </div>
    </div>

    <!-- 标签选择 -->
    <div class="filter-section">
      <div class="filter-title">
        <el-icon><PriceTag /></el-icon>
        <span>关键词</span>
      </div>
      <div class="filter-content">
        <el-space wrap :size="10">
          <el-tag
            v-for="item in tagOptions"
            :key="item.id"
            :class="['tag-item', { active: queryParams.tagId === item.id }]"
            @click="handleTagClick(item.id)"
            :effect="queryParams.tagId === item.id ? 'dark' : 'plain'"
            :type="queryParams.tagId === item.id ? 'danger' : 'info'"
            size="small"
            round
          >
            {{ item.name }}
          </el-tag>
        </el-space>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="search-buttons">
      <el-button type="danger" @click="handleSearch" round>
        <el-icon><Search /></el-icon>
        <span>搜索</span>
      </el-button>
      <el-button @click="resetQuery" round>
        <el-icon><Refresh /></el-icon>
        <span>重置</span>
      </el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { 
  Search, 
  Collection, 
  PriceTag, 
  Refresh, 
  ArrowRight 
} from '@element-plus/icons-vue'
import { CategoryApi } from '@/api/blog/category'
import { TagApi } from '@/api/blog/tag'

// 分类和标签接口
interface CategoryItem {
  id: number
  name: string
}

interface TagItem {
  id: number
  name: string
}

// 查询参数对象
const queryParams = reactive({
  pageNo: 1,
  pageSize: 16,
  keyword: '',
  categoryId: undefined as number | undefined,
  tagId: undefined as number | undefined
})

// 组件状态
const isFocused = ref(false)
const hasInput = computed(() => queryParams.keyword.length > 0)

// 标签与分类
const categoryOptions = ref<CategoryItem[]>([])
const tagOptions = ref<TagItem[]>([])

// 事件定义
const emit = defineEmits(['search', 'reset'])

// 获取分类列表
const getCategoryList = async () => {
  try {
    const res = await CategoryApi.getCategoryList({}) // 传递空对象以满足API要求
    if (res) {
      categoryOptions.value = res
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取标签列表
const getTagList = async () => {
  try {
    const res = await TagApi.getTagList({}) // 传递空对象以满足API要求
    if (res) {
      tagOptions.value = res
    }
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

// 处理搜索按钮点击
const handleSearch = () => {
  queryParams.pageNo = 1
  emit('search', { ...queryParams })
}

// 重置查询条件
const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.categoryId = undefined
  queryParams.tagId = undefined
  queryParams.pageNo = 1
  emit('reset', { ...queryParams })
}

// 处理分类点击
const handleCategoryClick = (id: number) => {
  queryParams.categoryId = queryParams.categoryId === id ? undefined : id
}

// 处理标签点击
const handleTagClick = (id: number) => {
  queryParams.tagId = queryParams.tagId === id ? undefined : id
}

// 搜索框事件处理
const handleFocus = () => {
  isFocused.value = true
}

const handleBlur = () => {
  isFocused.value = false
}

const handleInput = () => {
  // 输入时的处理逻辑可以在这里添加
}

// 页面挂载时获取数据
onMounted(() => {
  getCategoryList()
  getTagList()
})
</script>

<style lang="scss" scoped>
$primary-color: #c52121; // 红色主题色

.search-container {
  background-color: #fff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border-top: 4px solid $primary-color;
  margin-bottom: 20px;
}

// 搜索框样式 - 参考 practice-search.txt
.search-box-wrapper {
  margin-bottom: 30px;
  display: flex;
  justify-content: center;
}

.search-box {
  position: relative;
  width: 100%;
  max-width: 500px;
  height: 60px;
  border-radius: 30px;
  margin: 0 auto;
}

.search-icon, .go-icon {
  position: absolute;
  top: 0;
  height: 60px;
  width: 60px;
  line-height: 61px;
  text-align: center;
  z-index: 10;
}

.search-icon {
  left: 0;
  pointer-events: none;
  font-size: 1.2em;
  color: $primary-color;
  will-change: transform;
  transform: rotate(-40deg);
  transform-origin: center center;
  transition: transform 400ms 220ms cubic-bezier(0.190, 1.000, 0.220, 1.000);
}

.search-box:hover .search-icon,
.search-box.border-searching .search-icon {
  transform: rotate(0deg);
}

.go-icon {
  right: 0;
  pointer-events: none;
  font-size: 1.2em;
  color: $primary-color;
  cursor: default;
  opacity: 0;
  transform: rotate(45deg);
  transition: opacity 190ms ease-out, transform 260ms cubic-bezier(0.190, 1.000, 0.220, 1.000);
}

.go-icon.go-in {
  opacity: 1;
  pointer-events: all;
  cursor: pointer;
  transform: rotate(0);
  transition: opacity 190ms ease-out, transform 260ms 20ms cubic-bezier(0.190, 1.000, 0.220, 1.000);
}

.search-border {
  display: block;
  width: 100%;
  max-width: 500px;
  height: 60px;
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
}

.border {
  fill: none;
  stroke: $primary-color;
  stroke-width: 3;
  stroke-miterlimit: 10;
  stroke-dasharray: 740;
  stroke-dashoffset: 0;
  transition: stroke-dashoffset 400ms cubic-bezier(0.600, 0.040, 0.735, 0.990);
}

.border-searching .border {
  stroke-dasharray: 740;
  stroke-dashoffset: 459;
  transition: stroke-dashoffset 650ms cubic-bezier(0.755, 0.150, 0.205, 1.000);
}

#search {
  font-family: 'Microsoft YaHei', -apple-system, BlinkMacSystemFont, sans-serif;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 30px;
  border: none;
  background: rgba(255, 255, 255, 0);
  padding: 0 70px 0 70px;
  color: #333;
  font-size: 1.1em;
  font-weight: 400;
  letter-spacing: -0.015em;
  outline: none;
  z-index: 5;

  &::placeholder {
    color: #999;
  }

  &::-webkit-input-placeholder {
    color: #999;
  }

  &::-moz-placeholder {
    color: #999;
  }

  &:-ms-input-placeholder {
    color: #999;
  }

  &:-moz-placeholder {
    color: #999;
  }

  &::-moz-selection {
    color: #333;
    background: rgba($primary-color, 0.25);
  }

  &::selection {
    color: #333;
    background: rgba($primary-color, 0.25);
  }
}

// 过滤区域样式
.filter-section {
  margin-bottom: 24px;

  .filter-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 16px;
    color: #333;

    .el-icon {
      color: $primary-color;
      font-size: 18px;
    }
  }

  .filter-content {
    .category-tag,
    .tag-item {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
      }

      &.active {
        transform: translateY(-2px);
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
      }
    }
  }
}

// 按钮区域
.search-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 24px;

  .el-button {
    min-width: 120px;
    height: 40px;
    padding: 0 25px;

    .el-icon {
      margin-right: 4px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .search-container {
    padding: 20px;
  }

  .search-box {
    max-width: 100%;
  }

  .search-buttons {
    flex-direction: column;
    align-items: center;

    .el-button {
      width: 200px;
    }
  }
}
</style> 
