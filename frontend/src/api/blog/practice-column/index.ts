import request from '@/config/axios'
// 查询实践主题的博客表页
export const PracticeColumnApi = {
  // 查询博客表（含审核、置顶状态）分页
  getPracticePage: async (params: any) => {
    return await request.get({ url: `/blog/blog/practice/page`, params })
  },

  //获取轮播图
  getPracticeCarousel:async () => {
    return await request.get({ url: `/blog/blog/practice/carousel` })
  }
}
