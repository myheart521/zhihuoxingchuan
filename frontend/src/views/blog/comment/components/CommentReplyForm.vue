<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
  >
    <el-table :data="formData" class="-mt-10px">
      <el-table-column label="序号" type="index" width="100" />
       <el-table-column label="博客ID" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.postId`" :rules="formRules.postId" class="mb-0px!">
            <el-input v-model="row.postId" placeholder="博客ID" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="用户ID" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.userId`" :rules="formRules.userId" class="mb-0px!">
            <el-input v-model="row.userId" placeholder="请输入回复者用户ID" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="被回复的用户ID" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.replyToUserId`" :rules="formRules.replyToUserId" class="mb-0px!">
            <el-input v-model="row.replyToUserId" placeholder="可空，默认回复父级评论人" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="回复内容" min-width="400">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.content`" :rules="formRules.content" class="mb-0px!">
            <Editor v-model="row.content" height="150px" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.status`" :rules="formRules.status" class="mb-0px!">
            <el-select v-model="row.status" placeholder="请选择状态" clearable class="!w-240px">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.BLOG_COMMENT_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="审核人ID" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.auditUserId`" :rules="formRules.auditUserId" class="mb-0px!">
            <el-input v-model="row.auditUserId" placeholder="（管理员）" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="60">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3">
    <el-button @click="handleAdd" round>+ 添加二级评论回复</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { CommentApi } from '@/api/blog/comment'
import {DICT_TYPE, getIntDictOptions} from "@/utils/dict";

const props = defineProps<{
  parentId: undefined // 关联的顶级评论ID（主表的关联字段）
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref([])
const formRules = reactive({
  parentId: [{ required: true, message: '关联的顶级评论ID不能为空', trigger: 'blur' }],
  postId: [{ required: true, message: '博客ID不能为空', trigger: 'blur' }],
  userId: [{ required: true, message: '回复者用户ID不能为空', trigger: 'blur' }],
  content: [{ required: true, message: '回复内容不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 监听主表的关联字段的变化，加载对应的子表数据 */
watch(
  () => props.parentId,
  async (val) => {
    // 1. 重置表单
    formData.value = []
    // 2. val 非空，则加载数据
    if (!val) {
      return;
    }
    try {
      formLoading.value = true
      formData.value = await CommentApi.getCommentReplyListByParentId(val)
    } finally {
      formLoading.value = false
    }
  },
  { immediate: true }
)

/** 新增按钮操作 */
const handleAdd = () => {
  const row = {
    id: undefined,
    parentId: undefined,
    postId: undefined,
    userId: undefined,
    replyToUserId: undefined,
    content: undefined,
    status: undefined,
    auditUserId: undefined
  }
  row.parentId = props.parentId
  formData.value.push(row)
}

/** 删除按钮操作 */
const handleDelete = (index) => {
  formData.value.splice(index, 1)
}

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
}

/** 表单值 */
const getData = () => {
  return formData.value
}

defineExpose({ validate, getData })
</script>
