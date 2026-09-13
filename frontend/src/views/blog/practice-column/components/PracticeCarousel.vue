<template>
  <div class="carousel-container">
    <div class="indicator" :style="{ width: `${progress}%` }"></div>
    
    <div class="carousel-wrapper" ref="carouselRef">
      <div 
        v-for="(item, index) in carouselData" 
        :key="item.id"
        class="card"
        :class="{ active: index === activeIndex }"
        :style="getCardStyle(index)"
      >
        <img :src="item.frontCover" :alt="item.title" class="card-image" />
        <div class="card-content">
          <div class="content-start"></div>
          <div class="content-title">{{ item.title }}</div>
          <div class="content-summary">{{ item.summary }}</div>
        </div>
      </div>
    </div>

    <div class="details" :class="{ even: detailsEven }">
      <div class="title-box">
        <div class="title">{{ currentItem?.title || '' }}</div>
      </div>
<!--      <div class="desc">{{ currentItem?.summary || '' }}</div>-->
      <div class="cta">
        <button class="bookmark" @click="handleBookmark">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path fill-rule="evenodd" d="M6.32 2.577a49.255 49.255 0 0111.36 0c1.497.174 2.57 1.46 2.57 2.93V21a.75.75 0 01-1.085.67L12 18.089l-7.165 3.583A.75.75 0 013.75 21V5.507c0-1.47 1.073-2.756 2.57-2.93z" clip-rule="evenodd" />
          </svg>
        </button>
        <button class="discover" @click="handleDiscover">查看详情</button>
      </div>
    </div>

    <div class="pagination">
      <div class="arrow arrow-left" @click="prevSlide">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
        </svg>
      </div>
      <div class="arrow arrow-right" @click="nextSlide">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
        </svg>
      </div>
      <div class="progress-sub-container">
        <div class="progress-sub-background">
          <div class="progress-sub-foreground" :style="{ width: `${progress}%` }"></div>
        </div>
      </div>
      <div class="slide-numbers">
        <div 
          v-for="(item, index) in carouselData" 
          :key="index"
          class="slide-number-item"
          :class="{ active: index === activeIndex }"
          :style="{ transform: `translateY(${(index - activeIndex) * 50}px)` }"
        >
          {{ index + 1 }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { PracticeColumnApi } from '@/api/blog/practice-column'
import router from "@/router";

defineOptions({
  name: 'PracticeCarousel'
})

// 轮播图数据接口
interface CarouselItem {
  id: number
  title: string
  summary: string
  frontCover: string
}

// 数据状态
const carouselData = ref<CarouselItem[]>([])
const activeIndex = ref(0)
const detailsEven = ref(true)
const autoPlayTimer = ref<ReturnType<typeof setInterval> | null>(null)
const carouselRef = ref<HTMLElement>()

// 新增：卡片布局相关常量
const cardWidth = 260 // 单张卡片宽度
const gap = -50         // 卡片间距（最小化）
const sideStart = 550 // 第一张等待卡片距离左侧的偏移（更紧）

// 计算属性
const currentItem = computed(() => carouselData.value[activeIndex.value])
const progress = computed(() => carouselData.value.length > 0 ? ((activeIndex.value + 1) / carouselData.value.length) * 100 : 0)

// 获取轮播图数据
const getCarouselData = async () => {
  try {
    const data = await PracticeColumnApi.getPracticeCarousel()
    if (data && Array.isArray(data)) {
      carouselData.value = data
    }
  } catch (error) {
    console.error('获取轮播图数据失败:', error)
  }
}

// 获取卡片样式
const getCardStyle = (index: number) => {
  const len = carouselData.value.length
  if (len === 0) return {}

  // 归一化偏移：始终得到 0-(len-1) 之间的正数
  const rawOffset = index - activeIndex.value
  const offset = (rawOffset + len) % len // 0 表示当前展示，1 表示第一张等待…

  if (offset === 0) {
    // 活动卡片
    return {
      transform: 'translateX(0) scale(1)',
      zIndex: 20,
      opacity: 1
    }
  }

  // 统一将所有等待卡片排在右侧依次展开
  return {
    transform: `translateX(${sideStart + (offset - 1) * (cardWidth + gap)}px) scale(0.8)`,
    zIndex: 30 - offset,
    opacity: 0.9
  }
}

// 下一张
const nextSlide = () => {
  if (carouselData.value.length === 0) return
  activeIndex.value = (activeIndex.value + 1) % carouselData.value.length
  detailsEven.value = !detailsEven.value
}

// 上一张
const prevSlide = () => {
  if (carouselData.value.length === 0) return
  activeIndex.value = activeIndex.value === 0 ? carouselData.value.length - 1 : activeIndex.value - 1
  detailsEven.value = !detailsEven.value
}

// 自动播放
const startAutoPlay = () => {
  autoPlayTimer.value = setInterval(() => {
    nextSlide()
  }, 5000)
}

const stopAutoPlay = () => {
  if (autoPlayTimer.value) {
    clearInterval(autoPlayTimer.value)
    autoPlayTimer.value = null
  }
}

// 事件处理
const handleBookmark = () => {
  console.log('收藏:', currentItem.value)
}

const handleDiscover = () => {
  console.log('查看详情:', currentItem.value)
  // 这里可以跳转到详情页
  router.push("/blog/detail/"+currentItem.value.id)
}

// 生命周期
onMounted(() => {
  getCarouselData()
  startAutoPlay()
})

onUnmounted(() => {
  stopAutoPlay()
})
</script>

<style lang="scss" scoped>
$primary-color: #c52121;
$text-color: #FFFFFFDD;

.carousel-container {
  position: relative;
  width: 100%;
  height: 500px;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  border-radius: 12px;
  overflow: hidden;
  color: $text-color;
  font-family: "Inter", sans-serif;
}

.indicator {
  position: absolute;
  top: 0;
  left: 0;
  height: 4px;
  background-color: $primary-color;
  z-index: 60;
  transition: width 0.3s ease;
}

.carousel-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
}

.card {
  position: absolute;
  top: 120px;
  left: 0;
  width: 250px;
  height: 300px;
  background-size: cover;
  background-position: center;
  border-radius: 10px;
  box-shadow: 6px 6px 20px rgba(0, 0, 0, 0.4);
  transition: all 0.6s cubic-bezier(0.23, 1, 0.32, 1);
  transform-origin: center center;
  overflow: hidden;

  &.active {
    top: 0;
    width: 100%;
    height: 100%;
    border-radius: 0;
    left: 0;
    transform: translateX(0) scale(1) !important;
  }

  .card-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }

  .card-content {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 120px; /* 减小玻璃背景高度 */
    padding: 16px;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;

    /* 玻璃质感背景 */
    background: rgba(0, 0, 0, 0.35);
    backdrop-filter: blur(8px);
    -webkit-backdrop-filter: blur(8px);
    color: white;
    transition: backdrop-filter 0.4s ease, background 0.4s ease;
   
    .content-start {
      width: 30px;
      height: 4px;
      background-color: $text-color;
      border-radius: 2px;
      margin-bottom: 8px;
    }

    .content-title {
      font-size: 18px;
      font-weight: 600;
      margin-bottom: 6px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .content-summary {
      font-size: 14px;
      opacity: 0.9;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      text-overflow: ellipsis;
      line-height: 1.4;
    }
  }

  /* 动画控制：非激活卡片保持隐藏状态 */
  .card:not(.active) .card-content > * {
    opacity: 0;
    transform: translateY(15px);
  }

  /* 当卡片成为活动卡片时，文字淡入并上移 */
  .card.active .card-content > * {
    opacity: 1;
    transform: translateY(0);
    transition: opacity 0.4s ease, transform 0.4s ease;
  }
}

.details {
  position: absolute;
  top: 30%;
  left: 20px;
  transform: translateY(-50%);
  z-index: 30;
  max-width: 250px;

  /* 玻璃背景 */
  background: rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  padding: 20px 30px;
  border-radius: 12px;

  .title-box {
    margin-bottom: 16px;
    
    .title {
      font-size: 24px;
      font-weight: 700;
      font-family: "Oswald", sans-serif;
      line-height: 1.1;
      display: -webkit-box;
      -webkit-line-clamp: 8;
      -webkit-box-orient: vertical;
      overflow: hidden;
      text-overflow: ellipsis;

      &:before {
        content: "";
        display: block;
        width: 30px;
        height: 4px;
        background-color: $primary-color;
        border-radius: 2px;
        margin-bottom: 16px;
      }

    }
  }

  .desc {
    font-size: 16px;
    line-height: 1.6;
    opacity: 0.9;
    margin-bottom: 24px;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .cta {
    display: flex;
    align-items: center;
    gap: 16px;

    .bookmark {
      width: 44px;
      height: 44px;
      border: none;
      background-color: $primary-color;
      border-radius: 50%;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        transform: scale(1.1);
        box-shadow: 0 4px 12px rgba(197, 33, 33, 0.4);
      }

      svg {
        width: 20px;
        height: 20px;
      }
    }

    .discover {
      padding: 12px 24px;
      border: 2px solid white;
      background: transparent;
      color: white;
      border-radius: 25px;
      font-size: 14px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      text-transform: uppercase;
      letter-spacing: 0.5px;

      &:hover {
        background: white;
        color: #1a1a1a;
      }
    }
  }

  /* 激活 details 时文字动画 */
  .details.even, .details:not(.even) {
    /* 同层优先级已设置 z-index; 使用额外类名 active-details 动画 */
  }

  /* 当 details 区域在当前卡片时（通过父组件设置类 active-detail） */
  .details-active .title-box,
  .details-active .desc,
  .details-active .cta {
    opacity: 1;
    transform: translateY(0);
  }
}

.pagination {
  position: absolute;
  bottom: 60px;
  right: 60px;
  display: flex;
  align-items: center;
  gap: 20px;
  z-index: 40;

  .arrow {
    width: 50px;
    height: 50px;
    border: 2px solid rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      border-color: rgba(255, 255, 255, 0.6);
      background: rgba(255, 255, 255, 0.1);
    }

    svg {
      width: 24px;
      height: 24px;
      color: rgba(255, 255, 255, 0.7);
    }
  }

  .progress-sub-container {
    width: 300px;
    height: 50px;
    display: flex;
    align-items: center;

    .progress-sub-background {
      width: 100%;
      height: 3px;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 2px;

      .progress-sub-foreground {
        height: 100%;
        background: $primary-color;
        border-radius: 2px;
        transition: width 0.3s ease;
      }
    }
  }

  .slide-numbers {
    width: 50px;
    height: 50px;
    position: relative;
    overflow: hidden;

    .slide-number-item {
      position: absolute;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      font-weight: bold;
      color: white;
      transition: transform 0.6s cubic-bezier(0.23, 1, 0.32, 1);

      &.active {
        color: $primary-color;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .carousel-container {
    height: 300px;
  }

  .details {
    left: 20px;
    right: 20px;
    max-width: calc(100% - 40px);

    .title-box .title {
      font-size: 20px;
    }

    .desc {
      font-size: 14px;
    }
  }

  .pagination {
    left: 20px;
    
    .progress-sub-container {
      width: 200px;
    }
  }

  .card .card-content {
    padding: 15px;

    .content-title {
      font-size: 16px;
    }

    .content-summary {
      font-size: 12px;
    }
  }
}
</style> 

