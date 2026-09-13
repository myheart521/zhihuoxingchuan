import request from '@/config/axios'

// 博客标签 VO
export interface TagVO {
  id: number // 标签ID
  name: string // 标签名称
}

// 博客标签 API
export const TagApi = {
  // 查询博客标签分页
  getTagPage: async (params: any) => {
    return await request.get({ url: `/blog/tag/page`, params })
  },

  // 查询博客标签列表
  getTagList: async (params: any) => {
    return await request.get({ url: `/blog/tag/list`, params })
  },

  // 查询博客标签详情
  getTag: async (id: number) => {
    return await request.get({ url: `/blog/tag/get?id=` + id })
  },

  // 新增博客标签
  createTag: async (data: TagVO) => {
    return await request.post({ url: `/blog/tag/create`, data })
  },

  // 修改博客标签
  updateTag: async (data: TagVO) => {
    return await request.put({ url: `/blog/tag/update`, data })
  },

  // 删除博客标签
  deleteTag: async (id: number) => {
    return await request.delete({ url: `/blog/tag/delete?id=` + id })
  },

  // 导出博客标签 Excel
  exportTag: async (params) => {
    return await request.download({ url: `/blog/tag/export-excel`, params })
  }
}
