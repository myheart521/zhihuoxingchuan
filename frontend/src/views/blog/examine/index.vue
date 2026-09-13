<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox, ElImage } from 'element-plus'
import { Search, Refresh, View, Check, Close, Lock, Unlock, Loading, Picture } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/formatTime'
import { getUserAuthorityPage, updateUserStatus, updateUserAuditStatus } from '@/api/system/user'
import { ElImageViewer } from 'element-plus'

// 定义接口类型
interface User {
  id: number
  username: string
  mobile: string | null
  sex: number
  avatar: string | null
  status: number
  reputation: number
  blogCount: number
  likeCount: number
  inviteCode: string | null
  realName: string | null
  auditStatus: number
  idCardFront: string | null
  idCardBack: string | null
  auditComment: string | null
  applicationIntroduction: string | null
  reasonApplication: string | null
}

interface QueryParams {
  pageNo: number
  pageSize: number
  username?: string
  mobile?: string
  status?: number
  auditStatus?: number
  createTime?: string[]
}

// 列表数据
const loading = ref(false)
const total = ref(0)
const dataList = ref<User[]>([])

// 查询条件
const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 10,
  username: '',
  mobile: '',
  auditStatus: undefined,
  createTime: undefined
})

// 日期范围
const dateRange = ref<[Date | undefined, Date | undefined]>([undefined, undefined])

// 审核状态选项
const auditStatusOptions = [
  { label: '未审核', value: 0 },
  { label: '审核中', value: 1 },
  { label: '已通过', value: 2 },
  { label: '已拒绝', value: 3 }
]

// 角色选项
const roleOptions = [
  { label: '普通用户', value: 1 },
  { label: '管理员', value: 2 }
]

// 审核对话框
const auditDialogVisible = ref(false)
const auditForm = reactive({
  userId: undefined as number | undefined,
  auditStatus: 2,
  auditComment: '',
  role: 1
})

// 图片预览
const previewImages = ref<string[]>([])
const previewVisible = ref(false)
const previewIndex = ref(0)

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    // 处理日期范围
    if (dateRange.value && dateRange.value[0] && dateRange.value[1]) {
      queryParams.createTime = [
        formatDate(dateRange.value[0], 'YYYY-MM-DD HH:mm:ss'),
        formatDate(dateRange.value[1], 'YYYY-MM-DD HH:mm:ss')
      ]
    } else {
      queryParams.createTime = undefined
    }

    const res = await getUserAuthorityPage(queryParams)
    if (res) {
      dataList.value = res.list
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

// 重置查询
const resetQuery = () => {
  Object.keys(queryParams).forEach(key => {
    if (key !== 'pageNo' && key !== 'pageSize') {
      queryParams[key] = undefined
    }
  })
  dateRange.value = [undefined, undefined]
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
  queryParams.pageNo = 1
  getList()
}

// 处理图片预览
const handlePreview = (frontUrl: string | null, backUrl: string | null) => {
  previewImages.value = [frontUrl, backUrl].filter((url): url is string => !!url)
  if (previewImages.value.length > 0) {
    previewVisible.value = true
  }
}

// 打开审核对话框
const openAuditDialog = (row: User) => {
  auditForm.userId = row.id
  auditForm.auditStatus = 2
  auditForm.auditComment = ''
  auditForm.role = 1
  auditDialogVisible.value = true
}

// 提交审核
const submitAudit = async () => {
  if (!auditForm.userId) {
    ElMessage.warning('用户ID不能为空')
    return
  }
  
  if (auditForm.auditStatus === 3 && !auditForm.auditComment) {
    ElMessage.warning('拒绝时必须填写审核意见')
    return
  }
  
  try {
    const res = await updateUserAuditStatus(
      auditForm.userId,
      auditForm.auditStatus,
      auditForm.auditComment
    )
    
    if (res === true) {
      ElMessage.success(auditForm.auditStatus === 2 ? '审核通过成功' : '审核拒绝成功')
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

// 禁用/启用用户
const handleLock = async (row: User) => {
  try {
    const newStatus = row.status === 0 ? 1 : 0
    const action = newStatus === 0 ? '启用' : '禁用'
    
    await ElMessageBox.confirm(`确认要${action}该用户吗？`, '提示', {
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

// 添加获取审核状态类型的方法
const getAuditStatusType = (status: number) => {
  switch (status) {
    case 2:
      return 'success'
    case 3:
      return 'danger'
    case 1:
      return 'warning'
    default:
      return 'info'
  }
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
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="queryParams.username"
            placeholder="请输入用户名"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input
            v-model="queryParams.mobile"
            placeholder="请输入手机号"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="审核状态" prop="auditStatus">
          <el-select v-model="queryParams.auditStatus" placeholder="请选择状态" clearable>
            <el-option
              v-for="item in auditStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            :default-time="[
              new Date(2000, 1, 1, 0, 0, 0),
              new Date(2000, 1, 1, 23, 59, 59)
            ]"
          />
        </el-form-item>
        <div class="search-buttons">
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery"  v-hasPermi="['blog:blog:query']">重置</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="!border-none" shadow="never">
      <el-table
        v-loading="loading"
        :data="dataList"
        style="width: 100%"
        border
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="mobile" label="手机号" width="120">
          <template #default="{ row }">
            {{ row.mobile || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="120">
          <template #default="{ row }">
            {{ row.realName || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="证件照片" width="220">
          <template #default="{ row }">
            <div v-if="row.idCardFront || row.idCardBack" class="id-card-preview">
              <div class="id-card-images">
                <div v-if="row.idCardFront" class="id-card-item">
                  <el-image
                    :src="row.idCardFront"
                    fit="cover"
                    class="id-card-image"
                    @click="handlePreview(row.idCardFront, row.idCardBack)"
                  >
                    <template #placeholder>
                      <div class="image-placeholder">
                        <el-icon><Loading /></el-icon>
                      </div>
                    </template>
                    <template #error>
                      <div class="image-error">
                        <el-icon><Picture /></el-icon>
                      </div>
                    </template>
                  </el-image>
                  <div class="id-card-label">正面</div>
                </div>
                <div v-if="row.idCardBack" class="id-card-item">
                  <el-image
                    :src="row.idCardBack"
                    fit="cover"
                    class="id-card-image"
                    @click="handlePreview(row.idCardFront, row.idCardBack)"
                  >
                    <template #placeholder>
                      <div class="image-placeholder">
                        <el-icon><Loading /></el-icon>
                      </div>
                    </template>
                    <template #error>
                      <div class="image-error">
                        <el-icon><Picture /></el-icon>
                      </div>
                    </template>
                  </el-image>
                  <div class="id-card-label">反面</div>
                </div>
              </div>
            </div>
            <el-empty v-else description="暂无证件照片" :image-size="32" />
          </template>
        </el-table-column>
        <el-table-column label="申请信息" min-width="200">
          <template #default="{ row }">
            <div v-if="row.applicationIntroduction || row.reasonApplication" class="application-info">
              <el-collapse>
                <el-collapse-item>
                  <template #title>
                    <span class="application-info-title">查看申请信息</span>
                  </template>
                  <div class="application-info-content">
                    <div v-if="row.applicationIntroduction" class="info-item">
                      <div class="info-label">个人介绍：</div>
                      <div class="info-value">{{ row.applicationIntroduction }}</div>
                    </div>
                    <div v-if="row.reasonApplication" class="info-item">
                      <div class="info-label">申请理由：</div>
                      <div class="info-value">{{ row.reasonApplication }}</div>
                    </div>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>
            <el-empty v-else description="暂无申请信息" :image-size="32" />
          </template>
        </el-table-column>
        <el-table-column label="信誉信息" width="200">
          <template #default="{ row }">
            <div>信誉分：{{ row.reputation }}</div>
            <div>博客数：{{ row.blogCount }}</div>
            <div>获赞数：{{ row.likeCount }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getAuditStatusType(row.auditStatus)">
              {{ auditStatusOptions.find(item => item.value === row.auditStatus)?.label || '未知状态' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditComment" label="审核意见" min-width="150" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.auditComment || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.auditStatus !== 2 && row.auditStatus !== 3"
              link 
              type="primary" 
              :icon="Check"
              @click="openAuditDialog(row)"
              v-hasPermi="['system:authority:update']"
            >
              审核
            </el-button>
            <el-button
              link
              :type="row.status === 0 ? 'danger' : 'success'"
              :icon="row.status === 0 ? Lock : Unlock"
              @click="handleLock(row)"
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
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditDialogVisible"
      title="用户审核"
      width="500px"
    >
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="3">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
<!--        <el-form-item v-if="auditForm.auditStatus === 2" label="指定角色">-->
<!--          <el-radio-group v-model="auditForm.role">-->
<!--            <el-radio-->
<!--              v-for="item in roleOptions"-->
<!--              :key="item.value"-->
<!--              :label="item.value"-->
<!--            >-->
<!--              {{ item.label }}-->
<!--            </el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
        <el-form-item label="审核意见">
          <el-input
            v-model="auditForm.auditComment"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 添加图片预览组件 -->
    <el-image-viewer
      v-if="previewVisible"
      :url-list="previewImages"
      :initial-index="previewIndex"
      @close="previewVisible = false"
    />
  </div>
</template>

<style lang="scss" scoped>
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

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
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

.id-card-preview {
  padding: 8px;
  
  .id-card-images {
    display: flex;
    gap: 12px;
  }

  .id-card-item {
    flex: 1;
    text-align: center;
  }

  .id-card-image {
    width: 90px;
    height: 56px;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s;
    border: 1px solid #e4e7ed;

    &:hover {
      transform: scale(1.05);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
  }

  .id-card-label {
    margin-top: 4px;
    font-size: 12px;
    color: #909399;
  }
}

.image-placeholder,
.image-error {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 20px;
}

.application-info {
  padding: 4px;

  :deep(.el-collapse) {
    border: none;
    
    .el-collapse-item__header {
      height: 32px;
      line-height: 32px;
      padding: 0 8px;
      border: none;
      background-color: #f5f7fa;
      border-radius: 4px;
      
      &:hover {
        background-color: #e9ecef;
      }
    }
    
    .el-collapse-item__content {
      padding: 12px 8px;
    }
  }

  .application-info-title {
    font-size: 13px;
    color: #409eff;
  }

  .application-info-content {
    .info-item {
      &:not(:last-child) {
        margin-bottom: 8px;
      }

      .info-label {
        font-size: 13px;
        color: #606266;
        font-weight: 500;
        margin-bottom: 4px;
      }

      .info-value {
        font-size: 13px;
        color: #303133;
        line-height: 1.5;
        word-break: break-all;
        white-space: pre-wrap;
      }
    }
  }
}

:deep(.el-empty) {
  padding: 8px;
  
  .el-empty__image {
    width: 32px;
    height: 32px;
  }
  
  .el-empty__description {
    margin-top: 4px;
    font-size: 12px;
  }
}
</style>
