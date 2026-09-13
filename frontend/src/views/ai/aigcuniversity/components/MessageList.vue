<template>
  <div class="message-list" ref="messageContainer">
    <div v-if="!conversation" class="empty-state">
      <div class="welcome-container">
        <img src="https://example.invalid/resource" alt="Welcome" class="welcome-img" />
        <h2 class="welcome-title">欢迎使用AI对话助手</h2>
        <p class="welcome-desc">开始一段新的对话，探索AI的无限可能</p>
      </div>
    </div>
    
    <template v-else>
      <div class="message-container">
        <div 
          v-for="message in conversation.messages" 
          :key="message.id" 
          class="message" 
          :class="message.role"
        >
          <div class="message-avatar">
            <el-avatar 
              :size="40" 
              :src="message.role === 'user' ? userAvatar : botAvatar"
              :class="message.role"
            />
          </div>
          <div class="message-content">
            <div class="message-role">{{ message.role === 'user' ? '我' : 'AI助手' }}</div>
            <div class="message-text" v-html="formatMessage(message.content)"></div>
            <div class="message-time">{{ formatTime(message.timestamp) }}</div>
          </div>
        </div>
        
        <div v-if="isLoading" class="message assistant loading">
          <div class="message-avatar">
            <el-avatar :size="40" :src="botAvatar" class="assistant" />
          </div>
          <div class="message-content">
            <div class="message-role">AI助手</div>
            <div class="loading-dots">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, nextTick, defineProps, onMounted } from 'vue';
import { ElEmpty, ElAvatar } from 'element-plus';
import { Conversation } from '../utils/storage';
import { THEME } from '../utils/theme';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import {useUserStore} from "@/store/modules/user";
import aiAvatar from "@/assets/ai/aigc-doudou-default.png"

const props = defineProps<{
  conversation: Conversation | null;
  isLoading: boolean;
}>();

const messageContainer = ref<HTMLElement | null>(null);
const userAvatar = useUserStore().user.avatar;
const botAvatar = aiAvatar;

// 配置Markdown渲染器
const md = new MarkdownIt({
  html: false,
  linkify: true,
  typographer: true,
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return '<pre class="hljs"><code>' +
               hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
               '</code></pre>';
      } catch (__) {}
    }
    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>';
  }
});

// 格式化消息内容（支持Markdown）
const formatMessage = (content: string): string => {
  return md.render(content);
};

// 格式化时间
const formatTime = (timestamp: number): string => {
  const date = new Date(timestamp);
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight;
    }
  });
};

// 监听消息变化，自动滚动到底部
watch(
  () => props.conversation?.messages,
  () => {
    scrollToBottom();
  },
  { deep: true }
);

// 监听加载状态变化
watch(
  () => props.isLoading,
  () => {
    scrollToBottom();
  }
);

onMounted(() => {
  scrollToBottom();
});
</script>

<style scoped lang="scss">
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: v-bind('THEME.colors.background.secondary');
  
  .empty-state {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .welcome-container {
      text-align: center;
      max-width: 500px;
      
      .welcome-img {
        width: 120px;
        height: 120px;
        margin-bottom: 24px;
      }
      
      .welcome-title {
        font-size: 24px;
        font-weight: 600;
        color: v-bind('THEME.colors.text.primary');
        margin-bottom: 12px;
      }
      
      .welcome-desc {
        font-size: 16px;
        color: v-bind('THEME.colors.text.secondary');
        line-height: 1.6;
      }
    }
  }
  
  .message-container {
    max-width: 900px;
    margin: 0 auto;
  }
  
  .message {
    display: flex;
    margin-bottom: 24px;
    
    &.user {
      .message-content {
        background-color: v-bind('THEME.colors.background.primary');
        border-radius: v-bind('THEME.radius.large');
        box-shadow: v-bind('THEME.shadows.small');
      }
      
      .message-role {
        color: v-bind('THEME.colors.primary');
      }
    }
    
    &.assistant {
      .message-content {
        background-color: v-bind('THEME.colors.light');
        border-radius: v-bind('THEME.radius.large');
      }
      
      .message-role {
        color: v-bind('THEME.colors.success');
      }
    }
    
    &.loading {
      .loading-dots {
        display: flex;
        align-items: center;
        height: 24px;
        padding: 0 8px;
        
        span {
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background-color: v-bind('THEME.colors.primary');
          margin: 0 3px;
          animation: loading 1.4s infinite ease-in-out both;
          
          &:nth-child(1) {
            animation-delay: -0.32s;
          }
          
          &:nth-child(2) {
            animation-delay: -0.16s;
          }
        }
      }
    }
    
    .message-avatar {
      margin-right: 16px;
      flex-shrink: 0;
      
      .el-avatar {
        border: 2px solid v-bind('THEME.colors.background.primary');
        box-shadow: v-bind('THEME.shadows.small');
        
        &.user {
          background-color: v-bind('THEME.colors.primary');
        }
        
        &.assistant {
          background-color: v-bind('THEME.colors.success');
        }
      }
    }
    
    .message-content {
      padding: 16px 20px;
      max-width: 85%;
      
      .message-role {
        font-size: 14px;
        font-weight: 500;
        margin-bottom: 8px;
      }
      
      .message-text {
        font-size: 15px;
        line-height: 1.6;
        color: v-bind('THEME.colors.text.primary');
        
        :deep(pre) {
          background-color: v-bind('THEME.colors.background.tertiary');
          padding: 16px;
          border-radius: v-bind('THEME.radius.medium');
          overflow-x: auto;
          margin: 16px 0;
          font-family: 'Fira Code', Consolas, Monaco, 'Andale Mono', monospace;
        }
        
        :deep(code) {
          background-color: v-bind('THEME.colors.background.tertiary');
          padding: 2px 6px;
          border-radius: v-bind('THEME.radius.small');
          font-family: 'Fira Code', Consolas, Monaco, 'Andale Mono', monospace;
          font-size: 14px;
        }
        
        :deep(p) {
          margin: 12px 0;
        }
        
        :deep(ul), :deep(ol) {
          padding-left: 24px;
          margin: 12px 0;
        }
        
        :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
          margin-top: 24px;
          margin-bottom: 16px;
          font-weight: 600;
          line-height: 1.25;
        }
        
        :deep(h1) {
          font-size: 2em;
        }
        
        :deep(h2) {
          font-size: 1.5em;
        }
        
        :deep(h3) {
          font-size: 1.25em;
        }
        
        :deep(blockquote) {
          padding: 0 16px;
          color: v-bind('THEME.colors.text.secondary');
          border-left: 4px solid v-bind('THEME.colors.border');
          margin: 16px 0;
        }
        
        :deep(table) {
          border-collapse: collapse;
          width: 100%;
          margin: 16px 0;
          
          th, td {
            border: 1px solid v-bind('THEME.colors.border');
            padding: 8px 12px;
            text-align: left;
          }
          
          th {
            background-color: v-bind('THEME.colors.background.tertiary');
          }
        }
      }
      
      .message-time {
        font-size: 12px;
        color: v-bind('THEME.colors.text.light');
        margin-top: 8px;
        text-align: right;
      }
    }
  }
}

@keyframes loading {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

@media (max-width: 768px) {
  .message-list {
    padding: 16px 12px;
    
    .message {
      .message-content {
        max-width: 80%;
      }
    }
  }
}
</style> 
