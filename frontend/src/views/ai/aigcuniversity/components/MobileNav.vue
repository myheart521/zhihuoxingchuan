<template>
  <div class="mobile-nav" v-if="isMobile">
    <div class="nav-container">
      <el-button v-if="showBackButton" @click="$emit('back')" class="back-btn">
        <el-icon><ArrowLeft /></el-icon>
        <span>返回对话列表</span>
      </el-button>
      <el-button v-else @click="$emit('new')" type="primary" class="new-btn">
        <el-icon><Plus /></el-icon>
        <span>新建对话</span>
      </el-button>
      
      <div class="mobile-title" v-if="showBackButton">
        <span class="title-text">{{ title }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, onMounted, onUnmounted } from 'vue';
import { ElButton } from 'element-plus';
import { ArrowLeft, Plus } from '@element-plus/icons-vue';
import { THEME } from '../utils/theme';

const props = defineProps({
  showBackButton: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '对话详情'
  }
});

const emit = defineEmits(['back', 'new']);
const isMobile = ref(false);

// 检查是否为移动设备
const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768;
};

onMounted(() => {
  checkMobile();
  window.addEventListener('resize', checkMobile);
});

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile);
});
</script>

<style scoped lang="scss">
.mobile-nav {
  display: none;
  padding: 12px 16px;
  background-color: v-bind('THEME.colors.background.primary');
  border-bottom: 1px solid v-bind('THEME.colors.border');
  
  @media (max-width: 768px) {
    display: block;
  }
  
  .nav-container {
    display: flex;
    align-items: center;
    
    .back-btn {
      display: flex;
      align-items: center;
      gap: 4px;
      color: v-bind('THEME.colors.primary');
      border-color: transparent;
      background-color: transparent;
      padding: 8px 12px;
      
      &:hover {
        background-color: v-bind('THEME.colors.light');
      }
      
      .el-icon {
        font-size: 16px;
      }
    }
    
    .new-btn {
      display: flex;
      align-items: center;
      gap: 4px;
      background-color: v-bind('THEME.colors.primary');
      border-color: v-bind('THEME.colors.primary');
      padding: 8px 16px;
      
      &:hover {
        background-color: v-bind('THEME.colors.secondary');
        border-color: v-bind('THEME.colors.secondary');
      }
      
      .el-icon {
        font-size: 16px;
      }
    }
    
    .mobile-title {
      flex: 1;
      text-align: center;
      margin-right: 60px; // 平衡左侧按钮宽度
      
      .title-text {
        font-size: 16px;
        font-weight: 500;
        color: v-bind('THEME.colors.text.primary');
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}
</style> 