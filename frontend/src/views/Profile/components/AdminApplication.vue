<template>
  <div class="admin-application">
    <el-alert
      v-if="!verificationPassed"
      type="warning"
      show-icon
      :closable="false"
      title="需要先完成身份认证"
      description="申请成为管理员需要先完成身份认证。请在「身份认证」标签页完成认证后再申请。"
      class="verification-alert"
    />
    <el-alert
      v-else
      type="info"
      show-icon
      :closable="false"
      title="申请管理员说明"
      description="作为管理员，您将拥有更多权限来管理博客内容和用户。请认真填写申请理由，我们将仔细审核您的申请。"
      class="application-alert"
    />

    <div v-if="verificationPassed">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="application-form"
      >
        <!-- 个人介绍 -->
        <el-form-item label="个人介绍" prop="applicationIntroduction">
          <el-input
            v-model="formData.applicationIntroduction"
            type="textarea"
            :rows="3"
            placeholder="请简要介绍您的个人背景和相关经验"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <!-- 申请理由 -->
        <el-form-item label="申请理由" prop="reasonApplication">
          <el-input
            v-model="formData.reasonApplication"
            type="textarea"
            :rows="5"
            placeholder="请详细描述您申请成为管理员的理由和目标"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>

        <!-- 服务承诺 -->
        <el-form-item label="服务承诺" prop="agreement">
          <el-checkbox v-model="formData.agreement">
            我承诺将遵守平台规则，公正处理内容，积极参与管理工作
          </el-checkbox>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submitForm"> 提交申请</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 申请状态展示 -->
    <div v-if="applicationStatus.status" class="application-status">
      <el-divider>申请状态</el-divider>
      <el-result
        :icon="applicationStatusIcons[applicationStatus.status]"
        :title="applicationStatus.title"
        :sub-title="applicationStatus.subTitle"
      >
        <template #extra>
          <el-button
            v-if="applicationStatus.status === 'rejected'"
            type="primary"
            @click="resetForm"
          >
            重新申请
          </el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserProfile } from '@/api/system/user/profile'
import { applyManage } from '@/api/system/user'

// 用户资料
const userProfile = ref({
  id: 0,
  realName: '',
  idCardFront: '',
  idCardBack: '',
  auditStatus: 0, // 0-未认证 1-审核中 2-已通过 3-已拒绝
  application: 0, // 0-未申请 1-审核中 2-已通过 3-已拒绝
  applicationIntroduction: '',
  reasonApplication: '',
  roles: []
})

// 验证状态
const verificationPassed = ref(false)

// 表单数据
const formData = reactive({
  applicationIntroduction: '', // 与后端字段保持一致
  reasonApplication: '', // 与后端字段保持一致
  agreement: false
})

// 表单验证规则
const rules = {
  applicationIntroduction: [
    { required: true, message: '请填写个人介绍', trigger: 'blur' },
    { min: 10, max: 500, message: '个人介绍长度在10到500个字符之间', trigger: 'blur' }
  ],
  reasonApplication: [
    { required: true, message: '请填写申请理由', trigger: 'blur' },
    { min: 20, max: 1000, message: '申请理由长度在20到1000个字符之间', trigger: 'blur' }
  ],
  agreement: [
    {
      validator: (rule: any, value: boolean, callback: Function) => {
        if (value === false) {
          callback(new Error('请同意服务承诺'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

const formRef = ref()
const loading = ref(false)

// 申请状态，根据后端返回的状态显示不同的结果
// status: 空-未申请, pending-审核中, approved-已通过, rejected-已拒绝
type ApplicationStatusType = '' | 'pending' | 'approved' | 'rejected'

// 状态图标映射，解决el-result icon类型问题
const applicationStatusIcons = {
  '': '',
  pending: 'warning',
  approved: 'success',
  rejected: 'error'
} as const

const applicationStatus = reactive({
  status: '' as ApplicationStatusType,
  title: '',
  subTitle: ''
})

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    loading.value = true

    // 组装提交的数据
    const submitData = {
      applicationIntroduction: formData.applicationIntroduction,
      reasonApplication: formData.reasonApplication
    }

    // 调用后端API提交申请信息
    const res = await applyManage(submitData)

    if (res) {
      ElMessage.success('申请已提交，请等待审核')

      // 更新申请状态
      applicationStatus.status = 'pending'
      applicationStatus.title = '申请审核中'
      applicationStatus.subTitle = '您的管理员申请已提交，正在等待审核，我们将尽快处理'

      // 重新获取用户信息以更新状态
      await fetchUserProfile()
    } else {
      ElMessage.error('提交失败，请稍后重试')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请检查表单')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  formData.agreement = false

  // 如果状态是被拒绝，重置状态
  if (applicationStatus.status === 'rejected') {
    applicationStatus.status = ''
  }
}

// 获取用户资料
const fetchUserProfile = async () => {
  try {
    const res = await getUserProfile()
    if (res) {
      userProfile.value = res

      // 如果有之前的申请记录，填充表单
      if (res.applicationIntroduction) {
        formData.applicationIntroduction = res.applicationIntroduction
      }
      if (res.reasonApplication) {
        formData.reasonApplication = res.reasonApplication
      }

      // 检查认证状态
      verificationPassed.value = res.auditStatus === 2 // 认证通过状态为2

      // 更新申请状态
      updateApplicationStatus(res.application)
    }
  } catch (error) {
    console.error('获取用户资料失败:', error)
  }
}

// 更新申请状态 - 修复错误：去掉this引用
const updateApplicationStatus = (appStatus: number) => {
  let isAdmin = false
  userProfile.value.roles.forEach((role) => {
    console.log('11111', role.name)
    if (role.name == '超级管理员' || role.name == '博客管理员') {
      applicationStatus.status = 'approved'
      applicationStatus.title = '您已是管理员'
      applicationStatus.subTitle = '您已经是管理员，无需再次申请'
      isAdmin = true
      return
    }
  })
  if (isAdmin) {
    return
  }
  // 0-未申请 1-审核中 2-已通过 3-已拒绝
  switch (appStatus) {
    case 0:
      applicationStatus.status = 'pending'
      applicationStatus.title = '申请审核中'
      applicationStatus.subTitle = '您的管理员申请已提交，正在等待审核，我们将尽快处理'
      break
    case 1:
      // 未申请状态不显示结果
      applicationStatus.status = ''
      // applicationStatus.title = '您已是管理员或者已经被'
      // applicationStatus.subTitle = '您的管理员申请已提交，正在等待审核，我们将尽快处理'
      break
    // case 2:
    //   applicationStatus.status = 'approved'
    //   applicationStatus.title = '申请已通过'
    //   applicationStatus.subTitle = '恭喜您！您的管理员申请已通过审核，现在可以使用管理功能'
    //   break
    // case 2:
    //   applicationStatus.status = 'rejected'
    //   applicationStatus.title = '申请被拒绝'
    //   applicationStatus.subTitle = '很遗憾，您的申请未通过审核，您可以重新提交申请'
    //   break
  }
}

// 页面加载时获取用户资料和申请状态
onMounted(() => {
  fetchUserProfile()
})
</script>

<style lang="scss" scoped>
.admin-application {
  .verification-alert,
  .application-alert {
    margin-bottom: 24px;
  }

  .application-form {
    max-width: 600px;
    margin: 0 auto;
  }

  .role-description {
    margin-top: 8px;
    padding: 10px;
    background-color: #f8f9fa;
    border-radius: 4px;
    font-size: 14px;

    .description-title {
      color: #606266;
      font-weight: 600;
      margin-bottom: 4px;
    }

    .description-content {
      color: #606266;
      line-height: 1.5;
    }
  }

  :deep(.el-form-item__label) {
    font-weight: 500;
  }

  :deep(.el-textarea__inner) {
    font-family: inherit;
  }

  :deep(.el-form-item.is-error .el-input__inner),
  :deep(.el-form-item.is-error .el-textarea__inner) {
    border-color: #f56c6c;
  }

  :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background-color: #409eff;
    border-color: #409eff;
  }

  :deep(.el-select) {
    width: 100%;
  }

  .application-status {
    margin-top: 40px;

    :deep(.el-divider) {
      margin: 24px 0;

      .el-divider__text {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        background-color: #fff;
      }
    }

    :deep(.el-result) {
      padding: 40px 20px;

      .el-result__icon {
        margin-bottom: 20px;
      }

      .el-result__title {
        margin-top: 0;
        font-size: 20px;
        color: #303133;
      }

      .el-result__subtitle {
        margin-top: 10px;
        font-size: 14px;
        color: #606266;
      }

      .el-result__extra {
        margin-top: 24px;
      }
    }
  }
}
</style>
