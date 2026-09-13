<!-- dall3 -->
<template>
  <div class="prompt">
    <el-text tag="b">画面描述</el-text>
    <el-text tag="p">建议使用"形容词 + 动词 + 风格"的格式，使用"，"隔开</el-text>
    <el-input
      v-model="prompt"
      maxlength="1024"
      :rows="5"
      class="w-100% mt-15px"
      input-style="border-radius: 7px;"
      placeholder="例如：童话里的小屋应该是什么样子？"
      show-word-limit
      type="textarea"
    />
  </div>
  <div class="hot-words">
    <div>
      <el-text tag="b">随机热词</el-text>
    </div>
    <el-space wrap class="word-list">
      <el-button
        round
        class="btn"
        :type="selectHotWord === hotWord ? 'primary' : 'default'"
        v-for="hotWord in ImageHotEnglishWords"
        :key="hotWord"
        @click="handleHotWordClick(hotWord)"
      >
        {{ hotWord }}
      </el-button>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">采样方法</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-select v-model="sampler" placeholder="Select" size="large" class="!w-350px">
        <el-option
          v-for="item in StableDiffusionSamplers"
          :key="item.key"
          :label="item.name"
          :value="item.key"
        />
      </el-select>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">CLIP</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-select v-model="clipGuidancePreset" placeholder="Select" size="large" class="!w-350px">
        <el-option
          v-for="item in StableDiffusionClipGuidancePresets"
          :key="item.key"
          :label="item.name"
          :value="item.key"
        />
      </el-select>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">风格</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-select v-model="stylePreset" placeholder="Select" size="large" class="!w-350px">
        <el-option
          v-for="item in StableDiffusionStylePresets"
          :key="item.key"
          :label="item.name"
          :value="item.key"
        />
      </el-select>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">图片尺寸</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-input v-model="width" class="w-170px" placeholder="图片宽度" />
      <el-input v-model="height" class="w-170px" placeholder="图片高度" />
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">迭代步数</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-input
        v-model="steps"
        type="number"
        size="large"
        class="!w-350px"
        placeholder="Please input"
      />
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">引导系数</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-input
        v-model="scale"
        type="number"
        size="large"
        class="!w-350px"
        placeholder="Please input"
      />
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">随机因子</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-input
        v-model="seed"
        type="number"
        size="large"
        class="!w-350px"
        placeholder="Please input"
      />
    </el-space>
  </div>
  <div class="btns">
    <el-button
      type="primary"
      size="large"
      round
      :loading="drawIn"
      :disabled="prompt.length === 0"
      @click="handleGenerateImage"
    >
      {{ drawIn ? '生成中' : '生成内容' }}
    </el-button>
  </div>

  <!-- 知识文档选择按钮 -->
  <div class="knowledge-actions mt-10px">
    <el-button type="info" plain @click="showKnowledgeDialog = true">
      <el-icon><Document /></el-icon>
      选择知识文档
    </el-button>
  </div>

  <!-- 知识文档选择弹窗 -->
  <el-dialog 
    v-model="showKnowledgeDialog" 
    title="选择知识文档" 
    width="80%" 
    :before-close="handleKnowledgeDialogClose"
  >
    <div class="knowledge-dialog-content">
      <!-- 搜索栏 -->
      <div class="search-section mb-20px">
        <el-input
          v-model="knowledgeSearchName"
          placeholder="输入知识库名称搜索"
          class="search-input"
          clearable
          @clear="handleKnowledgeSearch"
          @keyup.enter="handleKnowledgeSearch"
        >
          <template #append>
            <el-button @click="handleKnowledgeSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
      
      <!-- 文档列表 -->
      <div class="knowledge-list" v-loading="knowledgeLoading">
        <el-table 
          :data="knowledgeDocuments" 
          stripe 
          height="400"
          @row-click="handleKnowledgeRowClick"
          highlight-current-row
        >
          <el-table-column prop="name" label="文档名称" min-width="200">
            <template #default="{ row }">
              <div class="document-name">
                <el-icon><Document /></el-icon>
                <span class="ml-5px">{{ row.name }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="content" label="内容预览" min-width="300">
            <template #default="{ row }">
              <div class="content-preview">
                {{ getContentPreview(row.content) }}
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="contentLength" label="内容长度" width="120">
            <template #default="{ row }">
              <el-tag size="small">{{ formatContentLength(row.contentLength) }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="tokens" label="Token数" width="100">
            <template #default="{ row }">
              <el-tag type="info" size="small">{{ row.tokens }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="retrievalCount" label="召回次数" width="100">
            <template #default="{ row }">
              <el-tag type="warning" size="small">{{ row.retrievalCount }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small" 
                @click.stop="handleSelectKnowledgeDocument(row)"
              >
                选择
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 分页 -->
      <div class="pagination-section mt-20px">
        <el-pagination
          v-model:current-page="knowledgePagination.pageNo"
          v-model:page-size="knowledgePagination.pageSize"
          :total="knowledgePagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleKnowledgePageSizeChange"
          @current-change="handleKnowledgePageChange"
        />
      </div>
    </div>
    
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="showKnowledgeDialog = false">取消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ImageApi, ImageDrawReqVO, ImageVO } from '@/api/ai/image'
import { hasChinese } from '@/views/ai/utils/utils'
import {
  AiPlatformEnum,
  ImageHotEnglishWords,
  StableDiffusionClipGuidancePresets,
  StableDiffusionSamplers,
  StableDiffusionStylePresets
} from '@/views/ai/utils/constants'
import { ModelVO } from '@/api/ai/model/model'
import { KnowledgeDocumentApi, KnowledgeDocumentVO } from '@/api/ai/knowledge/document'
import { Document } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/formatTime'

const message = useMessage() // 消息弹窗

// 接收父组件传入的模型列表
const props = defineProps({
  models: {
    type: Array<ModelVO>,
    default: () => [] as ModelVO[]
  }
})
const emits = defineEmits(['onDrawStart', 'onDrawComplete']) // 定义 emits

// 定义属性
const drawIn = ref<boolean>(false) // 生成中
const selectHotWord = ref<string>('') // 选中的热词
// 表单
const prompt = ref<string>('') // 提示词
const width = ref<number>(512) // 图片宽度
const height = ref<number>(512) // 图片高度
const sampler = ref<string>('DDIM') // 采样方法
const steps = ref<number>(20) // 迭代步数
const seed = ref<number>(42) // 控制生成图像的随机性
const scale = ref<number>(7.5) // 引导系数
const clipGuidancePreset = ref<string>('NONE') // 文本提示相匹配的图像(clip_guidance_preset) 简称 CLIP
const stylePreset = ref<string>('3d-model') // 风格

// 知识文档相关
const showKnowledgeDialog = ref<boolean>(false) // 知识文档选择弹窗
const knowledgeSearchName = ref<string>('') // 知识搜索名称
const knowledgeLoading = ref<boolean>(false) // 知识加载中
const knowledgeDocuments = ref<KnowledgeDocumentVO[]>([]) // 知识文档列表
const knowledgePagination = ref({ pageNo: 1, pageSize: 10, total: 0 }) // 知识分页

/** 获取知识文档分页数据 */
const getKnowledgeDocuments = async () => {
  try {
    knowledgeLoading.value = true
    const params = {
      name: knowledgeSearchName.value || undefined,
      pageNo: knowledgePagination.value.pageNo,
      pageSize: knowledgePagination.value.pageSize
    }
    
    const res = await KnowledgeDocumentApi.getKnowledgeDocumentPageSelf(params)
    knowledgeDocuments.value = res.list || []
    knowledgePagination.value.total = res.total || 0
  } catch (error) {
    console.error('获取知识文档失败:', error)
    message.error('获取知识文档失败')
  } finally {
    knowledgeLoading.value = false
  }
}

/** 知识文档相关操作 */
const handleKnowledgeDialogClose = () => {
  showKnowledgeDialog.value = false
}

const handleKnowledgeSearch = async () => {
  knowledgePagination.value.pageNo = 1
  getKnowledgeDocuments()
}

const handleKnowledgeRowClick = (row: KnowledgeDocumentVO) => {
  // 可以在这里添加行点击的逻辑
}

const handleKnowledgePageSizeChange = (pageSize: number) => {
  knowledgePagination.value.pageSize = pageSize
  knowledgePagination.value.pageNo = 1
  getKnowledgeDocuments()
}

const handleKnowledgePageChange = (pageNo: number) => {
  knowledgePagination.value.pageNo = pageNo
  getKnowledgeDocuments()
}

const handleSelectKnowledgeDocument = (document: KnowledgeDocumentVO) => {
  // 获取文档内容并截取前1024个字符
  const content = document.content || ''
  const truncatedContent = content.substring(0, 1024)
  
  // 直接将截取的内容赋值到输入框
  prompt.value = truncatedContent
  
  showKnowledgeDialog.value = false
  message.success(`已选择文档：${document.name}，内容已导入（${truncatedContent.length}/${content.length}字符）`)
}

/** 内容预览方法 */
const getContentPreview = (content: string): string => {
  if (!content) return '暂无内容'
  return content.length > 50 ? content.substring(0, 50) + '...' : content
}

/** 格式化内容长度 */
const formatContentLength = (length: number): string => {
  if (length < 1000) return `${length}字符`
  if (length < 1000000) return `${(length / 1000).toFixed(1)}K`
  return `${(length / 1000000).toFixed(1)}M`
}

/** 格式化日期时间 */
const formatDateTime = (dateTime: string | number) => {
  if (!dateTime) return '-'
  
  let date: Date
  if (typeof dateTime === 'number') {
    date = new Date(dateTime)
  } else {
    date = new Date(dateTime)
  }
  
  return formatDate(date, 'YYYY-MM-DD HH:mm')
}

/** 监听知识文档弹窗显示状态 */
watch(showKnowledgeDialog, (newVal) => {
  if (newVal) {
    getKnowledgeDocuments()
  }
})

/** 选择热词 */
const handleHotWordClick = async (hotWord: string) => {
  // 情况一：取消选中
  if (selectHotWord.value == hotWord) {
    selectHotWord.value = ''
    return
  }

  // 情况二：选中
  selectHotWord.value = hotWord // 选中
  prompt.value = hotWord // 替换提示词
}

/** 图片生成 */
const handleGenerateImage = async () => {
  // 从 models 中查找匹配的模型
  const selectModel = 'stable-diffusion-v1-6'
  const matchedModel = props.models.find(
    (item) => item.model === selectModel && item.platform === AiPlatformEnum.STABLE_DIFFUSION
  )
  if (!matchedModel) {
    message.error('该模型不可用，请选择其它模型')
    return
  }

  // 二次确认
  if (hasChinese(prompt.value)) {
    message.alert('暂不支持中文！')
    return
  }
  await message.confirm(`确认生成内容?`)

  try {
    // 加载中
    drawIn.value = true
    // 回调
    emits('onDrawStart', AiPlatformEnum.STABLE_DIFFUSION)
    // 发送请求
    const form = {
      modelId: matchedModel.id,
      prompt: prompt.value, // 提示词
      width: width.value, // 图片宽度
      height: height.value, // 图片高度
      style: '', // 添加缺少的style属性
      options: {
        seed: seed.value, // 随机种子
        steps: steps.value, // 图片生成步数
        scale: scale.value, // 引导系数
        sampler: sampler.value, // 采样算法
        clipGuidancePreset: clipGuidancePreset.value, // 文本提示相匹配的图像 CLIP
        stylePreset: stylePreset.value // 风格
      }
    } as ImageDrawReqVO
    await ImageApi.drawImage(form)
  } finally {
    // 回调
    emits('onDrawComplete', AiPlatformEnum.STABLE_DIFFUSION)
    // 加载结束
    drawIn.value = false
  }
}

/** 填充值 */
const settingValues = async (detail: ImageVO) => {
  prompt.value = detail.prompt
  width.value = detail.width
  height.value = detail.height
  seed.value = detail.options?.seed
  steps.value = detail.options?.steps
  scale.value = detail.options?.scale
  sampler.value = detail.options?.sampler
  clipGuidancePreset.value = detail.options?.clipGuidancePreset
  stylePreset.value = detail.options?.stylePreset
}

/** 暴露组件方法 */
defineExpose({ settingValues })
</script>
<style scoped lang="scss">
// 提示词
.prompt {
}

// 热词
.hot-words {
  display: flex;
  flex-direction: column;
  margin-top: 30px;

  .word-list {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: start;
    margin-top: 15px;

    .btn {
      margin: 0;
    }
  }
}

// 模型
.group-item {
  margin-top: 30px;

  .group-item-body {
    margin-top: 15px;
    width: 100%;
  }
}

.btns {
  display: flex;
  justify-content: center;
  margin-top: 50px;
}

.knowledge-actions {
  margin-top: 10px;
  text-align: right;
}

.knowledge-dialog-content {
  .search-section {
    margin-bottom: 20px;
  }
  
  .search-input {
    width: 100%;
  }
  
  .knowledge-list {
    height: 400px;
    overflow-y: auto;
  }
  
  .document-name {
    display: flex;
    align-items: center;
  }
  
  .content-preview {
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .pagination-section {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
