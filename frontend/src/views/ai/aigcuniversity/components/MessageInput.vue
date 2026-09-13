<template>
  <div class="message-input">
    <div class="input-container">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="3"
        :placeholder="placeholder"
        resize="none"
        :disabled="disabled"
        @keydown="handleKeyDown"
        class="chat-textarea"
        :class="{ 'is-loading': loading }"
        autosize
        ref="inputRef"
      />
      
      <div class="input-actions">
        <div class="input-tools">
          <el-tooltip content="清空输入" placement="top" v-if="inputMessage">
            <el-button class="tool-btn" @click="clearInput" circle>
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </div>
        
        <el-button
          type="primary"
          :loading="loading"
          :disabled="!inputMessage.trim() || disabled"
          @click="sendMessage"
          class="send-btn"
        >
          <el-icon v-if="!loading"><Position /></el-icon>
          <span>{{ loading ? '发送中...' : '发送' }}</span>
        </el-button>
      </div>
    </div>
    
    <div class="input-footer">
      <div class="shortcuts">
        <span class="shortcut-hint">Enter 发送 / Shift+Enter 换行</span>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, nextTick } from 'vue';
import { ElInput, ElButton, ElTooltip } from 'element-plus';
import { Picture, Delete, Position } from '@element-plus/icons-vue';
import { THEME } from '../utils/theme';

const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: '输入消息，按Enter发送，Shift+Enter换行...'
  }
});

const emit = defineEmits(['send']);
const inputMessage = ref('');
const inputRef = ref<any>(null);

// 处理键盘事件
const handleKeyDown = (e: KeyboardEvent) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
};

// 发送消息
const sendMessage = () => {
  const message = inputMessage.value.trim();
  if (!message || props.loading || props.disabled) return;
  
  emit('send', message);
  inputMessage.value = '';
  
  // 重新聚焦输入框
  nextTick(() => {
    if (inputRef.value) {
      inputRef.value.focus();
    }
  });
};

// 清空输入
const clearInput = () => {
  inputMessage.value = '';
  
  // 重新聚焦输入框
  nextTick(() => {
    if (inputRef.value) {
      inputRef.value.focus();
    }
  });
};
</script>

<style scoped lang="scss">
.message-input {
  padding: 16px 24px;
  background-color: v-bind('THEME.colors.background.primary');
  border-top: 1px solid v-bind('THEME.colors.border');
  
  .input-container {
    background-color: v-bind('THEME.colors.background.secondary');
    border-radius: v-bind('THEME.radius.large');
    padding: 12px 16px;
    box-shadow: v-bind('THEME.shadows.small');
    
    .chat-textarea {
      :deep(.el-textarea__inner) {
        background-color: transparent;
        border: none;
        padding: 8px 0;
        font-size: 15px;
        line-height: 1.6;
        color: v-bind('THEME.colors.text.primary');
        box-shadow: none !important;
        
        &:focus {
          outline: none;
          box-shadow: none;
        }
        
        &::placeholder {
          color: v-bind('THEME.colors.text.light');
        }
      }
      
      &.is-loading {
        :deep(.el-textarea__inner) {
          opacity: 0.7;
        }
      }
    }
    
    .input-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 8px;
      
      .input-tools {
        display: flex;
        gap: 8px;
        
        .tool-btn {
          color: v-bind('THEME.colors.text.secondary');
          
          &:hover {
            color: v-bind('THEME.colors.primary');
            background-color: v-bind('THEME.colors.light');
          }
        }
      }
      
      .send-btn {
        background-color: v-bind('THEME.colors.primary');
        border-color: v-bind('THEME.colors.primary');
        padding: 8px 16px;
        border-radius: v-bind('THEME.radius.medium');
        display: flex;
        align-items: center;
        gap: 4px;
        
        &:hover, &:focus:not(:disabled) {
          background-color: v-bind('THEME.colors.secondary');
          border-color: v-bind('THEME.colors.secondary');
        }
        
        .el-icon {
          font-size: 16px;
        }
      }
    }
  }
  
  .input-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 8px;
    padding: 0 4px;
    
    .shortcuts {
      .shortcut-hint {
        font-size: 12px;
        color: v-bind('THEME.colors.text.light');
      }
    }
    
    .powered-by {
      font-size: 12px;
      color: v-bind('THEME.colors.text.light');
      
      .provider {
        color: v-bind('THEME.colors.primary');
        font-weight: 500;
      }
    }
  }
}

@media (max-width: 768px) {
  .message-input {
    padding: 12px;
    
    .input-footer {
      flex-direction: column;
      align-items: flex-start;
      gap: 4px;
    }
  }
}
</style> 
