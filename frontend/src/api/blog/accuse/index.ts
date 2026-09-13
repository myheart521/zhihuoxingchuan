import request from '@/config/axios'

// 博客举报记录 VO
export interface AccuseVO {
  id: number // 举报ID
  postId: number // 被举报博客ID
  userId: number // 举报人ID
  type: string // 举报类型：字典内容
  content: string // 举报详情
  status: number // 处理状态：0-待处理 1-已处理
  handleUserId: number // 处理人ID（管理员）
  handleTime: Date // 处理时间
}

// 博客举报记录 API
export const AccuseApi = {
  // 查询博客举报记录分页
  getAccusePage: async (params: any) => {
    return await request.get({ url: `/blog/accuse/page`, params })
  },

  // 查询博客举报记录详情
  getAccuse: async (id: number) => {
    return await request.get({ url: `/blog/accuse/get?id=` + id })
  },

  // 新增博客举报记录
  createAccuse: async (data: AccuseVO) => {
    return await request.post({ url: `/blog/accuse/create`, data })
  },

  // 修改博客举报记录
  updateAccuse: async (data: AccuseVO) => {
    return await request.put({ url: `/blog/accuse/update`, data })
  },

  // 删除博客举报记录
  deleteAccuse: async (id: number) => {
    return await request.delete({ url: `/blog/accuse/delete?id=` + id })
  },

  // 导出博客举报记录 Excel
  exportAccuse: async (params) => {
    return await request.download({ url: `/blog/accuse/export-excel`, params })
  }
}