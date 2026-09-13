<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名（唯一）" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="formData.password" placeholder="请输入加密密码" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-radio-group v-model="formData.role">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.BLOG_USER_AUTHORITY)"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="是否启用" prop="isLocked">
        <el-select
          v-model="formData.isLocked"
          placeholder="0-正常 1-禁用"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BLOG_USER_LOCK)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="邀请码" prop="inviteCode">
        <el-input v-model="formData.inviteCode" placeholder="请输入邀请码" />
      </el-form-item>
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="formData.realName" placeholder="请输入真实姓名（证件照审核用）" />
      </el-form-item>
      <el-form-item label="身份证正面" prop="idCardFront">
        <UploadImg v-model="formData.idCardFront" />
      </el-form-item>
      <el-form-item label="身份证背面" prop="idCardBack">
        <UploadImg v-model="formData.idCardBack" />
      </el-form-item>
<!--todo 管理员新建的账号应该可以直接审核通过-->
      <el-form-item label="审核备注" prop="auditComment">
        <el-input v-model="formData.auditComment" placeholder="请输入审核备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { UserListApi, UserListVO } from '@/api/blog/user-list'

/** 用户表（含角色、审核、信誉等信息） 表单 */
defineOptions({ name: 'UserListForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  username: undefined,
  password: undefined,
  email: undefined,
  role: undefined,
  isLocked: undefined,
  reputation: undefined,
  blogCount: undefined,
  likeCount: undefined,
  inviteCode: undefined,
  realName: undefined,
  idCardFront: undefined,
  idCardBack: undefined,
  auditStatus: undefined,
  auditComment: undefined
})
const formRules = reactive({
  username: [{ required: true, message: '用户名（唯一）不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '加密密码不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await UserListApi.getUserList(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as UserListVO
    if (formType.value === 'create') {
      await UserListApi.createUserList(data)
      message.success(t('common.createSuccess'))
    } else {
      await UserListApi.updateUserList(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    username: undefined,
    password: undefined,
    email: undefined,
    role: undefined,
    isLocked: undefined,
    reputation: undefined,
    blogCount: undefined,
    likeCount: undefined,
    inviteCode: undefined,
    realName: undefined,
    idCardFront: undefined,
    idCardBack: undefined,
    auditStatus: undefined,
    auditComment: undefined
  }
  formRef.value?.resetFields()
}
</script>
