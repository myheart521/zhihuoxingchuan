<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="博客ID" prop="postId">
        <el-input v-model="formData.postId" placeholder="请输入关联的博客ID" />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="formData.userId" placeholder="请输入评论者用户ID" />
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入评论者用户名" />
      </el-form-item>
      <el-form-item label="用户角色" prop="userRole">
        <el-select v-model="formData.userRole" placeholder="请选择用户角色" clearable>
          <el-option label="超级管理员" value="超级管理员" />
          <el-option label="博客管理员" value="博客管理员" />
          <el-option label="博客审核用户" value="博客审核用户" />
        </el-select>
      </el-form-item>
      <el-form-item label="博客标题" prop="blogTitle">
        <el-input v-model="formData.blogTitle" placeholder="请输入博客标题" />
      </el-form-item>
      <el-form-item label="博客可见度" prop="blogVisibility">
        <el-select v-model="formData.blogVisibility" placeholder="请选择博客可见度" clearable>
          <el-option label="公开" value="0" />
          <el-option label="仅自己" value="1" />
          <el-option label="粉丝可见" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="评论内容" prop="content">
        <Editor v-model="formData.content" height="150px" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="formData.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BLOG_COMMENT_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核人ID）" prop="auditUserId">
        <el-input v-model="formData.auditUserId" placeholder="审核人ID" />
      </el-form-item>
    </el-form>
    <!-- 子表的表单 -->
    <el-tabs v-model="subTabsName">
      <el-tab-pane label="二级评论回复" name="commentReply">
        <CommentReplyForm ref="commentReplyFormRef" :parent-id="formData.id" />
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { CommentApi, CommentVO } from '@/api/blog/comment'
import CommentReplyForm from './components/CommentReplyForm.vue'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";

/** 顶级评论 表单 */
defineOptions({ name: 'CommentForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  postId: undefined,
  userId: undefined,
  username: undefined,
  userRole: undefined,
  blogTitle: undefined,
  blogVisibility: undefined,
  content: undefined,
  status: undefined,
  auditUserId: undefined
})
const formRules = reactive({
  postId: [{ required: true, message: '关联的博客ID不能为空', trigger: 'blur' }],
  userId: [{ required: true, message: '评论者用户ID不能为空', trigger: 'blur' }],
  username: [{ required: true, message: '评论者用户名不能为空', trigger: 'blur' }],
  userRole: [{ required: true, message: '用户角色不能为空', trigger: 'change' }],
  blogTitle: [{ required: true, message: '博客标题不能为空', trigger: 'blur' }],
  blogVisibility: [{ required: true, message: '博客可见度不能为空', trigger: 'change' }],
  content: [{ required: true, message: '评论内容不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

/** 子表的表单 */
const subTabsName = ref('commentReply')
const commentReplyFormRef = ref()

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
      formData.value = await CommentApi.getComment(id)
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
  // 校验子表单
  try {
    await commentReplyFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'commentReply'
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as CommentVO
    // 拼接子表的数据
    data.commentReplys = commentReplyFormRef.value.getData()
    if (formType.value === 'create') {
      await CommentApi.createComment(data)
      message.success(t('common.createSuccess'))
    } else {
      await CommentApi.updateComment(data)
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
    postId: undefined,
    userId: undefined,
    username: undefined,
    userRole: undefined,
    blogTitle: undefined,
    blogVisibility: undefined,
    content: undefined,
    status: undefined,
    auditUserId: undefined
  }
  formRef.value?.resetFields()
}
</script>
