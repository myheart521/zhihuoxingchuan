<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, Plus, Download, View, Warning } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/formatTime'

// 定义接口类型
interface InviteInfo {
  id: number
  code: string
  type: number
  status: number
  validDays: number
  createUserId: number
  createUserName: string
  usedUserId: number
  usedUserName: string
  usedTime: string
  expireTime: string
  createTime: string
}

interface QueryParams {
  pageNo: number
  pageSize: number
  code: string
  status?: number
  startTime?: string
  endTime?: string
}

// 列表数据
const loading = ref(false)
const total = ref(0)
const dataList = ref<InviteInfo[]>([])
const selectedIds = ref<number[]>([])

// 查询条件
const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 10,
  code: '',
  status: undefined,
  startTime: undefined,
  endTime: undefined
})

// 日期范围
const dateRange = ref<[Date, Date] | null>(null)

// 生成对话框
const generateDialogVisible = ref(false)
const generateForm = reactive({
  count: 5,
  type: 1,
  validDays: 7
})
const generatedCodes = ref<{id: number, code: string}[]>([])

// 类型选项
const typeOptions = [
  { label: '普通用户', value: 1 },
  { label: '管理员', value: 2 }
]

// 状态选项
const statusOptions = [
  { label: '未使用', value: 0 },
  { label: '已使用', value: 1 },
  { label: '已过期', value: 2 }
]

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    // 模拟数据
    const mockData: InviteInfo[] = [
      {
        id: 1,
        code: 'ADMIN2024XYZ1',
        type: 2,
        status: 0,
        validDays: 30,
        createUserId: 1,
        createUserName: 'admin',
        usedUserId: 0,
        usedUserName: '',
        usedTime: '',
        expireTime: '2024-04-13 00:00:00',
        createTime: '2024-03-13 00:00:00'
      },
      {
        id: 2,
        code: 'USER2024ABC1',
        type: 1,
        status: 1,
        validDays: 7,
        createUserId: 1,
        createUserName: 'admin',
        usedUserId: 2,
        usedUserName: '张三',
        usedTime: '2024-03-14 10:30:00',
        expireTime: '2024-03-20 00:00:00',
        createTime: '2024-03-13 00:00:00'
      },
      {
        id: 3,
        code: 'ADMIN2024XYZ2',
        type: 2,
        status: 2,
        validDays: 7,
        createUserId: 1,
        createUserName: 'admin',
        usedUserId: 0,
        usedUserName: '',
        usedTime: '',
        expireTime: '2024-03-10 00:00:00',
        createTime: '2024-03-03 00:00:00'
      },
      {
        id: 4,
        code: 'USER2024ABC2',
        type: 1,
        status: 0,
        validDays: 15,
        createUserId: 1,
        createUserName: 'admin',
        usedUserId: 0,
        usedUserName: '',
        usedTime: '',
        expireTime: '2024-03-28 00:00:00',
        createTime: '2024-03-13 00:00:00'
      },
      {
        id: 5,
        code: 'USER2024ABC3',
        type: 1,
        status: 1,
        validDays: 7,
        createUserId: 1,
        createUserName: 'admin',
        usedUserId: 3,
        usedUserName: '李四',
        usedTime: '2024-03-15 14:20:00',
        expireTime: '2024-03-20 00:00:00',
        createTime: '2024-03-13 00:00:00'
      }
    ]
    dataList.value = mockData
    total.value = mockData.length
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置查询
const resetQuery = () => {
  queryParams.code = ''
  queryParams.status = undefined
  queryParams.startTime = undefined
  queryParams.endTime = undefined
  dateRange.value = null
  queryParams.pageNo = 1
  getList()
}

// 处理查询
const handleQuery = () => {
  if (dateRange.value) {
    queryParams.startTime = formatDate(dateRange.value[0])
    queryParams.endTime = formatDate(dateRange.value[1])
  } else {
    queryParams.startTime = undefined
    queryParams.endTime = undefined
  }
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
  queryParams.pageNo = 1
  getList()
}

// 处理选择变化
const handleSelectionChange = (selection: InviteInfo[]) => {
  selectedIds.value = selection.map(item => item.id)
}

// 打开生成对话框
const openGenerateDialog = () => {
  generateForm.count = 5
  generateForm.type = 1
  generateForm.validDays = 7
  generatedCodes.value = []
  generateDialogVisible.value = true
}

// 生成邀请码
const handleGenerate = async () => {
  if (generateForm.count <= 0 || generateForm.count > 100) {
    ElMessage.warning('生成数量必须在1-100之间')
    return
  }
  if (generateForm.validDays <= 0) {
    ElMessage.warning('有效天数必须大于0')
    return
  }
  
  try {
    // 模拟生成邀请码
    const prefix = generateForm.type === 1 ? 'USER' : 'ADMIN'
    const date = new Date().getFullYear().toString()
    const codes = []
    
    for (let i = 0; i < generateForm.count; i++) {
      const randomStr = Math.random().toString(36).substring(2, 8).toUpperCase()
      codes.push({
        id: 100 + i,
        code: `${prefix}${date}${randomStr}`
      })
    }
    
    generatedCodes.value = codes
    ElMessage.success(`成功生成${generateForm.count}个邀请码`)
  } catch (error) {
    console.error('生成失败:', error)
  }
}

// 复制邀请码
const copyCode = (code: string) => {
  navigator.clipboard.writeText(code).then(() => {
    ElMessage.success('复制成功')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

// 导出邀请码
const exportCodes = () => {
  if (generatedCodes.value.length === 0) {
    ElMessage.warning('没有可导出的邀请码')
    return
  }
  
  const content = generatedCodes.value.map(item => item.code).join('\n')
  const blob = new Blob([content], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `邀请码_${new Date().getTime()}.txt`
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

// 删除邀请码
const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该邀请码吗？删除后不可恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // TODO: 调用后端接口
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请至少选择一条记录')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个邀请码吗？删除后不可恢复`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // TODO: 调用后端接口
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('批量删除成功')
    getList()
  } catch (error) {
    console.error('批量删除失败:', error)
  }
}

// 禁用/启用邀请码
const toggleStatus = async (row: InviteInfo) => {
  if (row.status === 1) {
    ElMessage.warning('已使用的邀请码不能修改状态')
    return
  }
  
  const newStatus = row.status === 0 ? 2 : 0
  const statusText = newStatus === 0 ? '启用' : '禁用'
  
  try {
    await ElMessageBox.confirm(`确定要${statusText}该邀请码吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // TODO: 调用后端接口
    await new Promise(resolve => setTimeout(resolve, 500))
    row.status = newStatus
    ElMessage.success(`${statusText}成功`)
  } catch (error) {
    console.error('操作失败:', error)
  }
}

// 跳转到用户详情
const goToUserDetail = (userId: number) => {
  if (userId === 0) return
  // TODO: 跳转到用户详情页
  console.log('跳转到用户详情页:', userId)
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
        <el-form-item label="邀请码" prop="code">
          <el-input
            v-model="queryParams.code"
            placeholder="请输入邀请码"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" prop="dateRange">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <div class="search-buttons">
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="!border-none" shadow="never">
      <div class="mb-4 flex gap-2">
        <el-button 
          type="primary" 
          :icon="Plus" 
          @click="openGenerateDialog"
        >
          生成邀请码
        </el-button>
        <el-button 
          type="danger" 
          :icon="Delete" 
          :disabled="selectedIds.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="dataList"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="code" label="邀请码" min-width="180">
          <template #default="{ row }">
            <div class="flex items-center">
              <span class="font-mono">{{ row.code }}</span>
              <el-button 
                link 
                type="primary" 
                @click="copyCode(row.code)"
              >
                复制
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 2 ? 'danger' : 'success'">
              {{ typeOptions.find(item => item.value === row.type)?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : row.status === 1 ? 'info' : 'danger'">
              {{ statusOptions.find(item => item.value === row.status)?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="validDays" label="有效天数" width="100" align="center" />
        <el-table-column prop="createUserName" label="创建人" width="120" />
        <el-table-column label="使用情况" min-width="200">
          <template #default="{ row }">
            <div v-if="row.status === 1">
              <div>使用人：
                <el-link type="primary" :underline="false" @click="goToUserDetail(row.usedUserId)">
                  {{ row.usedUserName }}
                </el-link>
              </div>
              <div>使用时间：{{ formatDate(row.usedTime) }}</div>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="expireTime" label="过期时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.expireTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status !== 1"
              link 
              :type="row.status === 0 ? 'danger' : 'success'"
              @click="toggleStatus(row)"
            >
              {{ row.status === 0 ? '禁用' : '启用' }}
            </el-button>
            <el-button 
              link 
              type="danger" 
              :icon="Delete"
              @click="handleDelete(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="queryParams.pageNo"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 生成邀请码对话框 -->
    <el-dialog
      v-model="generateDialogVisible"
      title="生成邀请码"
      width="600px"
    >
      <el-form :model="generateForm" label-width="100px">
        <el-form-item label="生成数量" required>
          <el-input-number 
            v-model="generateForm.count" 
            :min="1" 
            :max="100"
            controls-position="right"
          />
          <span class="text-gray-500 ml-2">最多100个</span>
        </el-form-item>
        <el-form-item label="邀请码类型" required>
          <el-radio-group v-model="generateForm.type">
            <el-radio :label="1">普通用户</el-radio>
            <el-radio :label="2">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="有效天数" required>
          <el-input-number 
            v-model="generateForm.validDays" 
            :min="1" 
            :max="365"
            controls-position="right"
          />
        </el-form-item>
      </el-form>
      
      <div v-if="generatedCodes.length > 0" class="mt-4">
        <div class="flex justify-between items-center mb-2">
          <h3 class="text-lg font-bold">生成结果</h3>
          <el-button type="primary" :icon="Download" @click="exportCodes">导出</el-button>
        </div>
        <el-scrollbar height="200px">
          <div class="space-y-2">
            <div 
              v-for="item in generatedCodes" 
              :key="item.id" 
              class="flex justify-between items-center p-2 bg-gray-50 rounded"
            >
              <span class="font-mono">{{ item.code }}</span>
              <el-button 
                link 
                type="primary" 
                @click="copyCode(item.code)"
              >
                复制
              </el-button>
            </div>
          </div>
        </el-scrollbar>
      </div>
      
      <template #footer>
        <el-button @click="generateDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleGenerate">生成</el-button>
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
.search-form :deep(.el-date-picker),
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

@media screen and (max-width: 768px) {
  .search-form :deep(.el-select),
  .search-form :deep(.el-date-picker),
  .search-form :deep(.el-input) {
    width: 100%;
  }
  
  .search-form .search-buttons {
    justify-content: flex-start;
  }
}
</style>
