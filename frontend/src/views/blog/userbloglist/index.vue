<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, View, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { formatDate } from '@/utils/formatTime'
import { BlogApi } from '@/api/blog/blog'
import { CategoryApi } from '@/api/blog/category'
import { TagApi } from '@/api/blog/tag'

const router = useRouter()

// 定义接口类型
interface User {
  id: number
  username: string
  role: string
}

interface Category {
  id: number
  name: string
  parentId: number
  children?: Category[]
}

interface Tag {
  id: number
  name: string
}

interface BlogPost {
  id: number
  userId: number
  title: string
  content: string
  status: number
  isTop: number
  topTime: string | null
  visibility: number
  viewCount: number
  likeCount: number
  auditUserId: number | null
  auditTime: string | null
  auditComment: string
  createTime: string
  updateTime: string
  author: User
  category: Category
  tags: Tag[]
  frontCover: string
}

// 在 interface 和变量定义部分添加日期范围变量
interface QueryParams {
  pageNo: number
  pageSize: number
  keyword: string
  categoryId?: number
  tagId?: number
  status?: number
  isTop?: number
  visibility?: number
  startTime?: string
  endTime?: string
}

// 列表数据
const loading = ref(false)
const total = ref(0)
const dataList = ref<BlogPost[]>([])

// 查询条件
const queryParams = reactive<QueryParams>({
  pageNo: 1,
  pageSize: 10,
  keyword: '',
  categoryId: undefined,
  tagId: undefined,
  status: undefined,
  isTop: undefined,
  visibility: undefined,
  startTime: undefined,
  endTime: undefined
})

// 添加日期范围变量
const dateRange = ref<[Date | undefined, Date | undefined]>([undefined, undefined])

// 监听日期范围变化
watch(dateRange, (val) => {
  if (val && val[0] && val[1]) {
    queryParams.startTime = formatDate(val[0])
    queryParams.endTime = formatDate(val[1])
  } else {
    queryParams.startTime = undefined
    queryParams.endTime = undefined
  }
})

// 分类和标签数据
const categoryOptions = ref<Category[]>([])
const tagOptions = ref<Tag[]>([])

// 状态选项
const statusOptions = [
  { label: '草稿', value: 0 },
  { label: '待审核', value: 1 },
  { label: '已发布', value: 2 },
  { label: '已下架', value: 3 }
]

// 可见性选项
const visibilityOptions = [
  { label: '公开', value: 0 },
  { label: '仅自己', value: 1 },
  { label: '粉丝可见', value: 2 }
]

// 获取列表数据，使用getUserBlogPage方法
const getList = async () => {
  loading.value = true
  try {
    const res = await BlogApi.getUserBlogPage({
      ...queryParams,
      startTime: queryParams.startTime,
      endTime: queryParams.endTime
    })

    if (res) {
      console.log('获取的个人博客列表', res)
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

// 获取分类树
const getCategoryTree = async () => {
  try {
    const res = await CategoryApi.getCategoryList({})
    if (res) {
      // 将扁平数组转换为树形结构
      const treeData = buildCategoryTree(res)
      categoryOptions.value = treeData
    } else {
      ElMessage.error('获取分类失败')
    }
  } catch (error) {
    console.error('获取分类失败:', error)
    ElMessage.error('获取分类失败')
  }
}

// 构建分类树的方法
const buildCategoryTree = (categories: Category[]): Category[] => {
  // 先找出所有顶级分类（parentId === 0）
  const rootCategories = categories.filter(cat => cat.parentId === 0)
  
  // 递归构建子分类
  const buildChildren = (parentId: number): Category[] => {
    return categories
      .filter(cat => cat.parentId === parentId)
      .map(cat => ({
        ...cat,
        children: buildChildren(cat.id)
      }))
  }

  // 为每个顶级分类构建子树
  return rootCategories.map(root => ({
    ...root,
    children: buildChildren(root.id)
  }))
}

// 获取标签列表
const getTagList = async () => {
  try {
    const res = await TagApi.getTagList({})
    if (res) {
      // 直接使用返回的标签数组
      tagOptions.value = res.map((tag) => ({
        id: tag.id,
        name: tag.name
      }))
    } else {
      ElMessage.error('获取标签失败')
    }
  } catch (error) {
    console.error('获取标签失败:', error)
    ElMessage.error('获取标签失败')
  }
}

// 重置查询
const resetQuery = () => {
  Object.keys(queryParams).forEach((key) => {
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

// 新增博客
const handleAdd = () => {
  router.push({
    path: '/content/add',
    query: {
      type: 'add'
    }
  })
}

// 查看博客方法
const handleView = (row: BlogPost) => {
  if (row.id) {
    router.push(`/blog/detail/${row.id}`)
  }
}

// 编辑博客方法
const handleEdit = async (row: BlogPost) => {
  try {
    // 获取博客详情数据
    const res = await BlogApi.getBlogDetail(row.id)
    if (res) {
      // 跳转到编辑页面
      router.push({
        path: '/content/add',
        query: {
          id: row.id.toString(),
          type: 'edit'
        }
      })
    }
  } catch (error) {
    console.error('获取博客详情失败:', error)
    ElMessage.error('获取博客详情失败')
  }
}

// 删除博客
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认要删除该博客吗？', '提示', {
      type: 'warning'
    })
    const res = await BlogApi.deleteUserBlog(row.id)
    if (res) {
      ElMessage.success('删除成功')
      await getList()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

// 修改可见性
const handleVisibility = async (row: any, visibility: number) => {
  try {
    const res = await BlogApi.updateBlogVisibilityUser(row.id, {
      visibility: visibility
    })
    if (res) {
      ElMessage.success('修改可见性成功')
      await getList()
    } else {
      ElMessage.error('修改失败')
    }
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error('修改失败')
  }
}

onMounted(() => {
  getCategoryTree()
  getTagList()
  getList()
})
</script>

<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="mb-4 !border-none" shadow="never">
      <el-form ref="queryFormRef" :model="queryParams" class="search-form">
        <!-- 第一行 -->
        <el-form-item label="关键词" prop="keyword">
          <el-input
            v-model="queryParams.keyword"
            placeholder="标题关键词"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-tree-select
            v-model="queryParams.categoryId"
            :data="categoryOptions"
            :props="{
              value: 'id',
              label: 'name',
              children: 'children',
              checkStrictly: true,
              emitPath: false
            }"
            placeholder="请选择分类"
            clearable
            filterable
          />
        </el-form-item>
        <el-form-item label="标签" prop="tagId">
          <el-select
            v-model="queryParams.tagId"
            placeholder="请选择标签"
            clearable
            filterable
            collapse-tags
            collapse-tags-tooltip
          >
            <el-option
              v-for="item in tagOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <!-- 第二行 -->
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
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="可见性" prop="visibility">
          <el-select v-model="queryParams.visibility" placeholder="请选择可见性" clearable>
            <el-option
              v-for="item in visibilityOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <!-- 按钮区域 -->
        <div class="search-buttons">
          <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
          <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="mb-4 !border-none" shadow="never">
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增博客</el-button>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="!border-none" shadow="never">
      <el-table v-loading="loading" :data="dataList" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <div class="flex items-center">
              <el-tag v-if="row.isTop" type="danger" effect="dark" size="small" class="mr-2">置顶</el-tag>
              {{ row.title }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category.name" label="分类" width="120" />
        <el-table-column label="标签" width="200">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tags" :key="tag.id" size="small" class="mr-1">
              {{ tag.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="阅读量" width="100" align="center" />
        <el-table-column prop="likeCount" label="点赞数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 2 ? 'success' : row.status === 1 ? 'warning' : 'info'">
              {{ statusOptions.find((item) => item.value === row.status)?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="visibility" label="可见性" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.visibility === 0 ? 'success' : 'warning'">
              {{ visibilityOptions.find((item) => item.value === row.visibility)?.label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="operation-buttons">
              <el-button link type="primary" :icon="View" @click="handleView(row)">查看</el-button>
              <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
              <el-dropdown trigger="click">
                <el-button link type="primary">
                  更多
                  <el-icon class="el-icon--right">
                    <arrow-down />
                  </el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :icon="Lock" divided>
                      <el-dropdown trigger="hover" placement="right-start">
                        <span>修改可见性</span>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item
                              v-for="item in visibilityOptions"
                              :key="item.value"
                              @click="handleVisibility(row, item.value)"
                            >
                              {{ item.label }}
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </el-dropdown-item>
                    <el-dropdown-item :icon="Delete" divided @click="handleDelete(row)">
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
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
.search-form :deep(.el-tree-select),
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
  .search-form :deep(.el-tree-select),
  .search-form :deep(.el-date-picker),
  .search-form :deep(.el-input) {
    width: 100%;
  }

  .search-form .search-buttons {
    justify-content: flex-start;
  }
}

/* 添加操作按钮样式 */
.operation-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

.operation-buttons :deep(.el-dropdown) {
  margin-left: 8px;
}

/* 修改嵌套下拉菜单样式 */
:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}

:deep(.el-dropdown-menu__item .el-icon) {
  margin-right: 0;
}

:deep(.el-dropdown [class*='el-icon--right']) {
  margin-left: 4px;
}
</style>
