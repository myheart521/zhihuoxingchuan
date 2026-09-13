<script setup lang="ts">
import { ref, nextTick, watch, onMounted, onUnmounted, computed } from 'vue'
import { 
  Refresh, 
  Search,
  Avatar,
  ArrowLeft
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/modules/user'
import { getRefreshToken } from "@/utils/auth"
import { useWebSocket } from '@vueuse/core'
import { formatDate, formatPast } from '@/utils/formatTime'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ChatMessagesGroupApi } from '@/api/blog/chatmessagesgroup'


// 用户信息
const userStore = useUserStore()
const userId = ref(userStore.user?.id)

// 当前选中的聊天
const selectedChat = ref(0)

// 群聊类型枚举选项
const chatGroupOptions = getIntDictOptions(DICT_TYPE.BLOG_CHAT_MESSAGE_GROUP)

// 聊天联系人列表
const contacts = ref([
  {
    id: 1,
    name: '特别能战斗精神',
    avatar: 'https://example.invalid/resource',
    lastMessage: "点击开始聊天...",
    time: formatDate(new Date()),
    description: "参与讨论特别能战斗精神的群聊...",
    wsType: {
      send: 'send_fight',
      receive: 'receive_fight'
    },
    receiverGroup: chatGroupOptions[0]?.value || 0 // 群聊标识，对应后端的receiverGroup
  },
  {
    id: 2,
    name: '红色革命故事',
    avatar: 'https://example.invalid/resource',
    lastMessage: '点击开始聊天...',
    time: formatDate(new Date()),
    description: "讨论红色革命故事的群聊",
    wsType: {
      send: 'send_revolution',
      receive: 'receive_revolution'
    },
    receiverGroup: chatGroupOptions[1]?.value || 1 // 群聊标识，对应后端的receiverGroup
  }
])

// 定义消息类型接口 - 匹配后端格式
interface ChatMessage {
  id: number;
  text: string;
  fromMe: boolean;
  userId: number | string;
  userName?: string;
  userAvatar?: string;
  timestamp: number; // 存储为时间戳
}

// 定义分页查询参数
interface ChatMessagesQueryParams {
  pageNo: number;
  pageSize: number;
  receiverGroup: number;
}

// 初始化查询参数
const queryParams = ref<Record<number, ChatMessagesQueryParams>>({
  0: {
    pageNo: 1,
    pageSize: 20,
    receiverGroup: contacts.value[0].receiverGroup
  },
  1: {
    pageNo: 1,
    pageSize: 20,
    receiverGroup: contacts.value[1].receiverGroup
  }
})

// 本地存储键名
const STORAGE_KEY_PREFIX = 'chat_messages_'

// 从本地存储加载消息
const loadMessagesFromStorage = (): Record<number, ChatMessage[]> => {
  try {
    const data: Record<number, ChatMessage[]> = { 0: [], 1: [] }
    
    for (let i = 0; i < contacts.value.length; i++) {
      const storageKey = `${STORAGE_KEY_PREFIX}${i}`
      const storedMessages = localStorage.getItem(storageKey)
      
      if (storedMessages) {
        data[i] = JSON.parse(storedMessages)
      }
    }
    
    return data
  } catch (error) {
    console.error('加载本地消息失败:', error)
    return { 0: [], 1: [] }
  }
}

// 将消息保存到本地存储
const saveMessagesToStorage = (chatIndex: number, messages: ChatMessage[]) => {
  try {
    const storageKey = `${STORAGE_KEY_PREFIX}${chatIndex}`
    localStorage.setItem(storageKey, JSON.stringify(messages))
  } catch (error) {
    console.error('保存本地消息失败:', error)
  }
}

// 各联系人的聊天消息 - 从本地加载
const allMessages = ref<Record<number, ChatMessage[]>>(loadMessagesFromStorage())

// 当前显示的消息
const messages = ref<ChatMessage[]>(allMessages.value[0])

// 新消息内容
const newMessage = ref('')

// 添加动画标志
const messagesVisible = ref(true)

// 连接状态
const wsStatus = ref('CLOSED')
const isConnected = computed(() => wsStatus.value === 'OPEN')

// 历史消息加载状态
const historyLoading = ref(false)

// 是否还有更多历史消息
const hasMoreHistory = ref<Record<number, boolean>>({
  0: true, 
  1: true
})

// 配置WebSocket服务地址
const serverUrl = (import.meta.env.VITE_BASE_URL + '/infra/ws').replace('http', 'ws') + 
  '?token=' + getRefreshToken()

// 使用vueuse的WebSocket钩子
const { status, data, send, close, open } = useWebSocket(serverUrl, {
  autoReconnect: true,
  heartbeat: true,
  autoClose: false
})

// 监听WebSocket状态
watch(status, (newStatus) => {
  wsStatus.value = newStatus
  console.log('WebSocket状态变更为:', newStatus)
})

// 监听WebSocket数据
watch(data, (newData) => {
  if (!newData) return
  
  try {
    // 忽略心跳
    if (newData === 'pong') return
    
    // 解析消息
    const jsonMessage = JSON.parse(newData)
    const type = jsonMessage.type
    const content = JSON.parse(jsonMessage.content)
    
    console.log('收到消息:', type, content)
    
    if (type === 'receive_fight') {
      handleReceivedMessage(0, content)
    } else if (type === 'receive_revolution') {
      handleReceivedMessage(1, content)
    }
  } catch (error) {
    console.error('处理WebSocket消息错误:', error, newData)
  }
})

// 处理收到的消息
const handleReceivedMessage = (chatIndex: number, content: any) => {
  const isFromMe = Number(content.userId || content.senderId) === Number(userId.value)
  
  // 获取时间戳
  let timestamp: number;
  if (content.time || content.createTime) {
    // 尝试将后端的时间转换为时间戳
    if (typeof content.time === 'number' || typeof content.createTime === 'number') {
      timestamp = content.time || content.createTime;
    } else if (typeof content.time === 'string' || typeof content.createTime === 'string') {
      try {
        timestamp = new Date(content.time || content.createTime).getTime();
      } catch (e) {
        timestamp = Date.now();
      }
    } else {
      timestamp = Date.now();
    }
  } else {
    timestamp = Date.now();
  }
  
  // 创建消息对象，匹配后端字段，以时间戳存储时间
  const newMsg: ChatMessage = {
    id: content.id || Date.now(),
    text: content.text || content.content,
    fromMe: isFromMe,
    userId: content.userId || content.senderId,
    userName: content.userName || content.senderName,
    userAvatar: content.userAvatar || content.senderAvatar,
    timestamp: timestamp // 存储为时间戳
  }
  
  console.log('新消息:', newMsg)
  
  // 添加到当前会话的消息列表
  allMessages.value[chatIndex].push(newMsg)
  
  // 保存到本地存储
  saveMessagesToStorage(chatIndex, allMessages.value[chatIndex])
  
  // 更新最后一条消息
  contacts.value[chatIndex].lastMessage = content.text
  contacts.value[chatIndex].time = formatDate(new Date(timestamp), 'HH:mm')
  
  // 如果当前正在查看这个聊天，则更新显示
  if (selectedChat.value === chatIndex) {
    messages.value = [...allMessages.value[chatIndex]]
    scrollToBottom()
  }
}

// 从后端加载历史消息
const loadHistoryMessages = async (chatIndex: number, isLoadMore = false) => {
  try {
    historyLoading.value = true
    
    // 如果不是加载更多，则重置查询参数
    if (!isLoadMore) {
      queryParams.value[chatIndex].pageNo = 1
    }
    
    const params = {
      pageNo: queryParams.value[chatIndex].pageNo,
      pageSize: queryParams.value[chatIndex].pageSize,
      receiverGroup: queryParams.value[chatIndex].receiverGroup
    }
    
    console.log('查询历史消息参数:', params)
    
    const res = await ChatMessagesGroupApi.getChatMessagesGroupPage(params)
    
    if (res && res.list) {
      // 将后端消息转换为前端格式
      const historyMessages: ChatMessage[] = res.list.map((item: any) => ({
        id: item.id,
        text: item.text || item.content,
        fromMe: Number(item.senderId || item.userId) === Number(userId.value),
        userId: item.senderId || item.userId,
        userName: item.senderName || item.userName,
        userAvatar: item.senderAvatar || item.userAvatar,
        timestamp: item.createTime || new Date(item.createTime).getTime()
      }))
      
      // 检查是否还有更多数据
      hasMoreHistory.value[chatIndex] = res.total > queryParams.value[chatIndex].pageNo * queryParams.value[chatIndex].pageSize
      
      if (isLoadMore) {
        // 历史消息应该放在前面
        allMessages.value[chatIndex] = [...historyMessages.reverse(), ...allMessages.value[chatIndex]]
      } else {
        // 如果是初次加载，直接替换
        allMessages.value[chatIndex] = historyMessages.reverse()
      }
      
      // 保存到本地存储
      saveMessagesToStorage(chatIndex, allMessages.value[chatIndex])
      
      // 如果当前正在查看这个聊天，则更新显示
      if (selectedChat.value === chatIndex) {
        messages.value = [...allMessages.value[chatIndex]]
        
        // 如果不是加载更多，则滚动到底部
        if (!isLoadMore) {
          scrollToBottom()
        } else {
          // 记录当前滚动位置，加载完后还原
          preserveScrollPosition()
        }
      }
      
      // 更新联系人最后一条消息
      if (allMessages.value[chatIndex].length > 0) {
        const lastMsg = allMessages.value[chatIndex][allMessages.value[chatIndex].length - 1]
        contacts.value[chatIndex].lastMessage = lastMsg.text
        contacts.value[chatIndex].time = formatDate(new Date(lastMsg.timestamp), 'HH:mm')
      }
    }
  } catch (error) {
    console.error('加载历史消息失败:', error)
  } finally {
    historyLoading.value = false
  }
}

// 加载更多历史消息
const loadMoreHistory = async (chatIndex: number) => {
  if (historyLoading.value || !hasMoreHistory.value[chatIndex]) return
  
  queryParams.value[chatIndex].pageNo++
  await loadHistoryMessages(chatIndex, true)
}

// 记录滚动位置相关变量
const chatPanel = ref<HTMLElement | null>(null)
const lastScrollHeight = ref(0)
const lastScrollTop = ref(0)

// 保存滚动位置
const preserveScrollPosition = () => {
  if (!chatPanel.value) return
  
  lastScrollHeight.value = chatPanel.value.scrollHeight
  lastScrollTop.value = chatPanel.value.scrollTop
}

// 恢复滚动位置
const restoreScrollPosition = () => {
  if (!chatPanel.value) return
  
  const newPosition = chatPanel.value.scrollHeight - lastScrollHeight.value
  chatPanel.value.scrollTop = lastScrollTop.value + newPosition
}

// 监听聊天面板的滚动
const handleScroll = (e: Event) => {
  const target = e.target as HTMLElement
  if (target.scrollTop === 0 && !historyLoading.value) {
    // 用户滚动到顶部，加载更多历史消息
    loadMoreHistory(selectedChat.value)
  }
}

// 监听聊天对象变化，切换消息
watch(selectedChat, async (newVal) => {
  // 隐藏消息
  messagesVisible.value = false
  
  // 等待动画完成后切换消息并显示
  await nextTick()
  setTimeout(() => {
    messages.value = allMessages.value[newVal] || []
    messagesVisible.value = true
    
    // 从后端加载历史消息（如果还没有加载过）
    if (messages.value.length === 0) {
      loadHistoryMessages(newVal)
    } else {
      // 滚动到底部
      scrollToBottom()
    }
  }, 300) // 匹配CSS动画时间
})

// 添加这个变量用于控制移动端视图
const isMobileChatActive = ref(false)

// 修改选择联系人函数
const selectContact = (index: number) => {
  selectedChat.value = index
  
  // 在移动端视图下激活聊天
  if (isMobileDevice.value) {
    isMobileChatActive.value = true
  }
}

// 返回群聊列表
const backToContactList = () => {
  isMobileChatActive.value = false
}

// 发送消息
const sendMessage = () => {
  if (!newMessage.value.trim() || !isConnected.value) return
  
  const currentChat = contacts.value[selectedChat.value]
  const currentTime = Date.now()
  
  // 构建消息内容
  const messageContent = JSON.stringify({
    userId: userId.value,
    text: newMessage.value,
    time: currentTime // 发送时间戳
  })
  
  // 构建WebSocket消息
  const wsMessage = JSON.stringify({
    type: currentChat.wsType.send,
    content: messageContent
  })
  
  console.log('发送消息:', wsMessage)
  
  // 发送WebSocket消息
  send(wsMessage)
  
  // 清空输入框
  newMessage.value = ''
}

// 滚动到底部
const scrollToBottom = () => {
  setTimeout(() => {
    if (chatPanel.value) {
      chatPanel.value.scrollTop = chatPanel.value.scrollHeight
    }
  }, 100)
}

// 回车发送消息
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

// 添加一个响应式变量来存储窗口宽度
const windowWidth = ref(0)

// 判断是否为移动设备的计算属性
const isMobileDevice = computed(() => windowWidth.value <= 768)

// 修改处理窗口大小变化的函数
const handleResize = () => {
  // 更新窗口宽度
  windowWidth.value = window.innerWidth
  
  // 如果不是移动端尺寸，确保聊天视图不会被隐藏
  if (windowWidth.value > 768) {
    isMobileChatActive.value = false
  }
}

// 组件挂载时初始化WebSocket和加载历史消息
onMounted(() => {
  if (status.value !== 'OPEN') {
    open()
  }
  
  // 获取聊天面板DOM元素
  chatPanel.value = document.querySelector('.chat-panel')
  
  // 添加滚动监听
  if (chatPanel.value) {
    chatPanel.value.addEventListener('scroll', handleScroll)
  }
  
  // 加载当前选中聊天的历史消息
  loadHistoryMessages(selectedChat.value)
  
  // 初始设置窗口宽度
  windowWidth.value = window.innerWidth
  
  // 添加窗口大小变化监听
  window.addEventListener('resize', handleResize)
  // 初始检测
  handleResize()
})

// 组件卸载时关闭WebSocket连接和移除事件监听
onUnmounted(() => {
  close()
  
  // 移除滚动监听
  if (chatPanel.value) {
    chatPanel.value.removeEventListener('scroll', handleScroll)
  }
  
  // 移除窗口大小变化监听
  window.removeEventListener('resize', handleResize)
})

// 清除聊天记录
const clearChatHistory = (chatIndex: number) => {
  allMessages.value[chatIndex] = []
  if (selectedChat.value === chatIndex) {
    messages.value = []
  }
  saveMessagesToStorage(chatIndex, [])
  
  // 重新加载第一页历史消息
  queryParams.value[chatIndex].pageNo = 1
  loadHistoryMessages(chatIndex)
}

// 获取时间问候语
const getTimeGreeting = () => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  else if (hour < 9) return '早上好'
  else if (hour < 12) return '上午好'
  else if (hour < 14) return '中午好'
  else if (hour < 17) return '下午好'
  else if (hour < 19) return '傍晚好'
  else if (hour < 22) return '晚上好'
  else return '夜里好'
}

// 当前问候语
const greeting = ref(getTimeGreeting())
</script>

<template>
  <div class="chat-container">
    <div class="chat-row" :class="{'mobile-chat-active': isMobileChatActive}">
      <!-- 左侧联系人列表 -->
      <div class="chat-col-left">
        <div class="settings-tray">
          <img class="profile-image" :src="userStore.getUser.avatar" alt="Profile img"/>
          <span class="welcome-text">{{ greeting }}，{{ userStore.user?.nickname || '用户' }}</span>
        </div>
        
        <div class="search-box">
          <div class="input-wrapper">
            <el-icon><Search /></el-icon>
            <input placeholder="搜索联系人" type="text"/>
          </div>
        </div>
        
        <div class="contacts-container">
          <div 
            v-for="(contact, index) in contacts" 
            :key="contact.id"
            class="friend-drawer friend-drawer--onhover"
            :class="{'friend-drawer--active': selectedChat === index}"
            @click="selectContact(index)"
          >
            <img class="profile-image" :src="contact.avatar" :alt="contact.name"/>
            <div class="text">
              <h6>{{ contact.name }}</h6>
              <p class="text-muted">{{ contact.lastMessage }}</p>
            </div>
            <span class="time text-muted small">{{ contact.time }}</span>
          </div>
        </div>
      </div>
      
      <!-- 右侧聊天区域 -->
      <div class="chat-col-right">
        <div class="settings-tray">
          <div class="friend-drawer no-gutters friend-drawer--grey">
            <div class="back-button" v-show="isMobileDevice" @click="backToContactList">
              <el-icon><ArrowLeft /></el-icon>
              <span>返回</span>
            </div>
            
            <img
              class="profile-image"
              :src="contacts[selectedChat]?.avatar"
              :alt="contacts[selectedChat]?.name"
            />
            <div class="text">
              <h6>{{ contacts[selectedChat]?.name }}</h6>
              <p class="text-muted">{{ contacts[selectedChat]?.description || '群聊' }}</p>
            </div>
            <span class="settings-tray--right">
              <el-tag :type="isConnected ? 'success' : 'danger'">
                {{ isConnected ? '已连接' : '未连接' }}
              </el-tag>
              <el-tooltip content="清除聊天记录" placement="bottom">
                <el-icon class="tray-icon ml-2" @click="clearChatHistory(selectedChat)">
                  <Refresh />
                </el-icon>
              </el-tooltip>
            </span>
          </div>
        </div>
        
        <div class="chat-panel" ref="chatPanel">
          <!-- 加载更多历史消息提示 -->
          <div v-if="hasMoreHistory[selectedChat]" class="load-more-history" :class="{ 'loading': historyLoading }">
            <span v-if="historyLoading">加载中...</span>
            <span v-else @click="loadMoreHistory(selectedChat)">点击加载更多历史消息</span>
          </div>
          
          <transition-group name="fade-message" v-if="messagesVisible">
            <div 
              v-for="message in messages" 
              :key="message.id"
              class="message-row"
              :class="{'message-row--right': message.fromMe}"
            >
              <div class="message-avatar" v-if="!message.fromMe">
                <img v-if="message.userAvatar" :src="message.userAvatar" :alt="String(message.userId)" class="avatar-img" />
                <div v-else class="avatar-placeholder">
                  {{ message.userName ? message.userName.substr(0, 1) : String(message.userId).substr(0, 1) }}
                </div>
              </div>
              <div class="message-content">
                <div class="message-name" v-if="!message.fromMe">{{ message.userName || `用户${message.userId}` }}</div>
                <div 
                  class="chat-bubble"
                  :class="{'chat-bubble--left': !message.fromMe, 'chat-bubble--right': message.fromMe}"
                >
                  {{ message.text }}
                  <div class="message-time">
                    {{  formatPast(new Date(message.timestamp))  }}
                  </div>
                </div>
              </div>
            </div>
          </transition-group>
        </div>
        
        <div class="chat-box-tray">
          <el-icon class="tray-icon"><Avatar /></el-icon>
          <input 
            v-model="newMessage" 
            type="text" 
            placeholder="在此输入消息..."
            @keydown="handleKeydown"
            :disabled="!isConnected"
          />
          <el-button type="primary" @click="sendMessage" :disabled="!isConnected">
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
$blue: #409eff; // Element Plus 主色调
$red: #c52121; // 中国红主题色

.chat-container {
  width: 100%;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  height: 80vh;
  
  /* 波浪纹边框，更符合红色主题 */
  border: 1px solid $red;
  box-shadow: 0 5px 15px rgba($red, 0.2);
}

.chat-row {
  display: flex;
  height: 80vh;
}

.chat-col-left {
  width: 35%;
  border-right: 1px solid #eee;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
}

.contacts-container {
  flex: 1;
  overflow-y: auto;
  
  /* 隐藏滚动条但保留滚动功能 */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
  
  &::-webkit-scrollbar {
    display: none; /* Chrome, Safari, Opera */
  }
}

.chat-col-right {
  width: 65%;
  display: flex;
  flex-direction: column;
}

.profile-image {
  width: 50px;
  height: 50px;
  border-radius: 40px;
  object-fit: cover;
  border: 2px solid rgba($red, 0.3);
}

.welcome-text {
  margin-left: 12px;
  font-size: 16px;
  color: $red;
  font-weight: 500;
}

.settings-tray {
  background: #f7f7f7;
  padding: 15px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 2px solid rgba($red, 0.1);
  
  &--right {
    display: flex;
    align-items: center;
  }
  
  .tray-icon {
    font-size: 22px;
    color: $red;
    margin-left: 16px;
    cursor: pointer;
    transition: color 0.3s;
    
    &:hover {
      color: darken($red, 10%);
    }
  }
}

.search-box {
  background: #fafafa;
  padding: 10px 13px;
  
  .input-wrapper {
    background: #fff;
    border-radius: 40px;
    display: flex;
    align-items: center;
    padding: 8px 12px;
    border: 1px solid #eee;
    
    .el-icon {
      color: $red;
      margin-right: 8px;
    }
  }
  
  input {
    border: none;
    border-radius: 30px;
    width: 85%;
    padding: 5px;
    
    &::placeholder {
      color: #ccc;
      font-weight: 300;
    }
    
    &:focus {
      outline: none;
    }
  }
}

.friend-drawer {
  padding: 12px 15px;
  display: flex;
  align-items: center;
  background: #fff;
  transition: background 0.3s ease;
  border-bottom: 1px solid #f5f5f5;
  
  &--grey {
    background: #f7f7f7;
  }
  
  &--active {
    background: rgba($red, 0.1);
    
    .text h6 {
      color: $red;
      font-weight: 600;
    }
  }
  
  .text {
    color: $red;

    margin-left: 12px;
    width: 60%;
    
    h6 {
      margin-top: 3px;
      margin-bottom: 4px;
      font-size: 16px;
      font-weight: 500;
    }
    
    p {
      margin: 0;
      font-size: 13px;
      color: #999;
    }
  }
  
  .time {
    color: #999;
    font-size: 12px;
    margin-left: auto;
  }
  
  &--onhover {
    cursor: pointer;
    
    &:hover {
      background: rgba($red, 0.05);
    }
  }
}

.chat-panel {
  flex: 1;
  padding: 15px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  background-color: #f7f7f7;
  max-height: calc(80vh - 140px);
  
  // 红色主题的滚动条
  &::-webkit-scrollbar {
    width: 6px;
  }
  
  &::-webkit-scrollbar-thumb {
    background-color: rgba($red, 0.3);
    border-radius: 3px;
    
    &:hover {
      background-color: rgba($red, 0.5);
    }
  }
}

.load-more-history {
  text-align: center;
  padding: 10px;
  margin-bottom: 10px;
  color: $red;
  font-size: 14px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
  
  &:hover {
    background-color: rgba($red, 0.05);
  }
  
  &.loading {
    cursor: default;
    opacity: 0.6;
    
    &:hover {
      background-color: transparent;
    }
  }
}

.message-row {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
  width: 100%;
  
  &--right {
    justify-content: flex-end;
  }
}

.message-avatar {
  margin-right: 8px;
  flex-shrink: 0;
  
  .avatar-img {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    object-fit: cover;
    border: 1px solid rgba($red, 0.2);
  }
  
  .avatar-placeholder {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background-color: rgba($red, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: $red;
    text-transform: uppercase;
  }
}

.message-content {
  display: flex;
  flex-direction: column;
  max-width: 70%;
}

.message-name {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
  margin-left: 8px;
}

.chat-bubble {
  padding: 12px 16px;
  background: #fff;
  border-radius: 18px;
  position: relative;
  word-wrap: break-word;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  
  &--left {
    background: #fff;
    color: black;
    border-top-left-radius: 5px;
    align-self: flex-start;
  }
  
  &--right {
    background: $red;
    color: #fff;
    border-top-right-radius: 5px;
    align-self: flex-end;
  }
  
  .message-time {
    font-size: 11px;
    text-align: right;
    margin-top: 4px;
    
    .chat-bubble--left & {
      color: #999;
    }
    .chat-bubble--right & {
        color: rgba(255, 255, 255, 0.8);
      }
  }
}

/* 聊天消息切换动画 */
.fade-message-enter-active, 
.fade-message-leave-active {
  transition: all 0.3s ease;
}

.fade-message-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.fade-message-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

.chat-box-tray {
  background: #f7f7f7;
  display: flex;
  align-items: center;
  padding: 15px;
  border-top: 1px solid #eee;
  
  input {
    flex: 1;
    margin: 0 15px;
    padding: 10px;
    border: 1px solid #eee;
    border-radius: 20px;
    background: #fff;
    
    &:focus {
      outline: none;
      border-color: $red;
    }
    
    &:disabled {
      background: #f5f5f5;
      color: #999;
    }
  }
  
  .tray-icon {
    color: $red;
    font-size: 22px;
    cursor: pointer;
    transition: color 0.3s;
    
    &:hover {
      color: darken($red, 10%);
    }
  }
  
  .el-button--primary {
    background-color: $red;
    border-color: $red;
    
    &:hover, &:focus:not(:disabled) {
      background-color: darken($red, 5%);
      border-color: darken($red, 5%);
    }
    
    &:disabled {
      background-color: lighten($red, 20%);
      border-color: lighten($red, 20%);
    }
  }
}

.ml-2 {
  margin-left: 8px;
}

/* 针对移动端的QQ风格导航 */
@media (max-width: 768px) {
  .chat-row {
    position: relative;
    overflow: hidden;
  }
  
  .chat-col-left,
  .chat-col-right {
    width: 100%;
    position: absolute;
    left: 0;
    top: 0;
    height: 80vh;
    transition: transform 0.3s ease;
  }
  
  .chat-col-right {
    transform: translateX(100%);
  }
  
  /* 当聊天激活时的样式 */
  .mobile-chat-active {
    .chat-col-left {
      transform: translateX(-100%);
    }
    
    .chat-col-right {
      transform: translateX(0);
    }
  }
  
  .back-button {
    font-size: 16px;
    color: $red;
    margin-right: 12px;
    display: inline-flex;
    align-items: center;
    
    .el-icon {
      margin-right: 5px;
    }
  }
  
  .chat-panel {
    max-height: calc(80vh - 140px);
  }
}

// 修复原有的响应式代码
@media (max-width: 768px) {
  // 删除或注释掉原来的代码，避免冲突
  // .chat-row {
  //   flex-direction: column;
  // }
  
  // .chat-col-left,
  // .chat-col-right {
  //   width: 100%;
  // }
  
  // .chat-col-left {
  //   height: 30vh;
  //   border-right: none;
  //   border-bottom: 1px solid #eee;
  // }
  
  // .chat-panel {
  //   max-height: calc(40vh - 140px);
  // }
}
</style>
