<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, View, Check, Close, Warning } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/formatTime'
import { getStrDictOptions, DICT_TYPE } from '@/utils/dict'
import { AccuseApi } from '@/api/blog/accuse'
import { useRouter } from 'vue-router'

// 定义接口类型
interface UserInfo {
  id: number
  username: string
  nickname: string
  email: string
  mobile: string
  sex: number
  avatar: string
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
  application: number
}

interface AccuseInfo {
  id: number
  postId: number
  userId: number
  type: string
  content: string
  status: number
  handleUserId: number | null
  handleUserRole: string | null
  handleUserName: string | null
  handleTime: number | null
  createTime: number
  accuseUser: UserInfo
  accusedUser: UserInfo
  accuseRole: string
  accusedRole: string
}

interface QueryParams {
  pageNo: number
  pageSize: number
  type?: string
  content?: string
  status?: number
  handleTime?: string[]
  createTime?: string[]
}

// 列表数据
const loading = ref(false)
const total = ref(0)
const dataList = ref<AccuseInfo[]>([])

// 查询条件
const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 10,
  type: undefined,
  content: undefined,
  status: undefined,
  handleTime: undefined,
  createTime: undefined
})

// 日期范围
const dateRange = ref<[Date | undefined, Date | undefined]>([undefined, undefined])

// 举报类型选项
const accuseTypeOptions = getStrDictOptions(DICT_TYPE.BLOG_ACCUSE_TYPE)

// 处理状态选项
const statusOptions = [
  { label: '待处理', value: 0 },
  { label: '已处理', value: 1 }
]

// 博客处理选项
const blogStatusOptions = [
  { label: '不处理', value: 0 },
  { label: '下架', value: 1 },
  { label: '删除', value: 2 }
]

// 处罚类型选项
const punishTypeOptions = [
  { label: '不处罚', value: 0 },
  { label: '禁言', value: 1 },
  { label: '禁止发博客', value: 2 },
  { label: '封号', value: 3 }
]

// 添加处理方式字典
const processWayOptions = getStrDictOptions(DICT_TYPE.BLOG_ACCUSE_PROCESS)

// 在 script setup 中添加处理对话框的可见性控制
const handleDialogVisible = ref(false)

// 添加可选角色选项
const roleOptions = [
  { label: '已审核用户', value: 'blog_examine' },
  { label: '未审核用户', value: 'blog_unexamine' }
]

// 修改处理表单的接口定义，添加 score 字段
interface HandleForm {
  id: number
  processWay: number // 处理方式
  postId: number // 被举报博客ID
  accusedUserId: number // 被举报用户ID
  accuseUserId: number // 举报人ID
  roleCode?: string // 将要修改的用户角色
  isBlock?: boolean // 是否禁用被举报人账号
  blogStatus?: number // 将要修改的博客状态
  score?: number // 要扣多少分
}

// 修改处理对话框的表单数据初始化
const handleForm = reactive<HandleForm>({
  id: 0,
  processWay: 0,
  postId: 0,
  accusedUserId: 0,
  accuseUserId: 0,
  roleCode: undefined,
  isBlock: false,
  blogStatus: 0,
  score: 0
})

// 详情对话框
const detailDialogVisible = ref(false)
const detailInfo = ref<any>(null)

// 添加角色类型的映射
const roleTypeMap = {
  super_admin: { label: '超级管理员', type: 'danger' },
  blog_manage: { label: '博客管理员', type: 'warning' },
  blog_examine: { label: '已审核用户', type: 'success' },
  blog_unexamine: { label: '未审核用户', type: 'info' }
}

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    // 处理日期范围
    const params = {
      ...queryParams,
      handleTime: dateRange.value?.[0] ? [dateRange.value[0], dateRange.value[1]] : undefined,
      createTime: dateRange.value?.[0] ? [dateRange.value[0], dateRange.value[1]] : undefined
    }

    const res = await AccuseApi.getAccusePage(params)
    dataList.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取列表失败:', error)
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

// 重置查询
const resetQuery = () => {
  queryParams.type = undefined
  queryParams.content = undefined
  queryParams.status = undefined
  queryParams.handleTime = undefined
  queryParams.createTime = undefined
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

// 查看详情
const handleDetail = async (row: AccuseInfo) => {
  try {
    // TODO: 调用后端接口获取详情
    detailInfo.value = {
      ...row,
      authorInfo: {
        reputation: 80,
        accuseCount: 3,
        punishCount: 1
      },
      accuserInfo: {
        reputation: 100,
        accuseCount: 5,
        validCount: 4
      }
    }
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

// 打开处理对话框
const openHandleDialog = (row: AccuseInfo) => {
  // 设置当前处理的举报信息到 detailInfo，以便在对话框中使用
  detailInfo.value = row

  handleForm.id = row.id
  handleForm.processWay = 0
  handleForm.postId = row.postId
  handleForm.accusedUserId = row.accusedUser.id
  handleForm.accuseUserId = row.userId
  handleForm.roleCode = undefined
  handleForm.isBlock = false
  handleForm.blogStatus = 0
  handleForm.score = 0 // 初始化扣分为0
  handleDialogVisible.value = true
}

// 提交处理
const submitHandle = async () => {
  // 表单验证
  if (handleForm.processWay === undefined) {
    ElMessage.warning('请选择处理方式')
    return
  }

  // 如果是违规处理，需要选择博客状态
  if ((handleForm.processWay === 1 || handleForm.processWay === 2) && handleForm.blogStatus === 0) {
    ElMessage.warning('请选择博客处理方式')
    return
  }

  // 如果选择了修改角色，但是当前用户角色已经是最低级别，则提示
  if (handleForm.roleCode && detailInfo.value?.accusedRole === 'blog_unexamine') {
    ElMessage.warning('该用户已经是最低级别角色，无需再降级')
    return
  }

  try {
    // 构建提交的数据，确保所有必填字段都存在
    const submitData = {
      id: handleForm.id,
      processWay: handleForm.processWay,
      postId: handleForm.postId,
      accusedUserId: handleForm.accusedUserId,
      accuseUserId: handleForm.accuseUserId,
      roleCode: handleForm.roleCode || undefined, // 如果未选择，传递undefined
      isBlock: handleForm.isBlock,
      blogStatus: handleForm.processWay === 0 ? 0 : handleForm.blogStatus, // 非违规处理时不修改博客状态
      score: handleForm.score || 0
    }

    const res = await AccuseApi.updateAccuse(submitData)
    if (res) {
      ElMessage.success('处理成功')
      handleDialogVisible.value = false
      getList()
    }
  } catch (error) {
    console.error('处理失败:', error)
    ElMessage.error('处理失败')
  }
}

const router = useRouter()

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="mb-4 !border-none" shadow="never">
      <el-form ref="queryFormRef" :model="queryParams" class="search-form">
        <el-form-item label="举报类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择类型" clearable>
            <el-option
              v-for="item in accuseTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="举报内容" prop="content">
          <el-input
            v-model="queryParams.content"
            placeholder="请输入举报内容"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="处理状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围" prop="dateRange">
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
        <div class="search-buttons">
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery" v-hasPermi="['blog:accuse:query']">重置</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="!border-none" shadow="never">
      <el-table v-loading="loading" :data="dataList" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="postId" label="被举报博客" min-width="240">
          <template #default="{ row }">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <el-avatar :size="32" :src="row.accusedUser.avatar">
                  {{ row.accusedUser.nickname?.charAt(0) }}
                </el-avatar>
                <div class="ml-2">
                  <div class="flex items-center">
                    <span>{{ row.accusedUser.nickname }}</span>
                    <el-tag
                      class="ml-2"
                      :type="roleTypeMap[row.accusedRole]?.type || 'info'"
                      size="small"
                    >
                      {{ roleTypeMap[row.accusedRole]?.label || row.accusedRole }}
                    </el-tag>
                  </div>
                  <div class="text-gray-400 text-xs">博客ID: {{ row.postId }}</div>
                </div>
              </div>
              <el-button type="primary" link @click="router.push(`/blog/detail/${row.postId}`)">
                查看博客
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="userId" label="举报人" width="220">
          <template #default="{ row }">
            <div class="flex items-center">
              <el-avatar :size="32" :src="row.accuseUser.avatar">
                {{ row.accuseUser.nickname?.charAt(0) }}
              </el-avatar>
              <div class="ml-2">
                <div class="flex items-center">
                  <span>{{ row.accuseUser.nickname }}</span>
                  <el-tag
                    class="ml-2"
                    :type="roleTypeMap[row.accuseRole]?.type || 'info'"
                    size="small"
                  >
                    {{ roleTypeMap[row.accuseRole]?.label || row.accuseRole }}
                  </el-tag>
                </div>
                <div class="text-gray-400 text-xs">{{ row.accuseUser.username }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="举报类型" width="120">
          <template #default="{ row }">
            <el-tag
              :type="
                row.type === '涉黄涉政' ? 'danger' : row.type === '抄袭侵权' ? 'warning' : 'info'
              "
            >
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="举报详情" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="处理状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleUserId" label="处理人" width="180">
          <template #default="{ row }">
            <div v-if="row.handleUserName" class="flex items-center">
              <span>{{ row.handleUserName }}</span>
              <el-tag
                class="ml-2"
                :type="roleTypeMap[row.handleUserRole]?.type || 'info'"
                size="small"
              >
                {{ roleTypeMap[row.handleUserRole]?.label || row.handleUserRole }}
              </el-tag>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="举报时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" :icon="View" @click="handleDetail(row)">
              详情
            </el-button>
            <el-button
              v-if="row.status === 0 || row.status === 1"
              link
              type="primary"
              :icon="Check"
              @click="openHandleDialog(row)"
              v-hasPermi="['blog:accuse:update']"
            >
              处理
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

    <!-- 处理对话框 -->
    <el-dialog v-model="handleDialogVisible" title="处理举报" width="500px">
      <el-form :model="handleForm" label-width="120px">
        <el-form-item label="处理方式" required>
          <el-select v-model="handleForm.processWay" placeholder="请选择处理方式">
            <el-option
              v-for="item in processWayOptions"
              :key="item.value"
              :label="item.label"
              :value="Number(item.value)"
            />
          </el-select>
        </el-form-item>

        <!-- 仅在选择违规时显示以下选项 -->
        <template v-if="handleForm.processWay === 1 || handleForm.processWay === 2">
          <el-form-item
            label="修改用户角色"
            v-if="
              detailInfo?.accusedRole !== 'super_admin' &&
              roleOptions.length > 0
            "
          >
            <el-select 
              v-model="handleForm.roleCode" 
              placeholder="请选择要修改的角色"
              clearable
            >
              <el-option
                v-for="item in roleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <div class="form-tip">
              仅当需要降低用户角色时选择，不修改请留空
            </div>
          </el-form-item>

          <el-form-item label="是否禁用账号" v-if="detailInfo?.accusedRole !== 'super_admin'">
            <el-switch v-model="handleForm.isBlock" />
          </el-form-item>

          <el-form-item label="博客状态">
            <el-radio-group v-model="handleForm.blogStatus">
              <el-radio :label="1">下架</el-radio>
              <el-radio :label="2">删除</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <!-- 添加扣分字段 -->
          <el-form-item label="扣除信誉分">
            <el-input-number 
              v-model="handleForm.score" 
              :min="0" 
              :max="20" 
              :step="1"
              controls-position="right"
            />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="举报详情" width="800px">
      <template v-if="detailInfo">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="举报ID" :span="2">{{ detailInfo.id }}</el-descriptions-item>
          <el-descriptions-item label="举报类型">
            <el-tag
              :type="
                detailInfo.type === '涉黄涉政'
                  ? 'danger'
                  : detailInfo.type === '抄袭侵权'
                    ? 'warning'
                    : 'info'
              "
            >
              {{ detailInfo.type }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="举报时间">
            {{ formatDate(detailInfo.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="举报内容" :span="2"
            >{{ detailInfo.content }}
          </el-descriptions-item>

          <!-- 举报人信息 -->
          <el-descriptions-item label="举报人信息" :span="2">
            <el-card shadow="never" class="user-info-card">
              <div class="flex items-center">
                <el-avatar :size="64" :src="detailInfo.accuseUser.avatar">
                  {{ detailInfo.accuseUser.nickname?.charAt(0) }}
                </el-avatar>
                <div class="ml-4 flex-1">
                  <div class="flex items-center justify-between">
                    <div>
                      <span class="text-lg font-bold">{{ detailInfo.accuseUser.nickname }}</span>
                      <span class="text-gray-400 ml-2">({{ detailInfo.accuseUser.username }})</span>
                      <el-tag
                        class="ml-2"
                        :type="roleTypeMap[detailInfo.accuseRole]?.type || 'info'"
                        size="small"
                      >
                        {{ roleTypeMap[detailInfo.accuseRole]?.label || detailInfo.accuseRole }}
                      </el-tag>
                    </div>
                    <el-tag :type="detailInfo.accuseUser.status === 0 ? 'success' : 'danger'">
                      {{ detailInfo.accuseUser.status === 0 ? '正常' : '禁用' }}
                    </el-tag>
                  </div>
                  <div class="mt-2 text-gray-500">
                    <span class="mr-4">信誉分：{{ detailInfo.accuseUser.reputation }}</span>
                    <span class="mr-4">博客数：{{ detailInfo.accuseUser.blogCount }}</span>
                    <span>获赞数：{{ detailInfo.accuseUser.likeCount }}</span>
                  </div>
                  <div class="mt-2 text-gray-500">
                    <span class="mr-4">邮箱：{{ detailInfo.accuseUser.email }}</span>
                    <span>手机：{{ detailInfo.accuseUser.mobile }}</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-descriptions-item>

          <!-- 被举报人信息 -->
          <el-descriptions-item label="被举报人信息" :span="2">
            <el-card shadow="never" class="user-info-card">
              <div class="flex items-center">
                <el-avatar :size="64" :src="detailInfo.accusedUser.avatar">
                  {{ detailInfo.accusedUser.nickname?.charAt(0) }}
                </el-avatar>
                <div class="ml-4 flex-1">
                  <div class="flex items-center justify-between">
                    <div>
                      <span class="text-lg font-bold">{{ detailInfo.accusedUser.nickname }}</span>
                      <span class="text-gray-400 ml-2"
                        >({{ detailInfo.accusedUser.username }})</span
                      >
                      <el-tag
                        class="ml-2"
                        :type="roleTypeMap[detailInfo.accusedRole]?.type || 'info'"
                        size="small"
                      >
                        {{ roleTypeMap[detailInfo.accusedRole]?.label || detailInfo.accusedRole }}
                      </el-tag>
                    </div>
                    <el-tag :type="detailInfo.accusedUser.status === 0 ? 'success' : 'danger'">
                      {{ detailInfo.accusedUser.status === 0 ? '正常' : '禁用' }}
                    </el-tag>
                  </div>
                  <div class="mt-2 text-gray-500">
                    <span class="mr-4">信誉分：{{ detailInfo.accusedUser.reputation }}</span>
                    <span class="mr-4">博客数：{{ detailInfo.accusedUser.blogCount }}</span>
                    <span>获赞数：{{ detailInfo.accusedUser.likeCount }}</span>
                  </div>
                  <div class="mt-2 text-gray-500">
                    <span class="mr-4">邮箱：{{ detailInfo.accusedUser.email }}</span>
                    <span>手机：{{ detailInfo.accusedUser.mobile }}</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-descriptions-item>

          <!-- 处理信息 -->
          <el-descriptions-item label="处理状态">
            <el-tag :type="detailInfo.status === 1 ? 'success' : 'info'">
              {{ detailInfo.status === 1 ? '已处理' : '待处理' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理人">
            <template v-if="detailInfo.handleUserName">
              <span>{{ detailInfo.handleUserName }}</span>
              <el-tag
                class="ml-2"
                :type="roleTypeMap[detailInfo.handleUserRole]?.type || 'info'"
                size="small"
              >
                {{ roleTypeMap[detailInfo.handleUserRole]?.label || detailInfo.handleUserRole }}
              </el-tag>
            </template>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="处理时间">
            {{ detailInfo.handleTime ? formatDate(detailInfo.handleTime) : '-' }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="mt-4 flex justify-center">
          <el-button type="primary" @click="router.push(`/blog/detail/${detailInfo.postId}`)">
            查看被举报博客
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
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

.user-info-card {
  :deep(.el-card__body) {
    padding: 16px;
  }
}

.text-gray-400 {
  color: #9ca3af;
}

.text-gray-500 {
  color: #6b7280;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  line-height: 1.4;
}
</style>
