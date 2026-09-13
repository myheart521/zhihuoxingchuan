<template>
  <div class="ai-advice-manager">
    <!-- AI建议卡片 -->
    <el-card class="advice-card" shadow="hover">
      <template #header>
        <div class="advice-header">
          <div class="header-left">
            <el-icon class="header-icon"><Guide /></el-icon>
            <span class="header-title">AI个性化建议</span>
          </div>
          <div class="header-right">
            <el-tag 
              :type="getRiskTagType(aiAdvice?.riskLevel || 'medium')"
              effect="dark"
              size="small"
            >
              {{ getRiskText(aiAdvice?.riskLevel || 'medium') }}
            </el-tag>
          </div>
        </div>
      </template>

      <!-- 加载状态 -->
      <div v-if="isLoading" class="loading-state">
        <el-skeleton :rows="3" animated />
        <div class="loading-text">AI正在分析您的测试结果...</div>
      </div>

      <!-- AI建议内容 -->
      <div v-else-if="aiAdvice" class="advice-content">
        <div class="advice-main">
          <h4 class="advice-title">针对您的表现，AI为您提供以下建议：</h4>
          <div class="advice-text" v-html="formatAdviceText(aiAdvice.advice)"></div>
        </div>

        <!-- 具体建议列表 -->
        <div v-if="aiAdvice.suggestions?.length" class="suggestions-section">
          <h5 class="suggestions-title">重点改进建议：</h5>
          <ul class="suggestions-list">
            <li 
              v-for="(suggestion, index) in aiAdvice.suggestions" 
              :key="index"
              class="suggestion-item"
            >
              <el-icon class="suggestion-icon"><Check /></el-icon>
              {{ suggestion }}
            </li>
          </ul>
        </div>

        <!-- 操作按钮 -->
        <div class="advice-actions">
          <el-button type="primary" @click="refreshAdvice" :loading="isRefreshing">
            <el-icon><Refresh /></el-icon>
            重新生成建议
          </el-button>
          <el-button @click="saveAdviceToLocal" v-if="!isSavedLocally">
            <el-icon><Document /></el-icon>
            保存到本地
          </el-button>
          <el-button type="success" disabled v-else>
            <el-icon><Check /></el-icon>
            已保存到本地
          </el-button>
        </div>

        <!-- 保存时间 -->
        <div class="advice-meta">
          <span class="meta-text">
            生成时间：{{ formatDate(aiAdvice.createdAt) }}
          </span>
          <span v-if="isSavedLocally" class="saved-indicator">
            <el-icon><Check /></el-icon>
            已缓存到浏览器本地
          </span>
        </div>
      </div>

      <!-- 错误状态 -->
      <div v-else-if="error" class="error-state">
        <el-result
          icon="warning"
          title="AI建议生成失败"
          :sub-title="error"
        >
          <template #extra>
            <el-button type="primary" @click="generateAdvice">重试</el-button>
          </template>
        </el-result>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="暂无AI建议">
          <el-button type="primary" @click="generateAdvice">生成AI建议</el-button>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { Guide, Check, Refresh, Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import testStorageManager from '../utils/testStorageManager'
import type { AIAdvice } from '../utils/testStorageManager'
import { AiAdviceService } from '../components/AiAdviceService'

interface Props {
  testId: number
  testResult: any
  category: string
}

const props = defineProps<Props>()

// 响应式数据
const aiAdvice = ref<AIAdvice | null>(null)
const isLoading = ref(false)
const isRefreshing = ref(false)
const error = ref('')

// 计算属性
const isSavedLocally = computed(() => {
  return !!testStorageManager.getAIAdvice(props.testId)
})

// 方法
const getRiskTagType = (level: string) => {
  const types = {
    low: 'success',
    medium: 'warning', 
    high: 'danger'
  }
  return (types[level as keyof typeof types] || 'info') as 'success' | 'warning' | 'danger' | 'info'
}

const getRiskText = (level: string) => {
  const texts = {
    low: '风险较低',
    medium: '中等风险',
    high: '高风险'
  }
  return texts[level as keyof typeof texts] || '未知'
}

const formatAdviceText = (text: string) => {
  return text.replace(/\n/g, '<br>').replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
}

const formatDate = (dateStr: string) => {
  return new Date(dateStr).toLocaleString('zh-CN')
}

/**
 * 生成AI建议
 */
const generateAdvice = async () => {
  if (!props.testResult) {
    ElMessage.warning('测试结果不完整，无法生成建议')
    return
  }

  isLoading.value = true
  error.value = ''

  try {
    const aiService = AiAdviceService.getInstance()
    const advice = await aiService.generatePersonalizedAdvice(props.testResult)
    
    if (advice) {
      const aiAdviceData: AIAdvice = {
        testId: props.testId,
        resultId: props.testResult.id || `result_${props.testId}_${Date.now()}`,
        advice: advice.advice,
        riskLevel: advice.riskLevel,
        suggestions: advice.suggestions || [],
        createdAt: new Date().toISOString(),
        category: props.category
      }
      
      aiAdvice.value = aiAdviceData
      
      // 自动保存到本地
      testStorageManager.saveAIAdvice(aiAdviceData)
      
      ElMessage.success('AI建议生成成功并已保存到本地')
    } else {
      error.value = 'AI服务返回空结果'
    }
  } catch (err: any) {
    console.error('生成AI建议失败:', err)
    error.value = err.message || 'AI建议生成失败，请稍后重试'
    ElMessage.error('生成AI建议失败：' + error.value)
  } finally {
    isLoading.value = false
  }
}

/**
 * 刷新建议
 */
const refreshAdvice = async () => {
  isRefreshing.value = true
  
  try {
    await generateAdvice()
  } finally {
    isRefreshing.value = false
  }
}

/**
 * 手动保存到本地
 */
const saveAdviceToLocal = () => {
  if (aiAdvice.value) {
    testStorageManager.saveAIAdvice(aiAdvice.value)
    ElMessage.success('AI建议已保存到本地')
  }
}

/**
 * 加载本地缓存的建议
 */
const loadCachedAdvice = () => {
  const cached = testStorageManager.getAIAdvice(props.testId)
  if (cached) {
    aiAdvice.value = cached
    console.log('已加载本地缓存的AI建议:', cached)
  }
}

// 监听testId变化
watch(() => props.testId, () => {
  if (props.testId) {
    loadCachedAdvice()
    
    // 如果没有缓存的建议，自动生成
    if (!aiAdvice.value) {
      generateAdvice()
    }
  }
}, { immediate: true })

// 组件挂载时加载
onMounted(() => {
  loadCachedAdvice()
  
  // 如果没有缓存的建议且有测试结果，自动生成
  if (!aiAdvice.value && props.testResult) {
    generateAdvice()
  }
})
</script>

<style scoped lang="scss">
.ai-advice-manager {
  width: 100%;
}

.advice-card {
  border: 2px solid #f0f0f0;
  transition: all 0.3s ease;

  &:hover {
    border-color: #409eff;
  }

  :deep(.el-card__header) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 16px 20px;
  }
}

.advice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .header-left {
    display: flex;
    align-items: center;
    gap: 8px;

    .header-icon {
      font-size: 20px;
    }

    .header-title {
      font-size: 16px;
      font-weight: 600;
    }
  }
}

.loading-state {
  padding: 20px;
  text-align: center;

  .loading-text {
    margin-top: 16px;
    color: #666;
    font-size: 14px;
  }
}

.advice-content {
  padding: 4px;
}

.advice-main {
  margin-bottom: 24px;

  .advice-title {
    color: #333;
    font-size: 16px;
    margin-bottom: 12px;
    font-weight: 600;
  }

  .advice-text {
    line-height: 1.8;
    color: #555;
    font-size: 14px;
    background: #f8f9fa;
    padding: 16px;
    border-radius: 8px;
    border-left: 4px solid #409eff;
  }
}

.suggestions-section {
  margin-bottom: 24px;

  .suggestions-title {
    color: #333;
    font-size: 15px;
    margin-bottom: 12px;
    font-weight: 600;
  }

  .suggestions-list {
    list-style: none;
    padding: 0;
    margin: 0;

    .suggestion-item {
      display: flex;
      align-items: flex-start;
      gap: 8px;
      padding: 8px 0;
      color: #555;
      font-size: 14px;
      line-height: 1.6;

      .suggestion-icon {
        color: #67c23a;
        margin-top: 2px;
        flex-shrink: 0;
      }
    }
  }
}

.advice-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.advice-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #888;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;

  .saved-indicator {
    display: flex;
    align-items: center;
    gap: 4px;
    color: #67c23a;
  }
}

.error-state,
.empty-state {
  padding: 20px;
}

// 响应式设计
@media (max-width: 768px) {
  .advice-actions {
    flex-direction: column;

    .el-button {
      width: 100%;
    }
  }

  .advice-meta {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }
}
</style> 