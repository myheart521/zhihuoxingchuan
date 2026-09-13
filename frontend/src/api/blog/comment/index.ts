import request from '@/config/axios'

export interface CommentVO {
  id: number // 评论ID（顶级）
  postId: number // 关联的博客ID
  userId: number // 评论者用户ID
  content: string // 评论内容
  status: number // 状态：0-待审核 1-已发布 2-已删除
  auditUserId: number // 审核人ID（管理员）
}

// 二级评论回复 VO
export interface CommentReplyVO {
  id: number // 回复ID（二级）
  parentId: number // 关联的顶级评论ID
  postId: number // 冗余博客ID（避免跨表查文章）
  userId: number // 回复者用户ID
  replyToUserId: number // 被回复的用户ID（可空，默认回复父级评论人）
  content: string // 回复内容
  status: number // 状态：0-待审核 1-已发布 2-已删除
  auditUserId: number // 审核人ID（管理员）
}

// 顶级评论 API
export const CommentApi = {
  //查询用户博客下的评论分页
  getUserBlogCommentPage: async (params: any) => {
    return await request.get({ url: `/blog/comment/user/blog/page`, params })
  },

  //查询用户个人评论分页
  getUserCommentPage: async (params: any) => {
    return await request.get({ url: `/blog/comment/user/page`, params })
  },

  // 查询顶级评论分页
  getCommentPage: async (params: any) => {
    return await request.get({ url: `/blog/comment/page`, params })
  },

  // 查询顶级评论详情
  getComment: async (id: number) => {
    return await request.get({ url: `/blog/comment/get?id=` + id })
  },

  // 新增顶级评论
  createComment: async (data: CommentVO) => {
    return await request.post({ url: `/blog/comment/create`, data })
  },

  // 修改顶级评论
  updateComment: async (data: CommentVO) => {
    return await request.put({ url: `/blog/comment/update`, data })
  },

  // 删除顶级评论
  deleteComment: async (id: number) => {
    return await request.delete({ url: `/blog/comment/delete?id=` + id })
  },

  // 导出顶级评论 Excel
  exportComment: async (params) => {
    return await request.download({ url: `/blog/comment/export-excel`, params })
  },

  // ==================== 子表（二级评论回复） ====================

  // 获得二级评论回复列表
  getCommentReplyListByParentId: async (parentId) => {
    return await request.get({
      url: `/blog/comment/comment-reply/list-by-parent-id?parentId=` + parentId
    })
  }
}
// 二级评论回复 API
export const CommentReplyApi = {
  // 查询二级评论回复分页
  getCommentReplyPage: async (params: any) => {
    return await request.get({ url: `/blog/comment-reply/page`, params })
  },

  // 查询二级评论回复详情
  getCommentReply: async (id: number) => {
    return await request.get({ url: `/blog/comment-reply/get?id=` + id })
  },

  // 新增二级评论回复
  createCommentReply: async (data: CommentReplyVO) => {
    return await request.post({ url: `/blog/comment-reply/create`, data })
  },

  // 修改二级评论回复
  updateCommentReply: async (data: CommentReplyVO) => {
    return await request.put({ url: `/blog/comment-reply/update`, data })
  },

  // 删除二级评论回复
  deleteCommentReply: async (id: number) => {
    return await request.delete({ url: `/blog/comment-reply/delete?id=` + id })
  },

  // 导出二级评论回复 Excel
  exportCommentReply: async (params) => {
    return await request.download({ url: `/blog/comment-reply/export-excel`, params })
  }
}
