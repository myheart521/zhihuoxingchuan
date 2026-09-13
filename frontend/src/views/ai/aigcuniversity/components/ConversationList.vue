<template>
  <div class="conversation-list">
    <div class="list-header">
      <div class="header-main">
        <el-button type="primary" class="new-chat-btn" @click="createNewConversation">
          <el-icon><Plus /></el-icon>新建对话
        </el-button>
        
        <!-- 添加折叠按钮 (仅在移动端显示) -->
        <el-button 
          class="collapse-btn mobile-only" 
          @click="toggleCollapse"
          :icon="isCollapsed ? 'Expand' : 'Fold'"
          circle
        >
          <el-icon>
            <component :is="isCollapsed ? 'Expand' : 'Fold'" />
          </el-icon>
        </el-button>
      </div>
      
      <!-- 添加分类过滤器 -->
      <div class="filter-tabs" v-if="!isCollapsed">
        <el-radio-group v-model="activeFilter" size="small">
          <el-radio-button label="all">全部对话</el-radio-button>
          <el-radio-button label="today">今日对话</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    
    <el-scrollbar class="scrollbar" :class="{ 'collapsed': isCollapsed }">
      <div v-if="filteredConversations.length === 0" class="empty-state">
        <el-empty description="暂无对话历史" />
      </div>
      
      <div v-else class="conversation-items">
        <div
          v-for="item in filteredConversations"
          :key="item.id"
          class="conversation-item"
          :class="{ 
            'active': selectedId === item.id,
            'collapsed-item': isCollapsed 
          }"
          @click="selectConversation(item.id)"
        >
          <div class="item-content">
            <el-icon class="chat-icon"><ChatDotRound /></el-icon>
            <div class="item-info" v-if="!isCollapsed">
              <div class="item-title">{{ item.title }}</div>
              <div class="item-meta">{{ formatDate(item.updatedAt) }}</div>
            </div>
          </div>
          <div class="item-actions" v-if="!isCollapsed">
            <el-popconfirm
              title="确定要删除这个对话吗？"
              @confirm="deleteConversation(item.id)"
              width="200"
              confirm-button-type="danger"
              cancel-button-type="info"
            >
              <template #reference>
                <el-button
                  type="text"
                  size="small"
                  class="delete-btn"
                  @click.stop
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineEmits, computed } from 'vue';
import { Plus, Delete, ChatDotRound, Expand, Fold } from '@element-plus/icons-vue';
import { ElEmpty, ElButton, ElScrollbar, ElPopconfirm, ElIcon, ElRadioGroup, ElRadioButton } from 'element-plus';
import { THEME } from '../utils/theme';
import { 
  getConversations, 
  createConversation, 
  deleteConversation as deleteConversationUtil,
  Conversation
} from '../utils/storage';

const emit = defineEmits(['select', 'create', 'delete']);
const conversations = ref<Conversation[]>([]);
const selectedId = ref<string>('');
const isCollapsed = ref(false);
const activeFilter = ref('all');

// 根据过滤条件筛选对话
const filteredConversations = computed(() => {
  if (activeFilter.value === 'all') {
    return conversations.value;
  } else if (activeFilter.value === 'today') {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return conversations.value.filter(c => {
      return new Date(c.updatedAt).getTime() >= today.getTime();
    });
  }
  return conversations.value;
});

// 切换折叠状态
const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value;
  
  // 本地存储保存折叠状态，以便下次打开时恢复
  localStorage.setItem('conversation_list_collapsed', isCollapsed.value ? 'true' : 'false');
};

// 格式化日期
const formatDate = (timestamp: number): string => {
  const date = new Date(timestamp);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  
  // 如果是今天的消息，只显示时间
  if (date.toDateString() === now.toDateString()) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
  }
  
  // 如果是昨天的消息
  const yesterday = new Date(now);
  yesterday.setDate(now.getDate() - 1);
  if (date.toDateString() === yesterday.toDateString()) {
    return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
  }
  
  // 如果是最近7天内的消息
  if (diff < 7 * 24 * 60 * 60 * 1000) {
    const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
    return days[date.getDay()];
  }
  
  // 其他情况显示完整日期
  return date.toLocaleDateString();
};

// 加载对话列表
const loadConversations = () => {
  conversations.value = getConversations();
};

// 选择对话
const selectConversation = (id: string) => {
  selectedId.value = id;
  const conversation = conversations.value.find(c => c.id === id);
  if (conversation) {
    emit('select', conversation);
  }
  
  // 在移动设备上选择对话后自动折叠侧边栏
  if (window.innerWidth <= 768) {
    isCollapsed.value = true;
  }
};

// 创建新对话
const createNewConversation = () => {
  const newConversation = createConversation();
  loadConversations();
  selectConversation(newConversation.id);
  emit('create', newConversation);
};

// 删除对话
const deleteConversation = (id: string) => {
  deleteConversationUtil(id);
  loadConversations();
  
  // 如果删除的是当前选中的对话，则选择第一个对话或清空选择
  if (id === selectedId.value) {
    if (conversations.value.length > 0) {
      selectConversation(conversations.value[0].id);
    } else {
      selectedId.value = '';
      emit('select', null);
    }
  }
  
  emit('delete', id);
};

onMounted(() => {
  loadConversations();
  
  // 从本地存储恢复折叠状态
  const savedCollapsed = localStorage.getItem('conversation_list_collapsed');
  if (savedCollapsed) {
    isCollapsed.value = savedCollapsed === 'true';
  } else {
    // 默认在移动设备上折叠
    isCollapsed.value = window.innerWidth <= 768;
  }
  
  // 如果有对话，默认选中第一个
  if (conversations.value.length > 0) {
    selectConversation(conversations.value[0].id);
  } else {
    // 如果没有对话，创建一个新对话
    createNewConversation();
  }
});
</script>

<style scoped lang="scss">
.conversation-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  
  .list-header {
    padding: 16px;
    border-bottom: 1px solid v-bind('THEME.colors.border');
    
    .header-main {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .new-chat-btn {
        flex: 1;
        height: 40px;
        border-radius: v-bind('THEME.radius.medium');
        background-color: v-bind('THEME.colors.primary');
        border-color: v-bind('THEME.colors.primary');
        
        &:hover, &:focus {
          background-color: v-bind('THEME.colors.secondary');
          border-color: v-bind('THEME.colors.secondary');
        }
        
        .el-icon {
          margin-right: 4px;
        }
      }
      
      .collapse-btn {
        display: none;
        
        @media (max-width: 768px) {
          display: flex;
        }
      }
    }
    
    .filter-tabs {
      margin-top: 16px;
      display: flex;
      justify-content: center;
      
      .el-radio-group {
        width: 100%;
        display: flex;
        
        .el-radio-button {
          flex: 1;
          
          :deep(.el-radio-button__inner) {
            width: 100%;
          }
        }
      }
    }
  }
  
  .scrollbar {
    flex: 1;
    overflow-y: auto;
    transition: width v-bind('THEME.transitions.normal');
    
    &.collapsed {
      .conversation-items {
        padding: 8px 4px;
      }
    }
  }
  
  .empty-state {
    padding: 40px 0;
  }
  
  .conversation-items {
    padding: 0 8px;
    transition: padding v-bind('THEME.transitions.normal');
  }
  
  .conversation-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px;
    border-radius: v-bind('THEME.radius.medium');
    margin-bottom: 4px;
    cursor: pointer;
    transition: all v-bind('THEME.transitions.fast');
    
    &.collapsed-item {
      padding: 12px 8px;
      justify-content: center;
      
      .chat-icon {
        margin-right: 0;
        font-size: 20px;
      }
    }
    
    &:hover {
      background-color: v-bind('THEME.colors.background.tertiary');
      
      .item-actions {
        opacity: 1;
      }
    }
    
    &.active {
      background-color: v-bind('THEME.colors.light');
      
      .chat-icon {
        color: v-bind('THEME.colors.primary');
      }
      
      .item-title {
        color: v-bind('THEME.colors.primary');
        font-weight: 500;
      }
    }
    
    .item-content {
      display: flex;
      align-items: center;
      flex: 1;
      min-width: 0;
      
      .chat-icon {
        font-size: 18px;
        margin-right: 12px;
        color: v-bind('THEME.colors.text.secondary');
        transition: margin v-bind('THEME.transitions.normal');
      }
      
      .item-info {
        flex: 1;
        min-width: 0;
        
        .item-title {
          font-size: 14px;
          margin-bottom: 4px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          color: v-bind('THEME.colors.text.primary');
        }
        
        .item-meta {
          font-size: 12px;
          color: v-bind('THEME.colors.text.light');
        }
      }
    }
    
    .item-actions {
      opacity: 0;
      transition: opacity v-bind('THEME.transitions.fast');
      
      .delete-btn {
        color: v-bind('THEME.colors.text.light');
        
        &:hover {
          color: v-bind('THEME.colors.error');
        }
      }
    }
  }
}

// 折叠时的样式
.conversation-list.collapsed {
  width: 60px;
  
  .list-header {
    padding: 16px 8px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .mobile-only {
    display: block;
  }
}
</style> 