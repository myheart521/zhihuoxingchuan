<script setup lang="ts">
import { ref } from 'vue'
import SearchBox from './components/SearchBox.vue'
import PracticeCarousel from './components/PracticeCarousel.vue'
import PracticeArticleList from './components/PracticeArticleList.vue'

// 查询参数
const queryParams = ref({
  pageNo: 1,
  pageSize: 16,
  keyword: '',
  categoryId: undefined as number | undefined,
  tagId: undefined as number | undefined
})

// 处理搜索事件
const handleSearch = (params: any) => {
  queryParams.value = { ...params }
  console.log('搜索参数:', queryParams.value)
  // 这里可以调用获取博客列表的API
  // getBlogList()
}

// 处理重置事件
const handleReset = (params: any) => {
  queryParams.value = { ...params }
  console.log('重置参数:', queryParams.value)
  // 这里可以调用获取博客列表的API
  // getBlogList()
}
</script>

<template>
  <div class="social-practice-container">
    <!--轮播图区域-->
    <div class="carousel-section">
      <PracticeCarousel />
    </div>
    
    <!-- 这里是按照分类以及标签以及搜索框区域-->
    <div class="search-section">
      <SearchBox @search="handleSearch" @reset="handleReset" />
    </div>
    
    <!--下面是大家发的文章-->
    <div class="articles-section">
      <PracticeArticleList :query-params="queryParams" />
    </div>
  </div>
</template>

<style scoped lang="scss">
.social-practice-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;

  .carousel-section {
    margin-bottom: 30px;
  }

  .search-section {
    margin-bottom: 30px;
  }

  .articles-section {
    min-height: 400px;
    background-color: #fff;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 16px;
  }
}

@media (max-width: 768px) {
  .social-practice-container {
    padding: 10px;
  }
}
</style>
