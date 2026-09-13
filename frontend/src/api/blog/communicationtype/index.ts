import request from '@/config/axios'

// 群聊类型管理 VO
export interface CommunicationTypeVO {
  id: number // 主键
  typeName: string // 类型的名称
  typeDes: string // 类型的描述
  typeImage: string // 类型的图片
}

// 群聊类型管理 API
export const CommunicationTypeApi = {
  // 查询群聊类型管理分页
  getCommunicationTypePage: async (params: any) => {
    return await request.get({ url: `/blog/communication-type/page`, params })
  },

  // 查询群聊类型管理详情
  getCommunicationType: async (id: number) => {
    return await request.get({ url: `/blog/communication-type/get?id=` + id })
  },

  // 新增群聊类型管理
  createCommunicationType: async (data: CommunicationTypeVO) => {
    return await request.post({ url: `/blog/communication-type/create`, data })
  },

  // 修改群聊类型管理
  updateCommunicationType: async (data: CommunicationTypeVO) => {
    return await request.put({ url: `/blog/communication-type/update`, data })
  },

  // 删除群聊类型管理
  deleteCommunicationType: async (id: number) => {
    return await request.delete({ url: `/blog/communication-type/delete?id=` + id })
  },

  // 导出群聊类型管理 Excel
  exportCommunicationType: async (params) => {
    return await request.download({ url: `/blog/communication-type/export-excel`, params })
  }
}