<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="链接地址" prop="link">
        <el-input
          v-model="queryParams.link"
          placeholder="请输入链接地址"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="排序号" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
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
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['blog:carousel:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['blog:carousel:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="轮播图ID" align="center" prop="id" />
      <el-table-column label="图片" align="center" width="180">
        <template #default="{ row }">
          <div class="image-preview">
            <el-image
              v-if="row.image"
              :src="row.image"
              fit="cover"
              class="carousel-thumb"
              :preview-src-list="[row.image]"
              :initial-index="0"
              preview-teleported
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                  <span>暂无图片</span>
                </div>
              </template>
            </el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="描述" align="center" min-width="200">
        <template #default="{ row }">
          <div class="description-preview">
            <el-popover
              placement="right"
              trigger="hover"
              :width="400"
              popper-class="description-popover"
            >
              <template #reference>
                <div class="description-content">
                  <div 
                    v-html="row.description || '暂无描述'"
                  ></div>
                </div>
              </template>
              <template #default>
                <div class="description-full">
                  <div class="description-header">
                    <span class="title">{{ row.title }}</span>
                    <el-tag size="small" type="info" class="time">
                      {{ dateFormatter(row.createTime) }}
                    </el-tag>
                  </div>
                  <el-divider />
                  <div 
                    class="description-body rich-text-content" 
                    v-html="row.description || '暂无描述'"
                  ></div>
                  <div v-if="row.link" class="description-footer">
                    <el-link type="primary" :href="row.link" target="_blank">
                      <el-icon class="mr-5px"><Link /></el-icon>
                      查看链接
                    </el-link>
                  </div>
                </div>
              </template>
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="链接地址" align="center" prop="link" />
      <el-table-column label="排序号" align="center" prop="sort" />
      <el-table-column label="状态：0-禁用 1-启用" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BLOG_USER_LOCK" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['blog:carousel:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['blog:carousel:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <CarouselForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { CarouselApi, CarouselVO } from '@/api/blog/carousel'
import CarouselForm from './CarouselForm.vue'
import { Picture, Link } from '@element-plus/icons-vue'

/** 轮播图 列表 */
defineOptions({ name: 'Carousel' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<CarouselVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  image: undefined,
  title: undefined,
  description: undefined,
  link: undefined,
  sort: undefined,
  status: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CarouselApi.getCarouselPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await CarouselApi.deleteCarousel(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CarouselApi.exportCarousel(queryParams)
    download.excel(data, '轮播图.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>
.image-preview {
  display: flex;
  justify-content: center;
}

.carousel-thumb {
  width: 150px;
  height: 80px;
  border-radius: 4px;
  cursor: pointer;
  object-fit: cover;
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
}

.carousel-thumb:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.image-error {
  width: 150px;
  height: 80px;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #909399;
  font-size: 12px;
  border-radius: 4px;
  border: 1px dashed #d9d9d9;
}

.image-error .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

/* 优化表格内容的显示 */
:deep(.el-table .cell) {
  padding: 8px;
}

:deep(.el-table td) {
  padding: 8px 0;
}

/* 描述预览相关样式 */
.description-preview {
  padding: 8px;
}

.description-content {
  padding: 8px;
  max-height: 100px;
  overflow: hidden;
  position: relative;
}

.rich-text-preview {
  line-height: 1.5;
  max-height: 80px;
  overflow: hidden;
  position: relative;
}

/* 添加渐变遮罩 */
.rich-text-preview::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 40px;
  background: linear-gradient(transparent, white);
}

/* 富文本内容基础样式 */
:deep(.rich-text-preview),
:deep(.rich-text-content) {
  h1, h2, h3, h4, h5, h6 {
    margin: 8px 0;
    font-weight: bold;
  }

  p {
    margin: 8px 0;
    line-height: 1.6;
  }

  img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 8px 0;
  }

  a {
    color: var(--el-color-primary);
    text-decoration: none;
    &:hover {
      text-decoration: underline;
    }
  }

  ul, ol {
    padding-left: 20px;
    margin: 8px 0;
  }

  blockquote {
    margin: 8px 0;
    padding: 8px 16px;
    border-left: 4px solid var(--el-color-primary-light-7);
    background-color: var(--el-color-primary-light-9);
    color: #666;
  }

  code {
    background-color: #f5f7fa;
    padding: 2px 4px;
    border-radius: 3px;
    font-family: monospace;
  }

  table {
    border-collapse: collapse;
    width: 100%;
    margin: 8px 0;
    
    th, td {
      border: 1px solid #dcdfe6;
      padding: 8px;
      text-align: left;
    }
    
    th {
      background-color: #f5f7fa;
    }
  }
}

/* 弹出框中的富文本内容特殊样式 */
:deep(.rich-text-content) {
  max-height: 400px;
  overflow-y: auto;
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;
}

/* 自定义滚动条 */
:deep(.rich-text-content)::-webkit-scrollbar {
  width: 6px;
}

:deep(.rich-text-content)::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 3px;
}

:deep(.rich-text-content)::-webkit-scrollbar-track {
  background: #f5f7fa;
}

.description-full {
  padding: 8px;
}

.description-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.description-header .title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.description-header .time {
  font-size: 12px;
}

.description-body {
  line-height: 1.6;
  color: #606266;
  margin: 12px 0;
  max-height: 300px;
  overflow-y: auto;
}

.description-footer {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

/* 自定义弹出框样式 */
:deep(.description-popover) {
  max-width: 500px;
  padding: 16px;
}

:deep(.el-popover.description-popover) {
  padding: 16px;
}

/* 滚动条美化 */
.description-body::-webkit-scrollbar {
  width: 6px;
}

.description-body::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 3px;
}

.description-body::-webkit-scrollbar-track {
  background: #f5f7fa;
}
</style>
