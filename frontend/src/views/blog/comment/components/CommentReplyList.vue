<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="回复ID（二级）" align="center" prop="id" />
       <el-table-column label="博客ID" align="center" prop="postId" />
      <el-table-column label="回复者用户ID" align="center" prop="userId" />
      <el-table-column label="被回复的用户ID" align="center" prop="replyToUserId" />
      <el-table-column label="回复内容" align="center" prop="content" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="审核人ID" align="center" prop="auditUserId" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
    </el-table>
  </ContentWrap>
</template>
<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import { CommentApi } from '@/api/blog/comment'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const props = defineProps<{
  parentId?: number // 关联的顶级评论ID（主表的关联字段）
}>()
const loading = ref(false) // 列表的加载中
const list = ref([]) // 列表的数据

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    list.value = await CommentApi.getCommentReplyListByParentId(props.parentId)
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
