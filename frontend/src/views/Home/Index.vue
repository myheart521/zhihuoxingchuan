<template>
  <div class="blog-home-container">
    <!-- 顶部横幅 - 战斗精神主题 -->
    <div class="hero-banner" :style="{backgroundImage: `url(${default_cover}), linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7))`}">
      <div class="hero-content">
        <h1 class="hero-title">特别能战斗精神</h1>
        <p class="hero-subtitle">弘扬革命传统，传承红色基因，激发奋斗热情</p>
        <div class="hero-actions">
          <el-button type="danger" size="large" @click="router.push('/content/add')">
            <el-icon>
              <Edit />
            </el-icon>
            分享你的战斗故事
          </el-button>
        </div>
      </div>
    </div>

    <!-- 精选卡片区 -->
    <div class="feature-section">
      <h2 class="section-title">精神力量</h2>
      <div class="container">
        <div class="slide">
          <div 
            v-for="item in carouselItems" 
            :key="item.id" 
            class="item" 
            :style="{background: `url(${item.image || default_cover})`}"
          >
            <div class="content">
              <div class="name">{{ item.title }}</div>
              <div class="description" v-html="item.description"></div>
              <button v-if="item.link" @click.stop="openLink(item.link)">深入了解</button>
            </div>
          </div>
        </div>

        <div class="button">
          <button class="prev" @click="prevSlide"><i class="el-icon"><ArrowLeft /></i></button>
          <button class="next" @click="nextSlide"><i class="el-icon"><ArrowRight /></i></button>
        </div>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 左侧内容区 -->
      <div class="content-left">
        <!-- 搜索区域 -->
        <div class="search-container">
          <div class="search-input-wrapper">
            <el-input
              v-model="queryParams.keyword"
              placeholder="搜索战斗故事..."
              class="search-input"
              prefix-icon="Search"
              clearable
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon>
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </div>

          <!-- 分类选择 -->
          <div class="filter-section">
            <div class="filter-title">
              <el-icon>
                <Collection />
              </el-icon>
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
              <el-icon>
                <PriceTag />
              </el-icon>
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

          <div class="search-buttons">
            <el-button type="danger" @click="handleSearch" round>
              <el-icon>
                <Search />
              </el-icon>
              <span>搜索</span>
            </el-button>
            <el-button @click="resetQuery" round>
              <el-icon>
                <Refresh />
              </el-icon>
              <span>重置</span>
            </el-button>
          </div>
        </div>

        <!-- 使用CSS Grid布局的博客列表 -->
        <div class="blog-grid-container" v-loading="loading">
          <h2 class="section-title">战斗故事</h2>
          <div class="blog-grid">
            <figure
              v-for="item in sortedBlogList"
              :key="item.id"
              :class="{ featured: item.isTop, 'no-cover': !item.frontCover }"
              @click="router.push(`/blog/detail/${item.id}`)"
            >
              <img :src="item.frontCover || default_cover" :alt="item.title" />

              <figcaption>
                <div class="blog-meta">
                  <div class="meta-left">
                    <el-avatar :size="24" class="author-avatar">
                      {{ item.author?.username?.charAt(0)?.toUpperCase() }}
                    </el-avatar>
                    <span class="author-name">{{ item.author?.username }}</span>
                  </div>
                  <div class="meta-right">
                    <span class="create-time">
                      <el-icon><Timer /></el-icon>
                      {{ formatDate(item.createTime) }}
                    </span>
                  </div>
                </div>

                <h3>
                  <el-tag v-if="item.isTop" type="danger" size="small" class="top-tag">置顶</el-tag>
                  {{ item.title }}
                </h3>
                <p>{{ item.summary }}</p>

                <div class="blog-tags">
                  <el-tag size="small" type="danger" effect="plain" class="category-tag">
                    {{ item.category?.name }}
                  </el-tag>
                  <el-tag
                    v-for="tag in item.tags"
                    :key="tag.id"
                    size="small"
                    type="info"
                    effect="plain"
                    class="tag-item"
                  >
                    {{ tag.name }}
                  </el-tag>
                </div>

                <div class="blog-stats">
                  <span class="stat-item">
                    <el-icon><View /></el-icon>
                    {{ item.viewCount }}
                  </span>
                  <span class="stat-item">
                    <el-icon><Star /></el-icon>
                    {{ item.likeCount }}
                  </span>
                </div>
              </figcaption>
            </figure>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="queryParams.pageNo"
            v-model:page-size="queryParams.pageSize"
            :page-sizes="[16, 24, 32]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>

      <!-- 右侧固定边栏 -->
      <div class="sidebar-right">
        <!-- 用户信息卡片 -->
        <div class="user-card" v-if="isLoggedIn">
          <div class="user-header">
            <div class="avatar-wrapper">
              <el-avatar
                :size="90"
                :src="
                  userInfo.avatar ||
                  'https://example.invalid/resource'
                "
              />
              <div
                class="user-role"
                :class="{'admin': isAdmin}"
              >
                {{ userRoleName }}
              </div>
            </div>
            <h3 class="username">{{ userInfo.username }}</h3>
            <p class="email">
              <el-icon>
                <Message />
              </el-icon>
              {{ userInfo.email || '未设置邮箱' }}
            </p>
          </div>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.blogCount }}</div>
              <div class="stat-label">战斗故事</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.likeCount }}</div>
              <div class="stat-label">获赞</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userInfo.reputation }}</div>
              <div class="stat-label">信誉分</div>
            </div>
          </div>
          <div class="user-actions">
            <el-button type="danger" size="small" round plain @click="router.push('/content/add')">
              <el-icon>
                <Edit />
              </el-icon>
              写战斗故事
            </el-button>
            <el-button type="info" size="small" round plain @click="router.push('/user/profile')">
              <el-icon>
                <User />
              </el-icon>
              个人中心
            </el-button>
          </div>
        </div>

        <!-- 未登录用户显示登录卡片 -->
        <div class="guest-card" v-else>
          <div class="guest-header">
            <el-avatar :size="90" icon="el-icon-user" />
            <h3 class="guest-title">欢迎来到战斗精神平台</h3>
            <p class="guest-subtitle">登录后分享你的战斗故事</p>
          </div>
          <div class="guest-actions">
            <el-button type="danger" @click="router.push('/login')" round>
              <el-icon>
                <User />
              </el-icon>
              登录
            </el-button>
            <el-button @click="router.push('/login')" round>
              <el-icon>
                <Plus />
              </el-icon>
              注册
            </el-button>
          </div>
        </div>

        <!-- 战斗精神口号 -->
        <div class="spirit-panel panel">
          <h3 class="panel-title">
            <el-icon>
              <Bell />
            </el-icon>
            精神口号
          </h3>
          <div class="spirit-quotes">
            <div v-for="(quote, index) in spiritQuotes" :key="index" class="spirit-quote">
              <el-icon>
                <InfoFilled />
              </el-icon>
              <span>{{ quote }}</span>
            </div>
          </div>
        </div>

        <!-- 每日鸡汤 替换为 战斗格言 -->
        <div class="daily-quote panel">
          <h3 class="panel-title">
            <el-icon>
              <Coffee />
            </el-icon>
            战斗格言
          </h3>
          <div class="quote-content">
            <span class="quote-icon">"</span>
            {{ dailyQuote }}
            <span class="quote-icon">"</span>
          </div>
        </div>

        <!-- 网站统计 -->
        <div class="site-stats panel">
          <h3 class="panel-title">
            <el-icon>
              <DataAnalysis />
            </el-icon>
            战斗数据
          </h3>
          <ul class="stats-list">
            <li>
              <el-icon>
                <View />
              </el-icon>
              <span>故事阅读：</span>
              <strong>{{ siteInfo.totalVisits }}</strong>
            </li>
            <li>
              <el-icon>
                <Document />
              </el-icon>
              <span>战斗故事：</span>
              <strong>{{ siteInfo.totalPosts }}</strong>
            </li>
            <li>
              <el-icon>
                <User />
              </el-icon>
              <span>战士人数：</span>
              <strong>{{ siteInfo.totalUsers }}</strong>
            </li>
            <li>
              <el-icon>
                <Timer />
              </el-icon>
              <span>战斗天数：</span>
              <strong>{{ siteInfo.runningDays }}</strong>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { formatTime } from '@/utils'
import {
  Search,
  Collection,
  PriceTag,
  Refresh,
  View,
  Star,
  Bell,
  Coffee,
  DataAnalysis,
  Document,
  User,
  Timer,
  Message,
  Edit,
  InfoFilled,
  Plus,
  ArrowLeft,
  ArrowRight
} from '@element-plus/icons-vue'
import default_cover from "@/assets/imgs/default_cover.png"

import { CarouselApi } from '@/api/blog/carousel'
import { CategoryApi } from '@/api/blog/category'
import { TagApi } from '@/api/blog/tag'
import { useRouter } from 'vue-router'
import { BlogApi } from '@/api/blog/blog'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getUserProfile } from '@/api/system/user/profile'
import { useUserStore } from '@/store/modules/user'
import { getAccessToken } from '@/utils/auth'

// 修改轮播图数据接口定义
interface CarouselItem {
  id: number
  image: string
  title: string
  description: string
  link: string
  sort: number
  createTime: string
}

// 博客项目接口
interface BlogItem {
  id: number
  title: string
  summary: string
  content: string
  frontCover: string
  isTop: boolean
  viewCount: number
  likeCount: number
  commentCount: number
  createTime: string
  updateTime: string
  tags: any[]
  category: {
    id: number
    name: string
  }
  author: {
    id: number
    username: string
    avatar: string
  }
}

// 分类和标签接口
interface CategoryItem {
  id: number
  name: string
}

interface TagItem {
  id: number
  name: string
}

// 卡片效果状态
interface CardState {
  width: number
  height: number
  mouseX: number
  mouseY: number
  mouseLeaveDelay: ReturnType<typeof setTimeout> | null
}

// 使用路由
const router = useRouter()

// 查询参数对象
const queryParams = reactive({
  pageNo: 1,
  pageSize: 16,
  keyword: '',
  categoryId: undefined as number | undefined,
  tagId: undefined as number | undefined
})

// 加载状态
const loading = ref(false)

// 列表总数
const total = ref(0)

// 标签与分类
const categoryOptions = ref<CategoryItem[]>([])
const tagOptions = ref<TagItem[]>([])

// 用户状态
const userStore = useUserStore()
const isLoggedIn = computed(() => !!getAccessToken())
const userInfo = reactive({
  username: '',
  email: '',
  avatar: '',
  blogCount: 0,
  likeCount: 0,
  reputation: 0
})
const role = ref('')

// 网站统计信息
const siteInfo = reactive({
  totalVisits: 0,
  totalPosts: 0,
  totalUsers: 0,
  runningDays: 0
})

// 博客列表
const blogList = ref<BlogItem[]>([])
const sortedBlogList = computed(() => {
  return [...blogList.value].sort((a, b) => {
    // 置顶文章排在前面
    if (a.isTop && !b.isTop) return -1
    if (!a.isTop && b.isTop) return 1
    // 按创建时间倒序排列
    return new Date(b.createTime).getTime() - new Date(a.createTime).getTime()
  })
})

// 战斗精神口号
const spiritQuotes = ref([
  '特别能战斗，永远跟党走',
  '发扬战斗精神，争做时代先锋',
  '传承红色基因，弘扬奋斗精神',
  '不忘初心，牢记使命，永远奋斗',
  '团结一心，共同奋斗，实现伟大复兴'
])

// 修改为战斗格言
const dailyQuotes = [
  '不经历风雨，怎能见彩虹，不经历奋斗，怎能获成功。',
  '历经千辛万苦，方能成就大业，战胜重重困难，方能铸就辉煌。',
  '奋斗是青春最亮丽的底色，行动是青年最有效的磨砺。',
  '艰难困苦，玉汝于成，千淘万漉，方显真金。',
  '万里长征，不畏艰险，一往无前，勇往直前。'
]

// 修改轮播图数据的ref
const carouselItems = ref<CarouselItem[]>([])

// 卡片引用
const cardRefs = ref<HTMLElement[]>([])

// 卡片状态管理
const cardStates = reactive<CardState[]>([])

// 处理鼠标移动 - 修改为使用索引而不是id
const handleMouseMove = (e: MouseEvent, index: number) => {
  if (!cardStates[index]) {
    initCardState(index)
  }
  
  if (!cardRefs.value[index]) return
  
  const card = cardRefs.value[index]
  const state = cardStates[index]
  
  state.mouseX = e.pageX - card.offsetLeft - state.width/2
  state.mouseY = e.pageY - card.offsetTop - state.height/2
}

// 处理鼠标进入
const handleMouseEnter = (index: number) => {
  if (cardStates[index]?.mouseLeaveDelay) {
    clearTimeout(cardStates[index].mouseLeaveDelay as unknown as number)
  }
}

// 处理鼠标离开
const handleMouseLeave = (index: number) => {
  if (!cardStates[index]) return
  
  cardStates[index].mouseLeaveDelay = setTimeout(() => {
    cardStates[index].mouseX = 0
    cardStates[index].mouseY = 0
  }, 1000)
}

// 初始化卡片状态
const initCardState = (index: number) => {
  if (!cardRefs.value[index]) return
  
  const cardElement = cardRefs.value[index]
  
  // 确保有足够的状态对象
  while (cardStates.length <= index) {
    cardStates.push({
      width: 0,
      height: 0,
      mouseX: 0,
      mouseY: 0,
      mouseLeaveDelay: null
    })
  }
  
  cardStates[index] = {
    width: cardElement.offsetWidth,
    height: cardElement.offsetHeight,
    mouseX: 0,
    mouseY: 0,
    mouseLeaveDelay: null
  }
}

// 获取卡片样式
const getCardStyle = (index: number) => {
  if (!cardStates[index]) return {}
  
  const state = cardStates[index]
  const mousePX = state.mouseX / state.width
  const mousePY = state.mouseY / state.height
  const rX = mousePX * 30
  const rY = mousePY * -30
  
  return {
    transform: `rotateY(${rX}deg) rotateX(${rY}deg)`
  }
}

// 获取卡片背景变换
const getCardBgTransform = (index: number) => {
  if (!cardStates[index]) return {}
  
  const state = cardStates[index]
  const mousePX = state.mouseX / state.width
  const mousePY = state.mouseY / state.height
  const tX = mousePX * -40
  const tY = mousePY * -40
  
  return {
    transform: `translateX(${tX}px) translateY(${tY}px)`
  }
}

// 打开链接
const openLink = (url: string) => {
  window.open(url, '_blank')
}

// 获取轮播图数据时初始化卡片状态
const getCarousel = async () => {
  try {
    const res = await CarouselApi.getCarouselList()
    if (res) {
      console.log('轮播图数据:resdata', res)
      // 按照 sort 字段排序
      carouselItems.value = res.sort(
        (a: CarouselItem, b: CarouselItem) => (a.sort || 0) - (b.sort || 0)
      )
      
      // 初始化卡片状态
      setTimeout(() => {
        for (let i = 0; i < carouselItems.value.length; i++) {
          initCardState(i)
        }
      }, 100)
    }
  } catch (error) {
    console.error('获取轮播图数据失败:', error)
  }
}

// 格式化时间
const formatDate = (time: string | number) => {
  if (!time) return ''
  return dayjs(new Date(time)).format('YYYY-MM-DD')
}

// 获取博客列表数据
const getBlogList = async () => {
  try {
    loading.value = true
    const res = await BlogApi.getIndexBlogPage(queryParams)
    if (res) {
      blogList.value = res.list || []
      total.value = res.total || 0
    }
  } catch (error) {
    console.error('获取博客列表失败:', error)
    ElMessage.error('获取博客列表失败')
  } finally {
    loading.value = false
  }
}

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

// 获取用户信息
const getUserInfo = async () => {
  if (!isLoggedIn.value) return
  
  try {
    const res = await getUserProfile()
    if (res) {
      Object.assign(userInfo, res.user || res)
      // 正确处理roles数组
      role.value = res.roles || []
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 获取站点统计信息
const getSiteInfo = async () => {
  try {
    // 模拟数据，实际应替换为真实API
    siteInfo.totalVisits = 12345
    siteInfo.totalPosts = 789
    siteInfo.totalUsers = 456
    siteInfo.runningDays = Math.floor((new Date().getTime() - new Date('2023-01-01').getTime()) / (24 * 60 * 60 * 1000))
  } catch (error) {
    console.error('获取站点统计信息失败:', error)
  }
}

// 处理搜索按钮点击
const handleSearch = () => {
  queryParams.pageNo = 1
  getBlogList()
}

// 重置查询条件
const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.categoryId = undefined
  queryParams.tagId = undefined
  queryParams.pageNo = 1
  getBlogList()
}

// 处理分类点击
const handleCategoryClick = (id: number) => {
  queryParams.categoryId = queryParams.categoryId === id ? undefined : id
}

// 处理标签点击
const handleTagClick = (id: number) => {
  queryParams.tagId = queryParams.tagId === id ? undefined : id
}

// 处理页码变化
const handleCurrentChange = (val: number) => {
  queryParams.pageNo = val
  getBlogList()
}

// 处理每页条数变化
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  queryParams.pageNo = 1
  getBlogList()
}

// 每日格言
const dailyQuote = ref(dailyQuotes[Math.floor(Math.random() * dailyQuotes.length)])

// 轮播图控制函数
const nextSlide = () => {
  if (!carouselItems.value.length) return
  
  // 轮换数组中的第一个元素到最后
  const firstItem = carouselItems.value.shift()
  if (firstItem) {
    carouselItems.value.push(firstItem)
  }
}

const prevSlide = () => {
  if (!carouselItems.value.length) return
  
  // 轮换数组中的最后一个元素到最前
  const lastItem = carouselItems.value.pop()
  if (lastItem) {
    carouselItems.value.unshift(lastItem)
  }
}

// 用户角色相关的计算属性
const isAdmin = computed(() => {
  if (!role.value || role.value.length === 0) return false
  return role.value.some(r => r.name === '超级管理员' || r.name === '博客管理员')
})

const userRoleName = computed(() => {
  if (!role.value || role.value.length === 0) return '普通用户'
  return role.value.map(r => r.name).join('/')
})

// 页面挂载时获取数据
onMounted(() => {
  getCarousel()
  getBlogList()
  getCategoryList()
  getTagList()
  getUserInfo()
  getSiteInfo()

})
</script>

<style lang="scss" scoped>
.blog-home-container {
  padding: 0 0 40px 0;
  background-color: #f5f5f5;
  font-family:
    'Microsoft YaHei',
    -apple-system,
    BlinkMacSystemFont,
    'Segoe UI',
    Roboto,
    sans-serif;

  // 主题色变量
  $primary-color: #c52121; // 红色主题色
  $secondary-color: #333;
  $text-color: #333;
  $highlight-color: #f5e3e3;
  $light-color: #f8f8f8;
  $dark-color: #222;

  // 顶部横幅
  .hero-banner {
    width: 100%;
    height: 500px;
    background-image: url('/banner_bg.jpg'), linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7));
    background-size: cover;
    background-position: center;
    background-blend-mode: overlay;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    position: relative;
    margin-bottom: 60px;

    &::before {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 100px;
      background: linear-gradient(to top, rgba(245, 245, 245, 1), transparent);
      z-index: 1;
    }

    .hero-content {
      max-width: 800px;
      padding: 0 20px;
      z-index: 2;
    }

    .hero-title {
      font-size: 4rem;
      font-weight: 700;
      margin-bottom: 20px;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
      letter-spacing: 3px;

      @media (max-width: 768px) {
        font-size: 2.5rem;
      }
    }

    .hero-subtitle {
      font-size: 1.5rem;
      margin-bottom: 30px;
      opacity: 0.9;
      font-weight: 300;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);

      @media (max-width: 768px) {
        font-size: 1.2rem;
      }
    }

    .hero-actions {
      margin-top: 30px;

      .el-button {
        height: 50px;
        font-size: 16px;
        padding: 0 30px;

        .el-icon {
          margin-right: 8px;
        }
      }
    }
  }

  // 精选内容区域
  .feature-section {
    padding: 0 0 60px;
    margin: 0 auto 40px;
    
    .section-title {
      text-align: center;
      font-size: 2rem;
      font-weight: 700;
      color: $primary-color;
      margin-bottom: 30px;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 4px;
        background-color: $primary-color;
        border-radius: 2px;
      }
    }

    .container {
      position: relative;
      margin: 0 auto;
      width: 1000px;
      height: 500px;
      background: #f5f5f5;
      box-shadow: 0 30px 50px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      
      .slide {
        width: 100%;
        height: 100%;
        
        .item {
          width: 200px;
          height: 300px;
          position: absolute;
          top: 50%;
          transform: translate(0, -50%);
          border-radius: 20px;
          box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);
          background-position: center;
          background-size: cover;
          background-repeat: no-repeat;
          display: inline-block;
          transition: 0.5s;
          cursor: pointer;
          
          &:nth-child(1), &:nth-child(2) {
            top: 0;
            left: 0;
            transform: translate(0, 0);
            border-radius: 0;
            width: 100%;
            height: 100%;
          }
          
          &:nth-child(2) .content {
            display: block;
          }
          
          &:nth-child(3) {
            left: 50%;
          }
          
          &:nth-child(4) {
            left: calc(50% + 220px);
          }
          
          &:nth-child(5) {
            left: calc(50% + 440px);
          }
          
          &:nth-child(n + 6) {
            left: calc(50% + 660px);
            opacity: 0;
          }
          
          .content {
            position: absolute;
            top: 50%;
            left: 100px;
            width: 300px;
            text-align: left;
            color: #fff;
            transform: translate(0, -50%);
            display: none;
            text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
            
            .name {
              font-size: 40px;
              text-transform: uppercase;
              font-weight: bold;
              opacity: 0;
              animation: animate 1s ease-in-out 1 forwards;
            }
            
            .description {
              margin: 15px 0 25px;
              opacity: 0;
              animation: animate 1s ease-in-out 0.3s 1 forwards;
              font-size: 16px;
              line-height: 1.5;
            }
            
            button {
              padding: 10px 20px;
              background-color: $primary-color;
              color: white;
              border: none;
              border-radius: 5px;
              cursor: pointer;
              font-size: 16px;
              opacity: 0;
              animation: animate 1s ease-in-out 0.6s 1 forwards;
              transition: all 0.3s;
              
              &:hover {
                background-color: darken($primary-color, 10%);
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
              }
            }
          }
        }
      }
      
      .button {
        width: 100%;
        text-align: center;
        position: absolute;
        bottom: 20px;
        z-index: 10;
        
        button {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background-color: rgba(255, 255, 255, 0.7);
          border: none;
          cursor: pointer;
          margin: 0 5px;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          transition: 0.3s;
          
          &:hover {
            background-color: $primary-color;
            color: white;
            transform: scale(1.1);
          }
          
          .el-icon {
            font-size: 18px;
          }
        }
      }
    }
  }

  @keyframes animate {
    from {
      opacity: 0;
      transform: translate(0, 50px);
      filter: blur(10px);
    }
    to {
      opacity: 1;
      transform: translate(0);
      filter: blur(0);
    }
  }

  // 主内容区域
  .main-content {
    display: flex;
    gap: 30px;
    max-width: 1440px;
    margin: 0 auto;
    padding: 0 40px;

    // 左侧内容区
    .content-left {
      flex: 1;
      min-width: 0;

      .search-container {
        margin-bottom: 20px;
        background-color: #fff;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
        border-top: 4px solid $primary-color;

        .search-input-wrapper {
          margin-bottom: 24px;

          .search-input {
            :deep(.el-input__wrapper) {
              height: 50px;
              border-radius: 25px;
              padding: 0 15px;
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
              transition: all 0.3s;

              &.is-focus {
                box-shadow: 0 0 0 2px rgba($primary-color, 0.2);
              }
            }

            :deep(.el-input__inner) {
              font-size: 16px;
            }
          }
        }

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
      }

      .blog-grid-container {
        margin-bottom: 30px;
        min-height: 200px;

        .section-title {
          font-size: 1.8rem;
          font-weight: 700;
          color: $primary-color;
          margin-bottom: 20px;
          position: relative;
          padding-left: 15px;

          &::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 5px;
            height: 24px;
            background-color: $primary-color;
            border-radius: 2px;
          }
        }
      }

      .blog-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        grid-auto-rows: 230px;
        grid-auto-flow: dense;
        gap: 16px;

        @media (max-width: 768px) {
          grid-template-columns: 1fr;
        }

        figure {
          container: figure / inline-size;
          overflow: hidden;
          position: relative;
          border-radius: 8px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
          background-color: #fff;
          transition:
            transform 0.3s ease,
            box-shadow 0.3s ease;
          cursor: pointer;
          display: grid;
          grid-template-columns: minmax(0, 1fr);
          grid-template-rows: minmax(0, 1fr);

          &:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);

            img {
              transform: scale(1.05);
            }
          }

          // 置顶文章样式 - 设计更明显
          &.featured {
            grid-column: span 2;
            grid-row: span 1;
            border: 2px solid rgba($primary-color, 0.3);

            @media (max-width: 768px) {
              grid-column: span 1;
            }

            figcaption {
              h3 {
                font-size: 1.3rem;
              }
            }

            &::before {
              content: '';
              position: absolute;
              top: 0;
              right: 0;
              width: 60px;
              height: 60px;
              background-color: $primary-color;
              clip-path: polygon(0 0, 100% 0, 100% 100%);
              z-index: 2;
            }
          }
        }

        // 每行第一个元素跨越2行 - 保持比例合理
        @media (min-width: 768px) {
          figure:nth-child(6n + 1):not(.featured) {
            grid-row: span 2;
          }

          // 每行第二个元素跨越2列
          figure:nth-child(8n + 3):not(.featured) {
            grid-column: span 2;
          }
        }

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          grid-area: 1 / 1 / -1 / -1;
          transition: transform 0.6s ease;
        }

        figcaption {
          grid-area: 1 / 1 / -1 / -1;
          align-self: end;
          z-index: 1;
          padding: 16px;
          color: #fff;
          background: linear-gradient(transparent, rgba(0, 0, 0, 0.6) 30%, rgba(0, 0, 0, 0.85));

          h3 {
            font-size: 1.1rem;
            font-weight: 600;
            margin: 6px 0;
            line-height: 1.3;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;

            .top-tag {
              margin-right: 6px;
              vertical-align: middle;
            }
          }

          p {
            margin: 0 0 10px;
            font-size: 13px;
            opacity: 0.9;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            line-height: 1.4;
          }
        }

        .blog-meta {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 6px;

          .meta-left {
            display: flex;
            align-items: center;
            gap: 5px;

            .author-avatar {
              background-color: $primary-color;
              font-size: 12px;
              transform: scale(0.9);
            }

            .author-name {
              font-size: 11px;
              font-weight: 500;
            }
          }

          .meta-right {
            font-size: 11px;
            opacity: 0.8;

            .el-icon {
              margin-right: 3px;
              font-size: 11px;
            }
          }
        }

        .blog-tags {
          display: flex;
          flex-wrap: wrap;
          gap: 5px;
          margin-bottom: 6px;

          .tag-item,
          .category-tag {
            height: 18px;
            line-height: 16px;
            font-size: 10px;
            padding: 0 5px;
          }
        }

        .blog-stats {
          display: flex;
          justify-content: flex-end;
          gap: 10px;
          font-size: 11px;

          .stat-item {
            display: flex;
            align-items: center;
            gap: 3px;

            .el-icon {
              font-size: 11px;
            }
          }
        }
      }

      .pagination-container {
        display: flex;
        justify-content: center;
        margin-top: 20px;
        margin-bottom: 40px;

        :deep(.el-pagination) {
          .el-pagination__jump,
          .el-pagination__sizes {
            margin-right: 16px;
          }

          .el-pager li.is-active {
            background-color: $primary-color;
            color: white;
          }

          .btn-prev,
          .btn-next {
            &:hover {
              color: $primary-color;
            }
          }
        }
      }
    }

    // 右侧边栏
    .sidebar-right {
      width: 300px;
      flex-shrink: 0;
      position: sticky;
      top: 20px;
      align-self: flex-start;
      margin-top: 0;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background-color: rgba(0, 0, 0, 0.2);
        border-radius: 3px;
      }

      .user-card {
        background: linear-gradient(135deg, $primary-color 0%, #8c1515 100%);
        border-radius: 12px;
        padding: 24px;
        color: white;
        margin-bottom: 20px;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);

        .user-header {
          text-align: center;
          margin-bottom: 20px;

          .avatar-wrapper {
            position: relative;
            display: inline-block;
            margin-bottom: 15px;

            .el-avatar {
              border: 3px solid rgba(255, 255, 255, 0.3);
              box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
            }

            .user-role {
              position: absolute;
              bottom: 0;
              right: 0;
              padding: 2px 8px;
              border-radius: 10px;
              font-size: 12px;
              background: rgba(255, 255, 255, 0.9);
              color: $primary-color;

              &.admin {
                background: #ffd700;
                color: #333;
              }
            }
          }

          .username {
            margin: 0;
            font-size: 18px;
            font-weight: 600;
          }

          .email {
            margin: 5px 0 0;
            font-size: 13px;
            opacity: 0.8;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 4px;
          }
        }

        .user-stats {
          display: flex;
          justify-content: space-around;
          background: rgba(255, 255, 255, 0.1);
          border-radius: 8px;
          padding: 15px 0;
          margin-bottom: 15px;

          .stat-item {
            text-align: center;

            .stat-value {
              font-size: 20px;
              font-weight: 600;
            }

            .stat-label {
              font-size: 12px;
              opacity: 0.8;
              margin-top: 4px;
            }
          }
        }

        .user-actions {
          display: flex;
          justify-content: center;
          gap: 10px;

          .el-button {
            flex: 1;

            .el-icon {
              margin-right: 4px;
            }
          }
        }
      }

      .guest-card {
        background: linear-gradient(135deg, $primary-color 0%, #8c1515 100%);
        border-radius: 12px;
        padding: 24px;
        color: white;
        margin-bottom: 20px;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
        text-align: center;

        .guest-header {
          margin-bottom: 20px;

          .el-avatar {
            margin: 0 auto 15px;
            border: 3px solid rgba(255, 255, 255, 0.3);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
          }

          .guest-title {
            margin: 10px 0 5px;
            font-size: 18px;
            font-weight: 600;
          }

          .guest-subtitle {
            margin: 0;
            font-size: 14px;
            opacity: 0.8;
          }
        }

        .guest-actions {
          display: flex;
          justify-content: center;
          gap: 12px;

          .el-button {
            flex: 1;

            .el-icon {
              margin-right: 4px;
            }
          }
        }
      }

      .panel {
        background: white;
        border-radius: 12px;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
        border-left: 4px solid $primary-color;

        .panel-title {
          display: flex;
          align-items: center;
          gap: 8px;
          margin: 0 0 15px;
          font-size: 16px;
          color: $primary-color;
          font-weight: 600;

          .el-icon {
            color: $primary-color;
            font-size: 18px;
          }
        }
      }

      .spirit-quotes {
        .spirit-quote {
          display: flex;
          align-items: flex-start;
          gap: 8px;
          padding: 10px 0;
          font-size: 14px;
          color: #666;
          border-left: 2px solid transparent;
          padding-left: 10px;
          transition: all 0.3s ease;

          &:hover {
            border-left-color: $primary-color;
            color: $primary-color;
            background-color: rgba($primary-color, 0.05);
            padding-left: 12px;
          }

          .el-icon {
            color: $primary-color;
            margin-top: 3px;
            flex-shrink: 0;
          }

          &:not(:last-child) {
            border-bottom: 1px dashed #eee;
          }
        }
      }

      .quote-content {
        position: relative;
        font-size: 14px;
        color: #666;
        line-height: 1.6;
        padding: 15px 20px;
        font-style: italic;
        background-color: rgba($primary-color, 0.05);
        border-radius: 8px;

        .quote-icon {
          color: $primary-color;
          opacity: 0.5;
          font-size: 24px;
          font-weight: bold;

          &:first-child {
            margin-right: 5px;
            vertical-align: top;
            position: relative;
            top: -5px;
          }

          &:last-child {
            margin-left: 5px;
            vertical-align: bottom;
          }
        }
      }

      .stats-list {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          display: flex;
          align-items: center;
          padding: 10px 0;
          font-size: 14px;
          color: #666;
          border-bottom: 1px dashed #eee;
          transition: all 0.3s ease;

          &:hover {
            background-color: rgba($primary-color, 0.05);
            padding-left: 5px;
          }

          &:last-child {
            border-bottom: none;
          }

          .el-icon {
            color: $primary-color;
            margin-right: 8px;
            font-size: 16px;
          }

          strong {
            margin-left: auto;
            color: $primary-color;
            font-weight: 600;
          }
        }
      }
    }
  }
}

@media (max-width: 992px) {
  .blog-home-container {
    .main-content {
      flex-direction: column;

      .sidebar-right {
        width: 100%;
        position: static;
        margin-top: 30px;
      }
    }
  }
}

.blog-card {
  cursor: pointer;
}
</style>
