<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, View } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { formatDate } from '@/utils/formatTime'
import { BlogApi } from '@/api/blog/blog'

const router = useRouter()

// 定义接口
interface BlogPost {
  id: number
  title: string
  content: string
  summary: string
  frontCover: string
  status: number
  isTop: number
  visibility: number
  viewCount: number
  likeCount: number
  createTime: string
  updateTime: string
  author: {
    id: number
    username: string
    role: number
  }
  category: {
    id: number
    name: string
  }
  tags: Array<{
    id: number
    name: string
  }>
}

// 修改查询参数接口定义
interface QueryParams {
  pageNo: number
  pageSize: number
  userId?: number
  status: number // 状态固定为1-待审核
  isTop?: number
  visibility?: number
  keyword?: string
  categoryId?: number
  tagId?: number
  startTime?: string
  endTime?: string
}

// 修改查询参数
const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 10,
  status: 1, // 固定查询待审核状态
  keyword: '',
  startTime: '',
  endTime: ''
})

// 日期范围
const dateRange = ref<[Date | null, Date | null]>([null, null])

// 列表数据
const loading = ref(false)
const total = ref(0)
const blogList = ref<BlogPost[]>([])

// 审核对话框数据
const auditDialogVisible = ref(false)
const auditForm = reactive({
  id: 0,
  status: 2,
  auditComment: ''
})

// 审核表单规则
const auditFormRules = {
  status: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
  auditComment: [
    {
      required: true,
      message: '请输入审核意见',
      trigger: 'blur',
      validator: (rule: any, value: string, callback: Function) => {
        if (auditForm.status === 3 && (!value || value.trim().length < 5)) {
          callback(new Error('拒绝发布时必须填写不少于5个字的审核意见'))
        } else {
          callback()
        }
      }
    }
  ]
}

// 修改获取列表数据的方法
const getList = async () => {
  loading.value = true
  try {
    const res = await BlogApi.getBlogPage(queryParams)
    if (res) {
      blogList.value = res.list
      total.value = res.total
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

// 查询
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  dateRange.value = [null, null]
  Object.assign(queryParams, {
    pageNo: 1,
    pageSize: 10,
    keyword: '',
    startTime: '',
    endTime: ''
  })
  getList()
}

// 查看博客
const handleView = (row: BlogPost) => {
  router.push(`/blog/detail/${row.id}`)
}

// 打开审核对话框
const handleAudit = (row: BlogPost) => {
  auditForm.id = row.id
  auditForm.status = 2 // 默认选中"通过"
  auditForm.auditComment = ''
  auditDialogVisible.value = true
}

// 修改提交审核的方法
const submitAudit = async () => {
  try {
    const res = await BlogApi.updateBlogStatus(
      auditForm.id,
      auditForm.status,
      auditForm.auditComment
    )

    if (res === true) {
      ElMessage.success(auditForm.status === 2 ? '博客已发布' : '博客已拒绝')
      auditDialogVisible.value = false
      getList() // 刷新列表
    } else {
      ElMessage.error('审核失败')
    }
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

// 添加分页方法
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val: number) => {
  queryParams.pageNo = val
  getList()
}

// 修改日期范围监听
watch(dateRange, (val) => {
  if (val && val[0] && val[1]) {
    queryParams.startTime = formatDate(val[0], 'YYYY-MM-DD HH:mm:ss')
    queryParams.endTime = formatDate(val[1], 'YYYY-MM-DD HH:mm:ss')
  } else {
    queryParams.startTime = undefined
    queryParams.endTime = undefined
  }
})

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form ref="queryForm" :model="queryParams" :inline="true">
        <el-form-item label="关键词" prop="keyword">
          <el-input
            v-model="queryParams.keyword"
            placeholder="请输入标题关键词"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 列表区域 -->
    <el-card class="list-card">
      <el-table v-loading="loading" :data="blogList" style="width: 100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author.username" label="作者" width="120" />
        <el-table-column prop="category.name" label="分类" width="120" />
        <el-table-column label="标签" width="200">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tags" :key="tag.id" size="small" class="mr-1">
              {{ tag.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click="handleView(row)">查看</el-button>
            <el-button link type="success" @click="handleAudit(row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditDialogVisible"
      title="博客审核"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="auditFormRef" :model="auditForm" :rules="auditFormRules" label-width="100px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="2">通过发布</el-radio>
            <el-radio :label="3">拒绝发布</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="auditComment" :required="auditForm.status === 3">
          <el-input
            v-model="auditForm.auditComment"
            type="textarea"
            :rows="4"
            :placeholder="
              auditForm.status === 3 ? '请输入拒绝原因（必填）' : '请输入审核意见（选填）'
            "
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.app-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    .el-table {
      margin-bottom: 20px;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  :deep(.el-tag) {
    margin-right: 5px;
    margin-bottom: 5px;
  }
}
</style>
