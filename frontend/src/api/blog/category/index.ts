import request from '@/config/axios'

// 博客分类 VO
export interface CategoryVO {
  name: string // 分类名称
  parentId: number // 父分类ID
}

// 博客分类 API
export const CategoryApi = {
  // 查询博客分类列表
  getCategoryList: async (params) => {
    return await request.get({ url: `/blog/category/list`, params })
  },

  // 查询博客分类详情
  getCategory: async (id: number) => {
    return await request.get({ url: `/blog/category/get?id=` + id })
  },

  // 新增博客分类
  createCategory: async (data: CategoryVO) => {
    return await request.post({ url: `/blog/category/create`, data })
  },

  // 修改博客分类
  updateCategory: async (data: CategoryVO) => {
    return await request.put({ url: `/blog/category/update`, data })
  },

  // 删除博客分类
  deleteCategory: async (id: number) => {
    return await request.delete({ url: `/blog/category/delete?id=` + id })
  },

  // 导出博客分类 Excel
  exportCategory: async (params) => {
    return await request.download({ url: `/blog/category/export-excel`, params })
  }
}
