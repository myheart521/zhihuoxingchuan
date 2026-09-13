import request from '@/config/axios'

// 用户表（含角色、审核、信誉等信息） VO
export interface UserListVO {
  id: number // 用户ID
  username: string // 用户名（唯一）
  email: string // 邮箱
  role: number // 角色：0-未审核用户 1-普通用户 2-管理员用户
  isLocked: number // 账号是否被禁用：0-正常 1-禁用
  reputation: number // 信誉等级（初始100分，违规扣分）
  blogCount: number // 已发布博客数量（用于管理员申请条件）
  likeCount: number // 博客总获赞数（用于管理员申请条件）
  inviteCode: string // 成功使用的邀请码（管理员激活用）
  realName: string // 真实姓名（证件照审核用）
  idCardFront: string // 身份证正面MinIO路径
  idCardBack: string // 身份证背面MinIO路径
  auditStatus: number // 审核状态：0-未审核 1-审核中 2-审核通过 3-拒绝
  auditComment: string // 审核备注（拒绝原因等）
}

// 用户表（含角色、审核、信誉等信息） API
export const UserListApi = {
  // 查询用户表（含角色、审核、信誉等信息）分页
  getUserListPage: async (params: any) => {
    return await request.get({ url: `/blog/user-list/page`, params })
  },

  // 查询用户表（含角色、审核、信誉等信息）详情
  getUserList: async (id: number) => {
    return await request.get({ url: `/blog/user-list/get?id=` + id })
  },

  // 新增用户表（含角色、审核、信誉等信息）
  createUserList: async (data: UserListVO) => {
    return await request.post({ url: `/blog/user-list/create`, data })
  },

  // 修改用户表（含角色、审核、信誉等信息）
  updateUserList: async (data: UserListVO) => {
    return await request.put({ url: `/blog/user-list/update`, data })
  },

  // 删除用户表（含角色、审核、信誉等信息）
  deleteUserList: async (id: number) => {
    return await request.delete({ url: `/blog/user-list/delete?id=` + id })
  },

  // 导出用户表（含角色、审核、信誉等信息） Excel
  exportUserList: async (params) => {
    return await request.download({ url: `/blog/user-list/export-excel`, params })
  }
}
