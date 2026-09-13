import { ref } from 'vue'

export interface Message {
  id: string;
  role: 'user' | 'assistant' | 'system';
  content: string;
  timestamp: number;
}

export interface Conversation {
  id: string;
  title: string;
  messages: Message[];
  createdAt: number;
  updatedAt: number;
}

const STORAGE_KEY = 'ai_university_conversations';

/**
 * 生成唯一ID
 */
export function generateId(): string {
  return Date.now().toString(36) + Math.random().toString(36).substring(2);
}

/**
 * 获取所有对话
 */
export function getConversations(): Conversation[] {
  try {
    const data = localStorage.getItem(STORAGE_KEY);
    return data ? JSON.parse(data) : [];
  } catch (error) {
    console.error('读取对话数据失败:', error);
    return [];
  }
}

/**
 * 保存所有对话
 */
export function saveConversations(conversations: Conversation[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(conversations));
  } catch (error) {
    console.error('保存对话数据失败:', error);
  }
}

/**
 * 创建新对话
 */
export function createConversation(title: string = '新对话'): Conversation {
  const now = Date.now();
  const newConversation: Conversation = {
    id: generateId(),
    title,
    messages: [],
    createdAt: now,
    updatedAt: now
  };
  
  const conversations = getConversations();
  conversations.unshift(newConversation);
  saveConversations(conversations);
  
  return newConversation;
}

/**
 * 更新对话
 */
export function updateConversation(conversation: Conversation): void {
  conversation.updatedAt = Date.now();
  
  const conversations = getConversations();
  const index = conversations.findIndex(c => c.id === conversation.id);
  
  if (index !== -1) {
    conversations[index] = conversation;
    saveConversations(conversations);
  }
}

/**
 * 删除对话
 */
export function deleteConversation(id: string): void {
  const conversations = getConversations();
  const newConversations = conversations.filter(c => c.id !== id);
  saveConversations(newConversations);
}

/**
 * 添加消息到对话
 */
export function addMessageToConversation(conversationId: string, message: Omit<Message, 'id' | 'timestamp'>): Message {
  const conversations = getConversations();
  const conversation = conversations.find(c => c.id === conversationId);
  
  if (!conversation) {
    throw new Error('对话不存在');
  }
  
  const newMessage: Message = {
    ...message,
    id: generateId(),
    timestamp: Date.now()
  };
  
  conversation.messages.push(newMessage);
  conversation.updatedAt = Date.now();
  
  // 如果是第一条用户消息，自动设置对话标题
  if (conversation.messages.length === 1 && message.role === 'user') {
    conversation.title = message.content.substring(0, 20) + (message.content.length > 20 ? '...' : '');
  }
  
  saveConversations(conversations);
  return newMessage;
}
