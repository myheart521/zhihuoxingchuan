import request from '@/config/axios'

// 用户聊天记录 VO
export interface ChatMessagesGroupVO {
  id: number // 主键
  senderId: number // 发送者ID
  senderAvatar: string // 发送者头像
  senderName: string // 发送者名称
  receiverGroup: number // 接收的群聊标识
  text: string // 消息内容
  messageType: number // 消息类型
}

// 用户聊天记录 API
export const ChatMessagesGroupApi = {
  // 查询用户聊天记录分页
  getChatMessagesGroupPage: async (params: any) => {
    return await request.get({ url: `/blog/chat-messages-group/page`, params })
  },

  // 查询用户聊天记录详情
  getChatMessagesGroup: async (id: number) => {
    return await request.get({ url: `/blog/chat-messages-group/get?id=` + id })
  },

  // 新增用户聊天记录
  createChatMessagesGroup: async (data: ChatMessagesGroupVO) => {
    return await request.post({ url: `/blog/chat-messages-group/create`, data })
  },

  // 修改用户聊天记录
  updateChatMessagesGroup: async (data: ChatMessagesGroupVO) => {
    return await request.put({ url: `/blog/chat-messages-group/update`, data })
  },

  // 删除用户聊天记录
  deleteChatMessagesGroup: async (id: number) => {
    return await request.delete({ url: `/blog/chat-messages-group/delete?id=` + id })
  },

  // 导出用户聊天记录 Excel
  exportChatMessagesGroup: async (params) => {
    return await request.download({ url: `/blog/chat-messages-group/export-excel`, params })
  }
}