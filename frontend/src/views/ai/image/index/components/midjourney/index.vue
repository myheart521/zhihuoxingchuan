<!-- dall3 -->
<template>
  <div class="prompt">
    <el-text tag="b">画面描述</el-text>
    <el-text tag="p">建议使用"人物特征+表情状态+服装风格+画质要求+艺术风格"的格式，使用"，"隔开。</el-text>
    
    <!-- 提示词工程生成器 -->
    <div class="prompt-generator">
      <el-button type="primary" plain @click="showPromptGenerator = !showPromptGenerator">
        {{ showPromptGenerator ? '收起' : '打开' }}提示词生成器
      </el-button>
      
      <div v-show="showPromptGenerator" class="generator-content">
        <el-row :gutter="20" class="mt-15px">
          <el-col :span="6">
            <div class="generator-section">
              <el-text tag="b" class="section-title">人物类型</el-text>
              <el-select v-model="promptConfig.character" placeholder="选择人物类型" class="w-full mt-10px">
                <el-option label="优秀青年" value="一位优秀青年"/>
                <el-option label="劳动者" value="一位辛勤的劳动者"/>
                <el-option label="医护人员" value="一位白衣天使"/>
                <el-option label="教师" value="一位知识渊博的教师"/>
                <el-option label="科技工作者" value="一位专业的科技工作者"/>
                <el-option label="志愿者" value="一位热心的志愿者"/>
                <el-option label="学者" value="一位睿智的学者"/>
                <el-option label="艺术工作者" value="一位才华横溢的艺术工作者"/>
              </el-select>
            </div>
          </el-col>
          
          <el-col :span="6">
            <div class="generator-section">
              <el-text tag="b" class="section-title">表情状态</el-text>
              <el-select v-model="promptConfig.expression" placeholder="选择表情" class="w-full mt-10px">
                <el-option label="积极向上" value="积极向上的表情"/>
                <el-option label="专注认真" value="专注认真的表情"/>
                <el-option label="温和慈祥" value="温和慈祥的笑容"/>
                <el-option label="智慧深邃" value="智慧深邃的眼神"/>
                <el-option label="坚定自信" value="坚定自信的神态"/>
                <el-option label="亲切和蔼" value="亲切和蔼的微笑"/>
                <el-option label="沉着冷静" value="沉着冷静的表情"/>
              </el-select>
            </div>
          </el-col>
          
          <el-col :span="6">
            <div class="generator-section">
              <el-text tag="b" class="section-title">服装风格</el-text>
              <el-select v-model="promptConfig.clothing" placeholder="选择服装" class="w-full mt-10px">
                <el-option label="现代正装" value="现代正装"/>
                <el-option label="工作服装" value="专业工作服装"/>
                <el-option label="医护制服" value="整洁的医护制服"/>
                <el-option label="学者装束" value="知识分子装束"/>
                <el-option label="休闲装" value="得体的休闲装"/>
                <el-option label="制服" value="统一制服"/>
                <el-option label="传统服装" value="端庄的传统服装"/>
              </el-select>
            </div>
          </el-col>
          
          <el-col :span="6">
            <div class="generator-section">
              <el-text tag="b" class="section-title">艺术风格</el-text>
              <el-select v-model="promptConfig.style" placeholder="选择风格" class="w-full mt-10px">
                <el-option label="肖像摄影" value="肖像摄影风格"/>
                <el-option label="写实绘画" value="写实绘画风格"/>
                <el-option label="油画风格" value="经典油画风格"/>
                <el-option label="素描风格" value="精细素描风格"/>
                <el-option label="水彩风格" value="温和水彩风格"/>
                <el-option label="国画风格" value="传统国画风格"/>
                <el-option label="现代插画" value="现代插画风格"/>
              </el-select>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="mt-15px">
          <el-col :span="8">
            <div class="generator-section">
              <el-text tag="b" class="section-title">画质要求</el-text>
              <el-select v-model="promptConfig.quality" placeholder="选择画质" class="w-full mt-10px">
                <el-option label="高清画质" value="高清画质"/>
                <el-option label="4K超清" value="4K超清画质"/>
                <el-option label="专业摄影" value="专业摄影画质"/>
                <el-option label="艺术级别" value="艺术级别画质"/>
              </el-select>
            </div>
          </el-col>
          
          <el-col :span="8">
            <div class="generator-section">
              <el-text tag="b" class="section-title">光线效果</el-text>
              <el-select v-model="promptConfig.lighting" placeholder="选择光线" class="w-full mt-10px">
                <el-option label="自然光线" value="自然光线"/>
                <el-option label="温暖光线" value="温暖光线"/>
                <el-option label="柔和光线" value="柔和光线"/>
                <el-option label="专业布光" value="专业布光"/>
                <el-option label="黄金时刻" value="黄金时刻光线"/>
              </el-select>
            </div>
          </el-col>
          
          <el-col :span="8">
            <div class="generator-section">
              <el-text tag="b" class="section-title">背景设置</el-text>
              <el-select v-model="promptConfig.background" placeholder="选择背景" class="w-full mt-10px">
                <el-option label="简洁背景" value="简洁背景"/>
                <el-option label="工作环境" value="相关工作环境背景"/>
                <el-option label="自然环境" value="自然环境背景"/>
                <el-option label="纯色背景" value="纯色背景"/>
                <el-option label="虚化背景" value="虚化背景"/>
              </el-select>
            </div>
          </el-col>
        </el-row>
        
        <div class="generator-actions mt-20px">
          <el-button type="success" @click="generatePrompt">生成提示词</el-button>
          <el-button @click="clearPromptConfig">清空选择</el-button>
        </div>
      </div>
    </div>
    
    <el-input
      v-model="prompt"
      maxlength="1024"
      :rows="5"
      class="w-100% mt-15px"
      input-style="border-radius: 7px;"
      placeholder="例如：一位优秀青年形象，积极向上的表情，现代服装，高清画质，肖像摄影风格"
      show-word-limit
      type="textarea"
    />
    
    <!-- 知识文档选择按钮 -->
    <div class="knowledge-actions mt-10px">
      <el-button type="info" plain @click="showKnowledgeDialog = true">
        <el-icon><Document /></el-icon>
        选择知识文档
      </el-button>
    </div>
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
  <div class="hot-words">
    <div>
      <el-text tag="b">随机热词</el-text>
    </div>
    <el-space wrap class="word-list">
      <el-button
        round
        class="btn"
        :type="selectHotWord === hotWord ? 'primary' : 'default'"
        v-for="hotWord in ImageHotWords"
        :key="hotWord"
        @click="handleHotWordClick(hotWord)"
      >
        {{ hotWord }}
      </el-button>
    </el-space>
  </div>
  <div class="image-size">
    <div>
      <el-text tag="b">尺寸</el-text>
    </div>
    <el-space wrap class="size-list">
      <div
        class="size-item"
        v-for="imageSize in MidjourneySizeList"
        :key="imageSize.key"
        @click="handleSizeClick(imageSize)"
      >
        <div
          :class="selectSize === imageSize.key ? 'size-wrapper selectImageSize' : 'size-wrapper'"
        >
          <div :style="imageSize.style"></div>
        </div>
        <div class="size-font">{{ imageSize.key }}</div>
      </div>
    </el-space>
  </div>
  <div class="model">
    <div>
      <el-text tag="b">模型</el-text>
    </div>
    <el-space wrap class="model-list">
      <div
        :class="selectModel === model.key ? 'modal-item selectModel' : 'modal-item'"
        v-for="model in MidjourneyModels"
        :key="model.key"
      >
        <el-image :src="model.image" fit="contain" @click="handleModelClick(model)" />
        <div class="model-font">{{ model.name }}</div>
      </div>
    </el-space>
  </div>
  <div class="version">
    <div>
      <el-text tag="b">版本</el-text>
    </div>
    <el-space wrap class="version-list">
      <el-select
        v-model="selectVersion"
        class="version-select !w-350px"
        clearable
        placeholder="请选择版本"
      >
        <el-option
          v-for="item in versionList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-space>
  </div>
  <div class="model">
    <div>
      <el-text tag="b">参考图</el-text>
    </div>
    <el-space wrap class="model-list">
      <UploadImg v-model="referImageUrl" height="120px" width="120px" />
    </el-space>
  </div>
  <div class="btns">
    <el-button
      type="primary"
      size="large"
      round
      :disabled="prompt.length === 0"
      @click="handleGenerateImage"
    >
      {{ drawIn ? '生成中' : '生成内容' }}
    </el-button>
  </div>
</template>
<script setup lang="ts">
import { ImageApi, ImageMidjourneyImagineReqVO, ImageVO } from '@/api/ai/image'
import {
  AiPlatformEnum,
  ImageHotWords,
  ImageSizeVO,
  ImageModelVO,
  MidjourneyModels,
  MidjourneySizeList,
  MidjourneyVersions,
  NijiVersionList
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
const referImageUrl = ref<any>() // 参考图
const selectModel = ref<string>('midjourney') // 选中的模型
const selectSize = ref<string>('1:1') // 选中 size
const selectVersion = ref<any>('6.0') // 选中的 version
const versionList = ref<any>(MidjourneyVersions) // version 列表

// 提示词生成器相关
const showPromptGenerator = ref<boolean>(false) // 是否显示提示词生成器
const promptConfig = ref({
  character: '', // 人物类型
  expression: '', // 表情状态
  clothing: '', // 服装风格
  style: '', // 艺术风格
  quality: '', // 画质要求
  lighting: '', // 光线效果
  background: '' // 背景设置
})

// 知识文档相关
const showKnowledgeDialog = ref<boolean>(false) // 是否显示知识文档弹窗
const knowledgeLoading = ref<boolean>(false) // 知识文档加载状态
const knowledgeSearchName = ref<string>('') // 搜索的知识库名称
const knowledgeDocuments = ref<KnowledgeDocumentVO[]>([]) // 知识文档列表
const knowledgePagination = ref({
  pageNo: 1,
  pageSize: 10,
  total: 0
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
  prompt.value = hotWord // 设置提示次
}

/** 点击 size 尺寸 */
const handleSizeClick = async (imageSize: ImageSizeVO) => {
  selectSize.value = imageSize.key
}

/** 点击 model 模型 */
const handleModelClick = async (model: ImageModelVO) => {
  selectModel.value = model.key
  if (model.key === 'niji') {
    versionList.value = NijiVersionList // 默认选择 niji
  } else {
    versionList.value = MidjourneyVersions // 默认选择 midjourney
  }
  selectVersion.value = versionList.value[0].value
}

/** 生成提示词 */
const generatePrompt = () => {
  const config = promptConfig.value
  const promptParts: string[] = []
  
  // 按顺序组合提示词各部分
  if (config.character) promptParts.push(config.character)
  if (config.expression) promptParts.push(config.expression)
  if (config.clothing) promptParts.push(config.clothing)
  if (config.quality) promptParts.push(config.quality)
  if (config.style) promptParts.push(config.style)
  if (config.lighting) promptParts.push(config.lighting)
  if (config.background) promptParts.push(config.background)
  
  if (promptParts.length === 0) {
    message.warning('请至少选择一个配置项')
    return
  }
  
  // 生成新的提示词部分
  const newPromptPart = promptParts.join('，')
  
  // 获取当前输入框内容
  const currentPrompt = prompt.value.trim()
  
  // 合并逻辑：如果输入框有内容，用中文逗号连接；如果没有内容，直接使用新提示词
  if (currentPrompt) {
    prompt.value = currentPrompt + '，' + newPromptPart
  } else {
    prompt.value = newPromptPart
  }
  
  message.success(`提示词已添加：${newPromptPart}`)
}

/** 清空提示词配置 */
const clearPromptConfig = () => {
  promptConfig.value = {
    character: '',
    expression: '',
    clothing: '',
    style: '',
    quality: '',
    lighting: '',
    background: ''
  }
  message.success('配置已清空')
}

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

/** 搜索知识文档 */
const handleKnowledgeSearch = () => {
  knowledgePagination.value.pageNo = 1
  getKnowledgeDocuments()
}

/** 分页大小改变 */
const handleKnowledgePageSizeChange = (size: number) => {
  knowledgePagination.value.pageSize = size
  knowledgePagination.value.pageNo = 1
  getKnowledgeDocuments()
}

/** 页码改变 */
const handleKnowledgePageChange = (page: number) => {
  knowledgePagination.value.pageNo = page
  getKnowledgeDocuments()
}

/** 选择知识文档 */
const handleSelectKnowledgeDocument = (document: KnowledgeDocumentVO) => {
  // 获取文档内容并截取前1024个字符
  const content = document.content || ''
  const truncatedContent = content.substring(0, 1024)
  
  // 直接将截取的内容赋值到输入框
  prompt.value = truncatedContent
  
  showKnowledgeDialog.value = false
  message.success(`已选择文档：${document.name}，内容已导入（${truncatedContent.length}/${content.length}字符）`)
}

/** 表格行点击 */
const handleKnowledgeRowClick = (row: KnowledgeDocumentVO) => {
  // 可以在这里添加行点击的逻辑，比如显示详情
}

/** 关闭知识文档弹窗 */
const handleKnowledgeDialogClose = () => {
  showKnowledgeDialog.value = false
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
  
  // 处理时间戳格式
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
    // 弹窗打开时获取数据
    getKnowledgeDocuments()
  }
})

/** 图片生成 */
const handleGenerateImage = async () => {
  // 从 models 中查找匹配的模型
  const matchedModel = props.models.find(
    (item) => item.model === selectModel.value && item.platform === AiPlatformEnum.MIDJOURNEY
  )
  if (!matchedModel) {
    message.error('该模型不可用，请选择其它模型')
    return
  }

  // 二次确认
  await message.confirm(`确认生成内容?`)
  try {
    // 加载中
    drawIn.value = true
    // 回调
    emits('onDrawStart', AiPlatformEnum.MIDJOURNEY)
    // 发送请求
    const imageSize = MidjourneySizeList.find(
      (item) => selectSize.value === item.key
    ) as ImageSizeVO
    const req = {
      prompt: prompt.value,
      modelId: matchedModel.id,
      width: imageSize.width,
      height: imageSize.height,
      version: selectVersion.value,
      referImageUrl: referImageUrl.value,
      base64Array: []
    } as ImageMidjourneyImagineReqVO
    await ImageApi.midjourneyImagine(req)
  } finally {
    // 回调
    emits('onDrawComplete', AiPlatformEnum.MIDJOURNEY)
    // 加载结束
    drawIn.value = false
  }
}

/** 填充值 */
const settingValues = async (detail: ImageVO) => {
  // 提示词
  prompt.value = detail.prompt
  // image size
  const imageSize = MidjourneySizeList.find(
    (item) => item.key === `${detail.width}:${detail.height}`
  ) as ImageSizeVO
  selectSize.value = imageSize.key
  // 选中模型
  const model = MidjourneyModels.find((item) => item.key === detail.options?.model) as ImageModelVO
  await handleModelClick(model)
  // 版本
  selectVersion.value = versionList.value.find(
    (item) => item.value === detail.options?.version
  ).value
  // image
  referImageUrl.value = detail.options.referImageUrl
}

/** 暴露组件方法 */
defineExpose({ settingValues })
</script>
<style scoped lang="scss">
// 提示词
.prompt {
}

// 提示词生成器
.prompt-generator {
  margin-top: 20px;
  
  .generator-content {
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 20px;
    margin-top: 15px;
    background-color: #fafafa;
    
    .generator-section {
      .section-title {
        font-size: 14px;
        color: #606266;
        margin-bottom: 10px;
      }
    }
    
    .generator-actions {
      text-align: center;
      padding-top: 20px;
      border-top: 1px solid #e4e7ed;
    }
  }
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

// version
.version {
  margin-top: 20px;

  .version-list {
    margin-top: 20px;
    width: 100%;
  }
}

// 模型
.model {
  margin-top: 30px;

  .model-list {
    margin-top: 15px;

    .modal-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 150px;
      //outline: 1px solid blue;
      overflow: hidden;
      border: 3px solid transparent;
      cursor: pointer;

      .model-font {
        font-size: 14px;
        color: #3e3e3e;
        font-weight: bold;
      }
    }

    .selectModel {
      border: 3px solid #1293ff;
      border-radius: 5px;
    }
  }
}

// 尺寸
.image-size {
  width: 100%;
  margin-top: 30px;

  .size-list {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    width: 100%;
    margin-top: 20px;

    .size-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      cursor: pointer;

      .size-wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        border-radius: 7px;
        padding: 4px;
        width: 50px;
        height: 50px;
        background-color: #fff;
        border: 1px solid #fff;
      }

      .size-font {
        font-size: 14px;
        color: #3e3e3e;
        font-weight: bold;
      }
    }
  }

  .selectImageSize {
    border: 1px solid #1293ff !important;
  }
}

.btns {
  display: flex;
  justify-content: center;
  margin-top: 50px;
}

// 知识文档选择弹窗
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
  
  .pagination-section {
    margin-top: 20px;
    text-align: right;
  }
}

.knowledge-actions {
  margin-top: 10px;
  text-align: right;
}
</style>
