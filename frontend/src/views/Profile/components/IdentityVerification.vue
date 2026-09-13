<template>
  <div class="identity-verification">
    <el-alert
      type="info"
      show-icon
      :closable="false"
      title="身份认证说明"
      description="上传您的身份证正反面照片进行实名认证，认证后可在申请管理员页面提交管理员申请。请确保身份证信息清晰可见，图片大小不超过5MB。"
      class="verification-alert"
    />

    <!-- 认证表单 -->
    <el-form 
      v-if="!isVerified"
      ref="formRef" 
      :model="formData" 
      :rules="rules" 
      label-width="100px"
      class="verification-form"
    >
      <!-- 真实姓名 -->
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="formData.realName" placeholder="请输入您的真实姓名" maxlength="20" />
      </el-form-item>
      
<!--      &lt;!&ndash; 身份证号 &ndash;&gt;-->
<!--      <el-form-item label="身份证号" prop="idCardNo">-->
<!--        <el-input v-model="formData.idCardNo" placeholder="请输入您的身份证号码" maxlength="18" />-->
<!--      </el-form-item>-->

      <!-- 身份证正面 -->
      <el-form-item label="身份证正面" prop="idCardFront">
        <div class="upload-container">
          <el-upload
            class="id-card-uploader"
            :show-file-list="false"
            :before-upload="file => beforeUpload(file, 'front')"
            accept="image/*"
          >
            <img v-if="formData.idCardFront" :src="formData.idCardFront" class="id-card-image" />
            <div v-else class="id-card-placeholder">
              <el-icon class="upload-icon"><Plus /></el-icon>
              <span>上传身份证正面</span>
            </div>
          </el-upload>
          <el-button 
            v-if="formData.idCardFront"
            type="danger" 
            size="small" 
            @click.stop="removeImage('front')"
            class="remove-button"
          >
            <el-icon><Delete /></el-icon>
            删除图片
          </el-button>
        </div>
        <div class="upload-tips">请上传清晰的身份证人像面照片</div>
      </el-form-item>

      <!-- 身份证反面 -->
      <el-form-item label="身份证反面" prop="idCardBack">
        <div class="upload-container">
          <el-upload
            class="id-card-uploader"
            :show-file-list="false"
            :before-upload="file => beforeUpload(file, 'back')"
            accept="image/*"
          >
            <img v-if="formData.idCardBack" :src="formData.idCardBack" class="id-card-image" />
            <div v-else class="id-card-placeholder">
              <el-icon class="upload-icon"><Plus /></el-icon>
              <span>上传身份证反面</span>
            </div>
          </el-upload>
          <el-button 
            v-if="formData.idCardBack" 
            type="danger" 
            size="small" 
            @click.stop="removeImage('back')"
            class="remove-button"
          >
            <el-icon><Delete /></el-icon>
            删除图片
          </el-button>
        </div>
        <div class="upload-tips">请上传清晰的身份证国徽面照片</div>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button 
          type="primary" 
          :loading="loading" 
          @click="submitForm"
        >
          提交认证
        </el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 审核状态提示 -->
    <div v-if="verificationStatus.status" class="verification-status">
      <el-divider>认证状态</el-divider>
      <el-result
        :icon="verificationStatusIcons[verificationStatus.status]"
        :title="verificationStatus.title"
        :sub-title="verificationStatus.subTitle"
      >
        <template #extra>
          <el-button v-if="verificationStatus.status === 'rejected'" type="primary" @click="resetVerification">重新提交</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, inject, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { getUploadUrl } from '@/components/UploadFile/src/useUpload'
import { getRefreshToken, getTenantId } from '@/utils/auth'
import { getUserProfile } from '@/api/system/user/profile'
import { updateUserInfo } from '@/api/system/user'

// 表单数据
const formData = reactive({
  realName: '',
  idCardFront: '',
  idCardBack: ''
})

// 用户资料
const userProfile = ref({
  id: 0,
  realName: '',
  idCardFront: '',
  idCardBack: '',
  auditStatus: 0, // 0-未认证 1-审核中 2-已通过 3-已拒绝
})

// 认证状态类型定义
type VerificationStatusType = '' | 'pending' | 'approved' | 'rejected';

// 状态图标映射
const verificationStatusIcons = {
  '': 'info',
  'pending': 'warning',
  'approved': 'success',
  'rejected': 'error'
} as const;

// 认证状态，根据后端返回的状态显示不同的结果
// status: 空-未认证, pending-审核中, approved-已通过, rejected-已拒绝
const verificationStatus = reactive({
  status: '' as VerificationStatusType,
  title: '',
  subTitle: ''
})

// 表单验证规则
const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2到20个字符之间', trigger: 'blur' }
  ],
  // idCardNo: [
  //   { required: true, message: '请输入身份证号码', trigger: 'blur' },
  //   { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
  // ],
  idCardFront: [
    { required: true, message: '请上传身份证正面照片', trigger: 'change' }
  ],
  idCardBack: [
    { required: true, message: '请上传身份证反面照片', trigger: 'change' }
  ]
}

const formRef = ref()
const loading = ref(false)

// 是否已认证
const isVerified = computed(() => {
  return verificationStatus.status === 'pending' || verificationStatus.status === 'approved';
})

// 外部引入的activeName
const activeName = inject('activeName', ref(''))

// 跳转到管理员申请标签页
const redirectToAdminApplication = () => {
  if (activeName && typeof activeName.value !== 'undefined') {
    activeName.value = 'adminApplication'
  }
}

// 文件上传前验证
const beforeUpload = async (file: File, type: 'front' | 'back') => {
  // 检查文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  
  // 检查文件大小（限制为5MB）
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB!')
    return false
  }

  try {
    loading.value = true
    // 上传文件
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
      // 更新表单数据
      if (type === 'front') {
        formData.idCardFront = result.data
      } else {
        formData.idCardBack = result.data
      }
      return false // 阻止默认上传行为
    } else {
      ElMessage.error('上传失败：' + result.message)
      return false
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败，请重试')
    return false
  } finally {
    loading.value = false
  }
}

// 移除图片
const removeImage = (type: 'front' | 'back') => {
  if (type === 'front') {
    formData.idCardFront = ''
  } else {
    formData.idCardBack = ''
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    loading.value = true

    // 组装提交的数据
    const submitData = {
      realName: formData.realName,
      idCardFront: formData.idCardFront,
      idCardBack: formData.idCardBack
    }
    
    // 调用后端API提交认证信息
    const res = await updateUserInfo(submitData)
    
    if (res) {
      ElMessage.success('认证信息提交成功，请等待审核')
      
      // 更新认证状态
      verificationStatus.status = 'pending'
      verificationStatus.title = '认证审核中'
      verificationStatus.subTitle = '您的认证信息已提交，正在等待管理员审核，请耐心等待'
      
      // 重新获取用户信息
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
  formData.idCardFront = ''
  formData.idCardBack = ''
}

// 重新认证（当认证被拒绝时）
const resetVerification = () => {
  verificationStatus.status = ''
  resetForm()
}

// 获取用户资料
const fetchUserProfile = async () => {
  try {
    const res = await getUserProfile()
    if (res) {
      userProfile.value = res
      
      // 更新表单数据
      formData.realName = res.realName || ''
      formData.idCardFront = res.idCardFront || ''
      formData.idCardBack = res.idCardBack || ''
      
      // 设置认证状态
      updateVerificationStatus(res.auditStatus)
    }
  } catch (error) {
    console.error('获取用户资料失败:', error)
  }
}

// 根据后端返回的审核状态更新界面显示
const updateVerificationStatus = (auditStatus: number) => {
  // 转换后端的审核状态为前端使用的状态
  // 0-未认证 1-审核中 2-已通过 3-已拒绝
  switch (auditStatus) {
    case 0:
      verificationStatus.status = ''
      break
    case 1:
      verificationStatus.status = 'pending'
      verificationStatus.title = '认证审核中'
      verificationStatus.subTitle = '您的认证信息已提交，正在等待管理员审核，请耐心等待'
      break
    case 2:
      verificationStatus.status = 'approved'
      verificationStatus.title = '认证已通过'
      verificationStatus.subTitle = '您的身份认证已通过审核，现在可以申请成为管理员'
      break
    case 3:
      verificationStatus.status = 'rejected'
      verificationStatus.title = '认证被拒绝'
      verificationStatus.subTitle = '您的认证申请被拒绝，可重新提交'
      break
    default:
      verificationStatus.status = ''
  }
}

// 组件挂载时获取用户资料
onMounted(() => {
  fetchUserProfile()
})
</script>

<style lang="scss" scoped>
.identity-verification {
  .verification-alert {
    margin-bottom: 24px;
  }
  
  .verification-form {
    max-width: 600px;
    margin: 0 auto;
  }
  
  .upload-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .id-card-uploader {
      width: 100%;
      max-width: 450px; // 修改上传框的最大宽度
      
      :deep(.el-upload) {
        width: 100%;
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: border-color 0.3s;
        
        &:hover {
          border-color: #409eff;
        }
      }
      
      .id-card-image {
        width: 100%;
        height: auto;
        max-height: 250px; // 限制图片显示的最大高度
        object-fit: contain; // 保持图片比例
        display: block;
      }
      
      .id-card-placeholder {
        width: 100%;
        height: 150px; // 修改上传框高度为更小的尺寸
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        color: #8c939d;
        
        .upload-icon {
          font-size: 28px;
          margin-bottom: 8px;
        }
      }
    }
    
    .remove-button {
      margin-top: 8px;
    }
  }
  
  .upload-tips {
    font-size: 12px;
    color: #909399;
    line-height: 1.4;
    margin-top: 8px;
    text-align: center; // 居中显示提示文字
  }
  
  .verification-status {
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
