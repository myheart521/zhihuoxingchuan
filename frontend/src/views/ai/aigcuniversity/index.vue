<script setup lang="ts">
import { ref, onMounted } from 'vue';
import ConversationList from './components/ConversationList.vue';
import MessageList from './components/MessageList.vue';
import MessageInput from './components/MessageInput.vue';
import MobileNav from './components/MobileNav.vue';
import { callAiModel } from './utils/aiApi';
import { THEME } from './utils/theme';
import { 
  Conversation, 
  Message, 
  addMessageToConversation, 
  updateConversation,
  createConversation
} from './utils/storage';

import {useUserStore} from "@/store/modules/user";


// 当前选中的对话
const currentConversation = ref<Conversation | null>(null);
// 加载状态
const isLoading = ref(false);
// 移动端视图状态
const showSidebar = ref(true);

// 检查是否为移动设备
const isMobile = () => window.innerWidth <= 768;

// 处理选择对话
const handleSelectConversation = (conversation: Conversation | null) => {
  currentConversation.value = conversation;
  
  // 在移动设备上，选择对话后隐藏侧边栏
  if (isMobile()) {
    showSidebar.value = false;
  }
};

// 处理新建对话
const handleCreateConversation = () => {
  const newConversation = createConversation();
  currentConversation.value = newConversation;
  
  // 在移动设备上，创建对话后隐藏侧边栏
  if (isMobile()) {
    showSidebar.value = false;
  }
};

// 返回对话列表
const handleBackToList = () => {
  showSidebar.value = true;
};

// 处理发送消息
const handleSendMessage = async (content: string) => {
  if (!currentConversation.value) return;
  
  try {
    isLoading.value = true;
    
    // 添加用户消息
    const userMessage = addMessageToConversation(currentConversation.value.id, {
      role: 'user',
      content
    });
    
    // 更新当前对话
    if (currentConversation.value) {
      currentConversation.value.messages.push(userMessage);
    }
    
    // 准备发送给API的消息
    const apiMessages = currentConversation.value.messages.map(msg => ({
      role: msg.role,
      content: msg.content
    }));
    
    // 调用AI模型
    const response = await callAiModel(apiMessages);
    
    // 添加AI回复
    const assistantMessage = addMessageToConversation(currentConversation.value.id, {
      role: 'assistant',
      content: response
    });
    
    // 更新当前对话
    if (currentConversation.value) {
      currentConversation.value.messages.push(assistantMessage);
      updateConversation(currentConversation.value);
    }
    
  } catch (error) {
    console.error('发送消息失败:', error);
    // 添加错误消息
    if (currentConversation.value) {
      addMessageToConversation(currentConversation.value.id, {
        role: 'assistant',
        content: '抱歉，发生了错误，请稍后再试。'
      });
    }
  } finally {
    isLoading.value = false;
  }
};

// 监听窗口大小变化
onMounted(() => {
  window.addEventListener('resize', () => {
    if (!isMobile()) {
      showSidebar.value = true;
    }
  });
});
</script>

<template>
  <div class="ai-university-container">
    <div class="chat-layout">
      <div class="sidebar" :class="{ 'sidebar-hidden': !showSidebar }">
        <div class="sidebar-header">
          <div class="logo">
            <img src="@/assets/ai/aigc-doudou-default.png" alt="Logo" class="logo-img" />
            <span class="logo-text">预防诈骗AIGC宣传者</span>
          </div>
        </div>
        
        <ConversationList 
          @select="handleSelectConversation"
          @create="handleCreateConversation"
        />
      </div>
      
      <div class="main-content" :class="{ 'main-content-full': !showSidebar }">
        <MobileNav 
          :show-back-button="!showSidebar" 
          @back="handleBackToList"
          @new="handleCreateConversation"
        />
        
        <div class="chat-header" v-if="currentConversation">
          <h2 class="chat-title">{{ currentConversation.title }}</h2>
        </div>
        
        <MessageList 
          :conversation="currentConversation" 
          :isLoading="isLoading" 
        />
        
        <MessageInput 
          :loading="isLoading" 
          :disabled="!currentConversation"
          @send="handleSendMessage" 
        />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.ai-university-container {
  height: 81vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  background-color: v-bind('THEME.colors.background.secondary');
  overflow: hidden;
  
  .chat-layout {
    display: flex;
    height: 100%;
    width: 100%;
    position: relative;
    overflow: hidden;
    
    .sidebar {
      width: 280px;
      height: 100%;
      background-color: v-bind('THEME.colors.background.primary');
      border-right: 1px solid v-bind('THEME.colors.border');
      display: flex;
      flex-direction: column;
      transition: transform v-bind('THEME.transitions.normal');
      box-shadow: v-bind('THEME.shadows.small');
      z-index: 10;
      
      .sidebar-header {
        padding: 16px;
        border-bottom: 1px solid v-bind('THEME.colors.border');
        
        .logo {
          display: flex;
          align-items: center;
          
          .logo-img {
            width: 32px;
            height: 32px;
            margin-right: 8px;
          }
          
          .logo-text {
            font-size: 18px;
            font-weight: 600;
            color: v-bind('THEME.colors.primary');
          }
        }
      }
    }
    
    .main-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      background-color: v-bind('THEME.colors.background.primary');
      transition: margin-left v-bind('THEME.transitions.normal');
      
      .chat-header {
        padding: 16px 24px;
        border-bottom: 1px solid v-bind('THEME.colors.border');
        background-color: v-bind('THEME.colors.background.primary');
        
        .chat-title {
          margin: 0;
          font-size: 18px;
          font-weight: 500;
          color: v-bind('THEME.colors.text.primary');
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .ai-university-container {
    .chat-layout {
      .sidebar {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: 20;
        transform: translateX(0);
        
        &.sidebar-hidden {
          transform: translateX(-100%);
        }
      }
      
      .main-content {
        margin-left: 0;
        width: 100%;
        
        &.main-content-full {
          margin-left: 0;
        }
      }
    }
  }
}

@media (min-width: 769px) {
  .ai-university-container {
    .chat-layout {
      .main-content {
        margin-left: 0;
        
        &.main-content-full {
          margin-left: 0;
        }
      }
    }
  }
}
</style>
