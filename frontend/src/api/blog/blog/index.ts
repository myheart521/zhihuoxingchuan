import request from '@/config/axios'

// 博客表（含审核、置顶状态） VO
export interface PostVO {
  id: number // 博客ID
  userId: number // 作者ID
  title: string // 标题
  content: string // 内容
  status: number // 状态
  isTop: number // 是否置顶
  topTime: Date // 置顶时间
  visibility: number // 可见性
  viewCount: number // 阅读量
  likeCount: number // 点赞数
  auditUserId: number // 审核人ID（管理员）
  auditTime: Date // 审核时间
  auditComment: string // 审核意见
  frontCover: string // 博客封面
}

// 新增博客的类型
export interface CreateBlogVO {
  title: string
  content: string
  categoryId: number
  tagIds: []
  visibility: number
  status: number
  frontCover: string
}

// 博客表（含审核、置顶状态） API
export const BlogApi = {
  //点赞
  likeBlog: async (id:number) => {
    return await request.post({ url: `/blog/blog/praise?id=` + id })
  },
  //修改审核状态
  updateBlogStatus: async (id:number,status:number,auditComment:string) => {
    return await request.put({ url: `/blog/blog/examine/${id}/${status}`,data:{auditComment} })
  },

  //获取博客详情
  getBlogDetail: async (id:number) => {
    return await request.get({ url: `/blog/blog/detail/${id}` })
  },
  // 删除博客
  deleteBlog: async (id:number) => {
    return await request.delete({ url: `/blog/blog/delete/${id}` })
  },
  // 删除用户个人博客
  deleteUserBlog: async (id:number) => {
    return await request.delete({ url: `/blog/blog/delete/user/${id}` })
  },
  // 修改博客置顶状态
  updateBlogTop: async (id:number,params: any) => {
    return await request.put({ url: `/blog/blog/top/${id}`, params })
  },
   // 修改博客可见度
   updateBlogVisibility: async (id:number,params: any) => {
    return await request.put({ url: `/blog/blog/visibility/${id}`, params })
  },
  // 用户修改个人博客可见度
  updateBlogVisibilityUser: async (id:number,params: any) => {
    return await request.put({ url: `/blog/blog/visibility/user/${id}`, params })
  },
   // 查询用户个人博客表（含审核、置顶状态）分页
   getUserBlogPage: async (params: any) => {
    return await request.get({ url: `/blog/blog/user/page`, params })
  },
  // 查询博客表（含审核、置顶状态）分页
  getBlogPage: async (params: any) => {
    return await request.get({ url: `/blog/blog/page`, params })
  },
  // 查询首页博客表（含审核、置顶状态）分页
  getIndexBlogPage: async (params: any) => {
    return await request.get({ url: `/blog/blog/index/page`, params })
  },
  // 查询博客表（含审核、置顶状态）分页
  getPostPage: async (params: any) => {
    return await request.get({ url: `/blog/post/page`, params })
  },

  // 查询博客表（含审核、置顶状态）详情
  getPost: async (id: number) => {
    return await request.get({ url: `/blog/post/get?id=` + id })
  },

  // 新增博客表（含审核、置顶状态）
  createPost: async (data: CreateBlogVO) => {
    return await request.post({ url: `/blog/blog/create`, data })
  },

  // 修改博客表（含审核、置顶状态）
  updatePost: async (data: PostVO) => {
    return await request.put({ url: `/blog/blog/update`, data })
  },

  // 删除博客表（含审核、置顶状态）
  deletePost: async (id: number) => {
    return await request.delete({ url: `/blog/post/delete?id=` + id })
  },

  // 导出博客表（含审核、置顶状态） Excel
  exportPost: async (params) => {
    return await request.download({ url: `/blog/post/export-excel`, params })
  },

  // 审核博客
  auditBlog: async (data: {
    id: number
    status: number
    auditComment: string
  }) => {
    return await request.post({ 
      url: '/blog/blog/audit',
      data
    })
  }
}
