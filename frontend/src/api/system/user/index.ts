import request from '@/config/axios'

export interface UserVO {
  id: number
  username: string
  nickname: string
  deptId: number
  postIds: string[]
  email: string
  mobile: string
  sex: number
  avatar: string
  loginIp: string
  status: number
  remark: string
  loginDate: Date
  createTime: Date
}
//更新用户的信息
export const updateUserInfo = (data) => {
  return request.put({ url: '/system/user/update/info', data: data })
}

//用户申请成为管理员
export const applyManage = (data) => {
  return request.post({ url: '/system/user/apply/manage', data: data })
}

// 查询管理员审核列表
export const getUserManagePage = (params: PageParam) => {
  return request.get({ url: '/system/user/manage/page', params })
}

// 查询用户审核列表
export const getUserAuthorityPage = (params: PageParam) => {
  return request.get({ url: '/system/user/authority/page', params })
}

// 查询用户管理列表
export const getUserPage = (params: PageParam) => {
  return request.get({ url: '/system/user/page', params })
}

// 查询所有用户列表
export const getAllUser = () => {
  return request.get({ url: '/system/user/all' })
}

// 查询用户详情
export const getUser = (id: number) => {
  return request.get({ url: '/system/user/get?id=' + id })
}

// 新增用户
export const createUser = (data: UserVO) => {
  return request.post({ url: '/system/user/create', data })
}

// 修改用户
export const updateUser = (data: UserVO) => {
  return request.put({ url: '/system/user/update', data })
}

// 删除用户
export const deleteUser = (id: number) => {
  return request.delete({ url: '/system/user/delete?id=' + id })
}

// 导出用户
export const exportUser = (params) => {
  return request.download({ url: '/system/user/export', params })
}

// 下载用户导入模板
export const importUserTemplate = () => {
  return request.download({ url: '/system/user/get-import-template' })
}

// 用户密码重置
export const resetUserPwd = (id: number, password: string) => {
  const data = {
    id,
    password
  }
  return request.put({ url: '/system/user/update-password', data: data })
}

// 用户状态修改
export const updateUserStatus = (id: number, status: number) => {
  const data = {
    id,
    status
  }
  return request.put({ url: '/system/user/update-status', data: data })
}

// 用户审核状态修改
export const updateUserAuditStatus = (id: number, auditStatus: number, auditComment: string) => {
  const data = {
    id,
    auditStatus,
    auditComment
  }
  return request.put({ url: '/system/user/update-authority', data: data })
}

// 修改用户权限
export const updateUserManage = (newCode: string, oldCode: string, userId: number,password:string) => {
  const data = {
    newCode,
    oldCode,
    userId,
    password
  }
  return request.put({ url: '/system/user/update-manage', data: data })
}

// 新增博客管理员
export const createBlogAdmin = (data) => {
  return request.post({ url: '/system/user/manage/create', data: data })
}

// 获取用户精简信息列表
export const getSimpleUserList = (): Promise<UserVO[]> => {
  return request.get({ url: '/system/user/simple-list' })
}
