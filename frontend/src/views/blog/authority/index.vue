<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Edit, Key, Plus, Picture } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/formatTime'
import {
  getUserManagePage,
  updateUserStatus,
  updateUserManage,
  createBlogAdmin,
  updateUser
} from '@/api/system/user'
import { getUploadUrl } from '@/components/UploadFile/src/useUpload'
import { getRefreshToken, getTenantId } from '@/utils/auth'

// 定义接口类型
interface AdminInfo {
  id: number
  username: string
  email: string
  code: string
  status: number
  reputation: number
  blogCount: number
  likeCount: number
  realName: string
  idCardFront: string
  idCardBack: string
  auditStatus: number
  auditComment: string
  createTime: string
}

interface QueryParams {
  pageNo: number
  pageSize: number
  keyword?: string
  code?: string
  status?: number
}

// 列表数据
const loading = ref(false)
const total = ref(0)
const dataList = ref<AdminInfo[]>([])

// 查询条件
const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 25,
  keyword: '',
  code: undefined,
  status: undefined
})

// 角色选项
const roleOptions = [
  { label: '超级管理员', value: 'super_admin' },
  { label: '审核用户', value: 'blog_examine' },
  { label: '管理员', value: 'blog_manage' }
]

// 审核状态选项
const auditStatusOptions = [
  { label: '未审核', value: 0 },
  { label: '审核中', value: 1 },
  { label: '已通过', value: 2 },
  { label: '已拒绝', value: 3 }
]

// 表单对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formData = reactive({
  id: undefined as number | undefined,
  username: '',
  nickname: '',
  remark: '',
  email: '',
  mobile: '',
  sex: undefined as number | undefined,
  avatar: '',
  password: '',
  realName: '',
  idCardFront: '',
  idCardBack: ''
})

// 密码对话框
const passwordDialogVisible = ref(false)
const passwordForm = reactive({
  id: undefined as number | undefined,
  oldCode: '',
  newCode: '',
  password: ''
})

// 审核对话框
const auditDialogVisible = ref(false)
const auditForm = reactive({
  id: undefined as number | undefined,
  auditStatus: 2,
  auditComment: ''
})

// 添加性别选项
const sexOptions = [
  { label: '男', value: 1 },
  { label: '女', value: 2 }
]

// 添加上传加载状态
const uploadLoading = reactive({
  idCardFront: false,
  idCardBack: false
})

// 修改表单验证规则
const rules = {
  username: [
    { required: true, message: '用户账号不能为空', trigger: 'blur' },
    { min: 4, max: 30, message: '长度在 4 到 30 个字符之间', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]+$/, message: '只能包含字母和数字', trigger: 'blur' }
  ],
  // 动态密码验证规则，仅在新增时生效
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 4, max: 16, message: '长度在 4 到 16 个字符之间', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '用户昵称不能为空', trigger: 'blur' },
    { max: 30, message: '长度不能超过 30 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '真实姓名不能为空', trigger: 'blur' },
    { max: 30, message: '长度不能超过 30 个字符', trigger: 'blur' }
  ],
  email: [
    {
      pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
      message: '请输入有效的邮箱地址',
      trigger: 'blur'
    }
  ],
  mobile: [{ pattern: /^1[3456789]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }],
  idCardFront: [{ required: true, message: '请上传身份证正面照片', trigger: 'change' }],
  idCardBack: [{ required: true, message: '请上传身份证背面照片', trigger: 'change' }]
}

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    // 确保分页参数在有效范围
    if (queryParams.pageNo < 1) {
      queryParams.pageNo = 1
    }
    if (queryParams.pageSize < 1) {
      queryParams.pageSize = 25
    }

    const res = await getUserManagePage(queryParams)
    if (res) {
      dataList.value = res.list
      total.value = res.total

      // 处理页码超出总页数的情况
      const totalPages = Math.ceil(total.value / queryParams.pageSize)
      if (totalPages > 0 && queryParams.pageNo > totalPages) {
        queryParams.pageNo = totalPages
        await getList() // 重新获取数据
      }
    } else {
      ElMessage.error('获取列表失败')
    }
  } catch (error) {
    console.error('获取列表失败:', error)
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

// 重置查询
const resetQuery = () => {
  queryParams.keyword = ''
  queryParams.code = undefined
  queryParams.status = undefined
  queryParams.pageNo = 1
  getList()
}

// 处理查询
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

// 处理页码变化
const handlePageChange = (val: number) => {
  queryParams.pageNo = val
  getList()
}

// 处理每页条数变化
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  queryParams.pageNo = 1 // 切换每页条数时，重置为第一页
  getList()
}

// 打开添加对话框
const openAddDialog = () => {
  dialogTitle.value = '添加管理员'
  formData.id = undefined
  formData.username = ''
  formData.nickname = ''
  formData.remark = ''
  formData.email = ''
  formData.mobile = ''
  formData.sex = undefined
  formData.avatar = ''
  formData.password = ''
  formData.realName = ''
  formData.idCardFront = ''
  formData.idCardBack = ''
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row: AdminInfo) => {
  dialogTitle.value = '修改用户信息'
  formData.id = row.id
  formData.username = row.username
  formData.nickname = row.nickname
  formData.remark = row.remark
  formData.email = row.email
  formData.mobile = row.mobile
  formData.sex = row.sex
  formData.avatar = row.avatar
  formData.realName = row.realName
  formData.idCardFront = row.idCardFront
  formData.idCardBack = row.idCardBack
  // 编辑时不需要设置密码字段
  formData.password = ''
  dialogVisible.value = true
}

// 打开修改密码对话框
const openPasswordDialog = (row: AdminInfo) => {
  passwordForm.id = row.id
  passwordForm.oldCode = row.code
  passwordForm.newCode = row.code
  passwordForm.password = ''
  passwordDialogVisible.value = true
}

// 打开审核对话框
const openAuditDialog = (row: AdminInfo) => {
  auditForm.id = row.id
  auditForm.auditStatus = row.auditStatus
  auditForm.auditComment = row.auditComment
  auditDialogVisible.value = true
}

// 修改上传图片方法
const handleUpload = async (file: File, type: 'idCardFront' | 'idCardBack') => {
  uploadLoading[type] = true
  try {
    const uploadFormData = new FormData()
    uploadFormData.append('file', file)

    const response = await fetch(getUploadUrl(), {
      method: 'POST',
      headers: {
        Accept: '*',
        Authorization: '' + getRefreshToken(),
        'tenant-id': getTenantId()
      },
      body: uploadFormData
    })

    const result = await response.json()
    if (result.code === 0) {
      // 直接修改 formData 对应的属性
      formData[type] = result.data
      return false // 返回 false 阻止 el-upload 默认上传行为
    } else {
      ElMessage.error('上传失败：' + result.message)
      return false
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败，请重试')
    return false
  } finally {
    uploadLoading[type] = false
  }
}

// 修改表单提交方法
const formRef = ref()
const submitForm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    formLoading.value = true
    let submitData = { ...formData }

    if (formData.id) {
      // 编辑时删除密码字段
      delete submitData.password
      const res = await updateUser(submitData)
      if (res === true) {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        getList()
      } else {
        ElMessage.error('修改失败')
      }
    } else {
      // 新增时必须包含密码字段
      const res = await createBlogAdmin(submitData)
      if (res === true) {
        ElMessage.success('添加成功')
        dialogVisible.value = false
        getList()
      } else {
        ElMessage.error('添加失败')
      }
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('表单验证失败，请检查输入')
  } finally {
    formLoading.value = false
  }
}

// 修改密码表单验证规则
const passwordRules = {
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 4, max: 16, message: '长度在 4 到 16 个字符之间', trigger: 'blur' }
  ]
}

// 密码表单引用
const passwordFormRef = ref()
const submitPassword = async () => {
  if (!passwordFormRef.value) return

  try {
    await passwordFormRef.value.validate()

    const res = await updateUserManage(
      passwordForm.newCode,
      passwordForm.oldCode,
      passwordForm.id as number,
      passwordForm.password
    )

    if (res === true) {
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
      getList()
    } else {
      ElMessage.error('密码修改失败')
    }
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error('密码验证失败，请检查输入')
  }
}

// 提交审核
const submitAudit = async () => {
  if (auditForm.auditStatus === 3 && !auditForm.auditComment) {
    ElMessage.warning('请填写拒绝原因')
    return
  }

  try {
    // TODO: 调用后端接口
    await new Promise((resolve) => setTimeout(resolve, 1000))
    ElMessage.success('审核提交成功')
    auditDialogVisible.value = false
    getList()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

// 切换状态
const toggleStatus = async (row: AdminInfo) => {
  try {
    const newStatus = row.status === 0 ? 1 : 0
    const action = newStatus === 1 ? '禁用' : '启用'

    await ElMessageBox.confirm(`确认要${action}该管理员吗？`, '提示', {
      type: 'warning'
    })

    const res = await updateUserStatus(row.id, newStatus)
    if (res === true) {
      ElMessage.success(`${action}成功`)
      getList() // 刷新列表
    } else {
      ElMessage.error(`${action}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 添加获取角色标签类型的方法
const getRoleTagType = (code: string) => {
  switch (code) {
    case 'super_admin':
      return 'danger'
    case 'blog_manage':
      return 'warning'
    case 'blog_examine':
      return 'success'
    default:
      return 'info'
  }
}

// 添加修改角色对话框
const roleDialogVisible = ref(false)
const roleForm = reactive({
  id: undefined as number | undefined,
  oldCode: '',
  newCode: '',
  password: ''
})

// 角色表单验证规则
const roleRules = {
  newCode: [{ required: true, message: '请选择新角色', trigger: 'change' }]
}

// 角色表单引用
const roleFormRef = ref()
const submitRoleChange = async () => {
  if (!roleFormRef.value) return

  try {
    await roleFormRef.value.validate()

    const res = await updateUserManage(
      roleForm.newCode,
      roleForm.oldCode,
      roleForm.id as number,
      roleForm.password
    )

    if (res === true) {
      ElMessage.success('角色修改成功')
      roleDialogVisible.value = false
      getList()
    } else {
      ElMessage.error('角色修改失败')
    }
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error('表单验证失败，请检查输入')
  }
}

// 在 script setup 中添加 openRoleDialog 方法
const openRoleDialog = (row: AdminInfo) => {
  roleForm.id = row.id
  roleForm.oldCode = row.code
  roleForm.newCode = row.code
  roleForm.password = ''
  roleDialogVisible.value = true
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="mb-4 !border-none" shadow="never">
      <el-form ref="queryFormRef" :model="queryParams" class="search-form">
        <el-form-item label="关键词" prop="keyword">
          <el-input
            v-model="queryParams.keyword"
            placeholder="用户名/真实姓名"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="角色" prop="code">
          <el-select v-model="queryParams.code" placeholder="请选择角色" clearable>
            <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="0" />
            <el-option label="禁用" :value="1" />
          </el-select>
        </el-form-item>
        <div class="search-buttons">
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery" v-hasPermi="['system:user:manage']">重置</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="!border-none" shadow="never">
      <div class="mb-4">
        <el-button type="primary" :icon="Plus" @click="openAddDialog" v-hasPermi="['system:manage:create']">添加管理员</el-button>
      </div>

      <el-table v-loading="loading" :data="dataList" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="code" label="角色" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.code)">
              {{ roleOptions.find((item) => item.value === row.code)?.label || '未知角色' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="证件照" width="180" align="center">
          <template #default="{ row }">
            <div class="id-card-preview">
              <el-image
                v-if="row.idCardFront"
                :src="row.idCardFront"
                fit="cover"
                class="id-card-thumb"
                :preview-src-list="[row.idCardFront]"
                :initial-index="0"
                preview-teleported
              >
                <template #error>
                  <div class="image-error">
                    <el-icon>
                      <Picture />
                    </el-icon>
                    <span>暂无图片</span>
                  </div>
                </template>
              </el-image>
              <el-image
                v-if="row.idCardBack"
                :src="row.idCardBack"
                fit="cover"
                class="id-card-thumb"
                :preview-src-list="[row.idCardBack]"
                :initial-index="0"
                preview-teleported
              >
                <template #error>
                  <div class="image-error">
                    <el-icon>
                      <Picture />
                    </el-icon>
                    <span>暂无图片</span>
                  </div>
                </template>
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="
                row.auditStatus === 2
                  ? 'success'
                  : row.auditStatus === 3
                    ? 'danger'
                    : row.auditStatus === 1
                      ? 'warning'
                      : 'info'
              "
            >
              {{ auditStatusOptions.find((item) => item.value === row.auditStatus)?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="账号状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">
              {{ row.status === 0 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="统计信息" width="200">
          <template #default="{ row }">
            <div class="text-sm">
              <div>信誉：{{ row.reputation }}</div>
              <div>博客：{{ row.blogCount }}篇</div>
              <div>获赞：{{ row.likeCount }}次</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="openEditDialog(row)" v-hasPermi="['system:user:update']">
              编辑
            </el-button>
            <el-button link type="primary" :icon="Key" @click="openPasswordDialog(row)" v-hasPermi="['system:user:update-password']">
              修改密码
            </el-button>
            <el-button link type="warning" @click="openRoleDialog(row)" v-hasPermi="['system:manage:update']"> 修改角色 </el-button>
            <el-button
              link
              :type="row.status === 0 ? 'danger' : 'success'"
              @click="toggleStatus(row)"
              v-hasPermi="['system:user:update']"
            >
              {{ row.status === 0 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[ 25, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 表单对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户账号" prop="username">
              <el-input
                v-model="formData.username"
                placeholder="请输入4-30位数字或字母"
                :disabled="!!formData.id"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickname">
              <el-input v-model="formData.nickname" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 仅在新增时显示密码字段 -->
        <el-row :gutter="20" v-if="!formData.id">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="formData.password"
                type="password"
                placeholder="请输入4-16位密码"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model="formData.mobile" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="formData.remark" placeholder="请输入备注信息" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="身份证正面" prop="idCardFront">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="(file) => handleUpload(file.raw, 'idCardFront')"
          >
            <el-icon v-if="uploadLoading.idCardFront" class="avatar-uploader-icon">
              <el-icon-loading />
            </el-icon>
            <img
              v-else-if="formData.idCardFront"
              :src="formData.idCardFront"
              class="avatar-image"
            />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
          <div class="upload-tip">请上传清晰的身份证正面照片</div>
        </el-form-item>

        <el-form-item label="身份证背面" prop="idCardBack">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="(file) => handleUpload(file.raw, 'idCardBack')"
          >
            <el-icon v-if="uploadLoading.idCardBack" class="avatar-uploader-icon">
              <el-icon-loading />
            </el-icon>
            <img v-else-if="formData.idCardBack" :src="formData.idCardBack" class="avatar-image" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
          <div class="upload-tip">请上传清晰的身份证背面照片</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="formLoading" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="400px">
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="80px"
      >
        <el-form-item label="新密码" prop="password">
          <el-input
            v-model="passwordForm.password"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPassword">确定</el-button>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog v-model="auditDialogVisible" title="审核管理员" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="审核结果" required>
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="3">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" :required="auditForm.auditStatus === 3">
          <el-input
            v-model="auditForm.auditComment"
            type="textarea"
            :rows="4"
            placeholder="请输入审核备注，拒绝时必填"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改角色对话框 -->
    <el-dialog v-model="roleDialogVisible" title="修改角色" width="400px">
      <el-form ref="roleFormRef" :model="roleForm" :rules="roleRules" label-width="80px">
        <el-form-item label="当前角色">
          <el-tag :type="getRoleTagType(roleForm.oldCode)">
            {{ roleOptions.find((item) => item.value === roleForm.oldCode)?.label || '未知角色' }}
          </el-tag>
        </el-form-item>
        <el-form-item label="新角色" prop="newCode">
          <el-select v-model="roleForm.newCode" placeholder="请选择角色" style="width: 100%">
            <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input
            v-model="roleForm.password"
            type="password"
            placeholder="可选，某些角色可能需要密码确认"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoleChange">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.app-container {
  padding: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 20px;
  margin-bottom: 10px;
}

.search-form :deep(.el-form-item) {
  margin-bottom: 10px;
  margin-right: 0;
}

.search-form :deep(.el-select),
.search-form :deep(.el-input) {
  width: 240px;
}

.search-form .search-buttons {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
  margin-bottom: 10px;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 240px;
  height: 135px;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 240px;
  height: 135px;
  text-align: center;
  line-height: 135px;
}

.avatar-image {
  width: 240px;
  height: 135px;
  object-fit: cover;
}

:deep(.el-form-item__label) {
  font-weight: bold;
}

:deep(.el-form-item.is-required > .el-form-item__label::before) {
  content: '*';
  color: var(--el-color-danger);
  margin-right: 4px;
}

@media screen and (max-width: 768px) {
  .search-form :deep(.el-select),
  .search-form :deep(.el-input) {
    width: 100%;
  }

  .search-form .search-buttons {
    justify-content: flex-start;
  }
}

/* 证件照预览样式 */
.id-card-preview {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.id-card-thumb {
  width: 80px;
  height: 50px;
  border-radius: 4px;
  cursor: pointer;
  object-fit: cover;
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
}

.id-card-thumb:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.image-error {
  width: 80px;
  height: 50px;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #909399;
  font-size: 12px;
}

.image-error .el-icon {
  font-size: 20px;
  margin-bottom: 4px;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style>
