import request from '@/config/axios'

// 轮播图 VO
export interface CarouselVO {
  id: number // 轮播图ID
  image: string // 图片URL
  title: string // 标题
  description: string // 描述
  link: string // 链接地址
  sort: number // 排序号
  status: number // 状态：0-禁用 1-启用
}

// 轮播图 API
export const CarouselApi = {
  // 查询轮播图分页
  getCarouselPage: async (params: any) => {
    return await request.get({ url: `/blog/carousel/page`, params })
  },

  //查询轮播图全部
  getCarouselList: async () => {
    return await request.get({ url: `/blog/carousel/list` })
  },

  // 查询轮播图详情
  getCarousel: async (id: number) => {
    return await request.get({ url: `/blog/carousel/get?id=` + id })
  },

  // 新增轮播图
  createCarousel: async (data: CarouselVO) => {
    return await request.post({ url: `/blog/carousel/create`, data })
  },

  // 修改轮播图
  updateCarousel: async (data: CarouselVO) => {
    return await request.put({ url: `/blog/carousel/update`, data })
  },

  // 删除轮播图
  deleteCarousel: async (id: number) => {
    return await request.delete({ url: `/blog/carousel/delete?id=` + id })
  },

  // 导出轮播图 Excel
  exportCarousel: async (params) => {
    return await request.download({ url: `/blog/carousel/export-excel`, params })
  }
}
