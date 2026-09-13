<template>
  <div class="result-container">
    <!-- 结果头部 -->
    <div class="result-header">
      <div class="header-content">
        <div class="result-icon">
          <span class="icon-emoji">{{ getResultIcon() }}</span>
        </div>
        <div class="result-info">
          <h1 class="result-title">
            {{ isReviewMode ? '成绩回顾' : '测试完成！' }}
          </h1>
          <p class="result-subtitle">{{ currentTest?.title }}</p>
        </div>
      </div>
    </div>

    <!-- 成绩展示 -->
    <div class="score-section">
      <div class="score-card">
        <div class="score-main">
          <div class="score-circle" :class="getScoreLevel()">
            <div class="score-number">{{ testResult?.score }}</div>
            <div class="score-unit">分</div>
          </div>
          <div class="score-info">
            <div class="score-level" :class="getScoreLevel()">
              {{ getScoreLevelText() }}
            </div>
            <div class="score-details">
              <span>答对 {{ testResult?.correctCount }}/{{ testResult?.totalCount }} 题</span>
              <span>·</span>
              <span>用时 {{ formatTime(testResult?.timeUsed || 0) }}</span>
            </div>
          </div>
        </div>
        
        <div class="score-stats">
          <div class="stat-item">
            <div class="stat-icon correct">✓</div>
            <div class="stat-info">
              <div class="stat-number">{{ testResult?.correctCount }}</div>
              <div class="stat-label">答对题数</div>
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon wrong">✗</div>
            <div class="stat-info">
              <div class="stat-number">{{ testResult?.wrongCount }}</div>
              <div class="stat-label">答错题数</div>
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon accuracy">%</div>
            <div class="stat-info">
              <div class="stat-number">{{ testResult?.accuracy }}%</div>
              <div class="stat-label">正确率</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- AI个性化建议 -->
    <div class="ai-advice-section" v-if="showAiAdvice">
      <div class="ai-advice-card">
        <div class="ai-advice-header">
          <el-icon class="ai-icon"><Cpu /></el-icon>
          <span>AI专家个性化建议</span>
          <el-button 
            v-if="!isLoadingAiAdvice" 
            type="primary" 
            size="small" 
            @click="generateAiAdvice"
          >
            {{ aiAdvice ? '重新生成' : '生成建议' }}
          </el-button>
        </div>
        
        <div class="ai-advice-content">
          <div v-if="isLoadingAiAdvice" class="ai-loading">
            <div class="loading-animation">
              <div class="loading-dots">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
            <p class="loading-text">AI专家正在深度分析您的测试结果，生成专属建议...</p>
          </div>
          
          <div v-else-if="aiAdvice" class="ai-advice-text">
            <div class="advice-content" v-html="formatAiAdvice(aiAdvice)"></div>
            <div class="ai-footer">
              <span class="ai-tag">🤖 AI专家分析</span>
              <el-button type="text" size="small" @click="generateAiAdvice">
                重新分析
              </el-button>
            </div>
          </div>
          
          <div v-else class="ai-no-advice">
            <p>点击上方按钮获取AI专家的个性化建议</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 防诈骗建议（当没有AI建议时显示） -->
    <div class="advice-section" v-if="!showAiAdvice || (!aiAdvice && !isLoadingAiAdvice)">
      <div class="advice-card">
        <div class="advice-header">
          <el-icon><Warning /></el-icon>
          <span>个性化红色知识学习建议</span>
        </div>
        <div class="advice-content">
          <div class="advice-level" :class="getAdviceLevel()">
            <div class="level-icon">{{ getAdviceLevelIcon() }}</div>
            <div class="level-text">
              <div class="level-title">{{ getAdviceLevelTitle() }}</div>
              <div class="level-desc">{{ getAdviceLevelDesc() }}</div>
            </div>
          </div>
          <div class="advice-list">
            <div 
              v-for="(advice, index) in getPersonalizedAdvice()"
              :key="index"
              class="advice-item"
            >
              <div class="advice-icon">{{ advice.icon }}</div>
              <div class="advice-text">{{ advice.text }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 答题详情 -->
    <div class="details-section">
      <div class="details-header">
        <h3 class="details-title">答题详情</h3>
        <div class="details-filter">
          <el-radio-group v-model="detailFilter" size="small">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="correct">答对</el-radio-button>
            <el-radio-button label="wrong">答错</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      
      <div class="details-list">
        <div
          v-for="(detail, index) in filteredDetails"
          :key="index"
          class="detail-item"
          :class="{ 'correct': detail.isCorrect, 'wrong': !detail.isCorrect }"
        >
          <div class="detail-header">
            <div class="question-info">
              <span class="question-number">第{{ detail.questionIndex + 1 }}题</span>
              <span class="question-type">{{ getQuestionTypeText(detail.question.type) }}</span>
              <span class="question-score">{{ detail.question.score }}分</span>
            </div>
            <div class="result-status" :class="{ 'correct': detail.isCorrect, 'wrong': !detail.isCorrect }">
              <el-icon v-if="detail.isCorrect"><Check /></el-icon>
              <el-icon v-else><Close /></el-icon>
              <span>{{ detail.isCorrect ? '答对' : '答错' }}</span>
            </div>
          </div>
          
          <div class="detail-content">
            <div class="question-title">{{ detail.question.title }}</div>
            
            <div class="answer-comparison">
              <div class="user-answer">
                <div class="answer-label">您的答案：</div>
                <div class="answer-value" :class="{ 'correct': detail.isCorrect, 'wrong': !detail.isCorrect }">
                  {{ detail.userAnswer || '未作答' }}
                </div>
              </div>
              <div class="correct-answer">
                <div class="answer-label">正确答案：</div>
                <div class="answer-value correct">{{ detail.correctAnswer }}</div>
              </div>
            </div>
            
            <div class="explanation" v-if="detail.question.explanation">
              <div class="explanation-header">
                <el-icon><InfoFilled /></el-icon>
                <span>解析</span>
              </div>
              <div class="explanation-content">{{ detail.question.explanation }}</div>
            </div>
            
            <div class="tips" v-if="detail.question.tips">
              <div class="tips-header">
                <el-icon><Warning /></el-icon>
                <span>知识要点</span>
              </div>
              <div class="tips-content">{{ detail.question.tips }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="action-section">
      <div class="action-buttons">
        <el-button size="large" @click="goBack" class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回题目列表
        </el-button>
        <el-button type="primary" size="large" @click="retakeTest" class="retake-btn">
          <el-icon><Refresh /></el-icon>
          重新测试
        </el-button>
        <el-button type="success" size="large" @click="shareResult" class="share-btn">
          <el-icon><Share /></el-icon>
          分享成绩
        </el-button>
        <el-button 
          v-if="!showAiAdvice" 
          type="info" 
          size="large" 
          @click="generateAiAdvice" 
          class="ai-btn"
        >
          <el-icon><Cpu /></el-icon>
          获取AI建议
        </el-button>
      </div>
    </div>

    <!-- 推荐学习 -->
    <div class="recommend-section">
      <div class="recommend-header">
        <h3 class="recommend-title">
          <span class="title-icon">📚</span>
          推荐学习
        </h3>
        <p class="recommend-subtitle">基于您的测试结果，我们为您推荐以下学习内容</p>
      </div>
      
      <div class="recommend-list">
        <div
          v-for="(item, index) in getRecommendedLearning()"
          :key="index"
          class="recommend-item"
          @click="goToLearning(item)"
        >
          <div class="recommend-icon">{{ item.icon }}</div>
          <div class="recommend-content">
            <div class="recommend-name">{{ item.title }}</div>
            <div class="recommend-desc">{{ item.description }}</div>
          </div>
          <div class="recommend-arrow">→</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  ArrowLeft, Refresh, Share, Check, Close, InfoFilled, Warning, Cpu 
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTestById } from '@/api/blog/check/questions'
import { FraudCheckStorage } from '@/utils/storage/fraudCheck'
import { aiAdviceService } from './components/AiAdviceService'
import type { TestVO, TestResultVO } from '@/api/blog/check'

const router = useRouter()
const route = useRoute()

// 响应式数据
const currentTest = ref<TestVO>()
const testResult = ref<TestResultVO>()
const detailFilter = ref('all')
const isReviewMode = ref(false)
const aiAdvice = ref('')
const isLoadingAiAdvice = ref(false)
const showAiAdvice = ref(false)

// 计算属性
const filteredDetails = computed(() => {
  if (!testResult.value) return []
  
  const details = testResult.value.details
  
  switch (detailFilter.value) {
    case 'correct':
      return details.filter(d => d.isCorrect)
    case 'wrong':
      return details.filter(d => !d.isCorrect)
    default:
      return details
  }
})

// 方法
const generateAiAdvice = async () => {
  if (!testResult.value) {
    ElMessage.warning('无测试结果数据')
    return
  }
  
  isLoadingAiAdvice.value = true
  showAiAdvice.value = true
  aiAdvice.value = '' // 清空之前的建议
  
  try {
    ElMessage.info('正在调用AI分析您的测试结果...')
    const advice = await aiAdviceService.generatePersonalizedAdvice(testResult.value)
    aiAdvice.value = advice
    ElMessage.success('AI建议生成成功！')
  } catch (error) {
    console.error('生成AI建议失败:', error)
    ElMessage.error('AI建议生成失败，已为您提供备用建议')
    // 如果AI调用失败，aiAdviceService 会自动返回备用建议
  } finally {
    isLoadingAiAdvice.value = false
  }
}

const initializeResult = async () => {
  const testId = parseInt(route.query.testId as string)
  const fromStorage = route.query.fromStorage === 'true'
  isReviewMode.value = route.query.reviewMode === 'true'
  
  if (!testId) {
    ElMessage.error('测试ID无效')
    router.push('/blog/check')
    return
  }

  // 获取测试数据
  const test = getTestById(testId)
  if (!test) {
    ElMessage.error('找不到指定的测试')
    router.push('/blog/check')
    return
  }
  currentTest.value = test

  if (fromStorage) {
    // 从本地存储获取结果
    const result = FraudCheckStorage.getTestResultById(testId)
    if (!result) {
      ElMessage.error('找不到测试结果')
      router.push('/blog/check')
      return
    }
    testResult.value = result
    
    // 如果不是回顾模式，自动生成AI建议
    if (!isReviewMode.value) {
      // 延迟一下再调用AI，让用户先看到结果
      setTimeout(() => {
        generateAiAdvice()
      }, 1000)
    }
  } else {
    ElMessage.error('无效的访问方式')
    router.push('/blog/check')
  }
}

const getResultIcon = () => {
  if (!testResult.value) return '🤔'
  
  const score = testResult.value.score
  const totalScore = testResult.value.details.reduce((sum, d) => sum + d.question.score, 0)
  const percentage = (score / totalScore) * 100
  
  if (percentage >= 90) return '🏆'
  if (percentage >= 80) return '🎉'
  if (percentage >= 70) return '👍'
  if (percentage >= 60) return '😊'
  return '💪'
}

const getScoreLevel = () => {
  if (!testResult.value) return 'poor'
  
  const score = testResult.value.score
  const totalScore = testResult.value.details.reduce((sum, d) => sum + d.question.score, 0)
  const percentage = (score / totalScore) * 100
  
  if (percentage >= 90) return 'excellent'
  if (percentage >= 80) return 'good'
  if (percentage >= 70) return 'average'
  return 'poor'
}

const getScoreLevelText = () => {
  const level = getScoreLevel()
  const levelTexts = {
    excellent: '优秀',
    good: '良好',
    average: '一般',
    poor: '需要提升'
  }
  return levelTexts[level as keyof typeof levelTexts]
}

const getAdviceLevel = () => {
  const level = getScoreLevel()
  const adviceLevels = {
    excellent: 'excellent',
    good: 'good',
    average: 'warning',
    poor: 'danger'
  }
  return adviceLevels[level as keyof typeof adviceLevels]
}

const getAdviceLevelIcon = () => {
  const level = getScoreLevel()
  const icons = {
    excellent: '🛡️',
    good: '👍',
    average: '⚠️',
    poor: '🚨'
  }
  return icons[level as keyof typeof icons]
}

const getAdviceLevelTitle = () => {
  const level = getScoreLevel()
  const titles = {
    excellent: '红色知识储备优秀',
    good: '红色知识储备良好',
    average: '红色知识储备一般',
    poor: '红色知识储备需要加强'
  }
  return titles[level as keyof typeof titles]
}

const getAdviceLevelDesc = () => {
  const level = getScoreLevel()
  const descriptions = {
    excellent: '您具有很强的红色文化知识储备，对党史、革命历史等有深入了解。请继续学习，并帮助身边的人提高红色文化素养。',
    good: '您的红色知识储备不错，对大部分内容都有了解。建议继续深入学习红色文化经典，保持学习热情。',
    average: '您对红色知识有一定了解，但仍有提升空间。建议多阅读党史、革命史料，提高理论水平。',
    poor: '您的红色知识储备需要加强，建议深入学习党的历史和革命传统，提高红色文化素养。'
  }
  return descriptions[level as keyof typeof descriptions]
}

const getPersonalizedAdvice = () => {
  if (!testResult.value) return []
  
  const wrongCategories = new Set<string>()
  testResult.value.details.forEach(detail => {
    if (!detail.isCorrect) {
      wrongCategories.add(detail.question.category)
    }
  })
  
  const allAdvice = [
{ icon: '📚', text: '系统学习党的历史和理论，筑牢思想根基', category: 'theory' },
{ icon: '🚶', text: '实地探访革命遗址纪念馆，感悟历史现场', category: 'practice' },
{ icon: '📖', text: '阅读红色经典书籍，汲取精神力量', category: 'reading' },
{ icon: '💻', text: '运用数字技术学习党史，增强互动体验', category: 'technology' },
{ icon: '🎭', text: '参与红色剧目演出，体验历史情感', category: 'art' },
{ icon: '🏫', text: '营造校园红色文化氛围，融入日常学习', category: 'campus' },
{ icon: '🌐', text: '创作新媒体内容传播红色文化', category: 'communication' },
{ icon: '🤝', text: '加强学校与红色场馆联动，整合教育资源', category: 'cooperation' },
{ icon: '🔍', text: '参与红色课题研究，深化理解创新', category: 'research' }
  ]
  
  // 根据错误类别筛选建议
  let filteredAdvice = allAdvice.filter(advice => 
    wrongCategories.has(advice.category) || advice.category === 'theory'
  )
  
  // 如果没有错误，显示通用建议
  if (filteredAdvice.length === 1) {
    filteredAdvice = allAdvice.slice(0, 4)
  }
  
  return filteredAdvice.slice(0, 5)
}

const getRecommendedLearning = () => {
  if (!testResult.value) return []
  
  const wrongCategories = new Set<string>()
  testResult.value.details.forEach(detail => {
    if (!detail.isCorrect) {
      wrongCategories.add(detail.question.category)
    }
  })
  
  const allRecommendations = [
    {
      icon: '📱',
      title: '马克思主义哲学',
      description: '理论与实践：相辅相成的双翼',
      category: 'theory',
      path: '/blog/detail/26'
    },
    {
      icon: '💳',
      title: '党史',
      description: '《百年党史：从石库门到天安门的光辉历程》',
      category: 'art',
      path: '/blog/detail/12'
    },
    {
      icon: '🌐',
      title: '毛泽东思想',
      description: '毛泽东思想的光辉历程与时代价值',
      category: 'technology',
      path: '/blog/detail/16'
    },
    {
      icon: '🛒',
      title: '社会主义核心价值观',
      description: ' 践行社会主义核心价值观：凝聚新时代的精神力量',
      category: 'reading',
      path: '/blog/detail/25'
    },
    {
      icon: '❤️',
      title: '特别能战斗精神',
      description: '特别能战斗精神VR展厅',
      category: 'practice',
      path: '/red/fight'
    }
  ]
  
  let recommendations = allRecommendations.filter(rec => 
    wrongCategories.has(rec.category)
  )
  console.log("11111",recommendations)
  
  // 如果没有特定推荐，显示通用推荐
  if (recommendations.length === 0) {
    recommendations = getRandomThree(allRecommendations);

  }
  console.log("22222",recommendations)
  console.log("33333",recommendations.slice(0, 4))

  return recommendations.slice(0, 4)
}

function getRandomThree(arr) {
    // 复制数组避免修改原数据
    const shuffled = [...arr]; 
    for (let i = shuffled.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]]; // 交换元素
    }
    return shuffled.slice(0, 3);
}

const getQuestionTypeText = (type: string) => {
  const texts = {
    single: '单选题',
    multiple: '多选题',
    judge: '判断题',
    text: '填空题'
  }
  return texts[type as keyof typeof texts] || '未知题型'
}

const formatTime = (seconds: number) => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  if (minutes > 0) {
    return `${minutes}分${remainingSeconds}秒`
  }
  return `${remainingSeconds}秒`
}

const goBack = () => {
  router.push('/blog/check')
}

const retakeTest = () => {
  if (!currentTest.value) return
  
  ElMessageBox.confirm(
    '确定要重新测试吗？这将清除当前的测试记录。',
    '确认重新测试',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 删除测试结果和进度
    FraudCheckStorage.deleteTestResult(currentTest.value!.id)
    FraudCheckStorage.clearTestProgress(currentTest.value!.id)
    
    ElMessage.success('已清除测试记录，开始重新测试')
    
    // 跳转到测试页面
    router.push({
      path: '/blog/check/test',
      query: {
        testId: currentTest.value!.id
      }
    })
  })
}

const shareResult = async () => {
  if (!testResult.value || !currentTest.value) return
  
  const shareText = `我在红色知识测试"${currentTest.value.title}"中获得了${testResult.value.score}分！答对了${testResult.value.correctCount}/${testResult.value.totalCount}题，正确率${testResult.value.accuracy}%。一起来测试你的红色文化知识吧！`
  
  try {
    if (navigator.share) {
      await navigator.share({
        title: '红色知识测试结果',
        text: shareText,
        url: window.location.href
      })
    } else {
      // 备用方案：复制到剪贴板
      await navigator.clipboard.writeText(shareText)
      ElMessage.success('测试结果已复制到剪贴板')
    }
  } catch (error) {
    console.error('分享失败:', error)
    ElMessage.error('分享失败，请重试')
  }
}

const goToLearning = (item: any) => {
  router.push(item.path)
}

/**
 * 格式化AI建议文本
 */
const formatAiAdvice = (advice: string) => {
  return advice
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/### (.*?)(?=\n|$)/g, '<h3>$1</h3>')
    .replace(/## (.*?)(?=\n|$)/g, '<h2>$1</h2>')
    .replace(/• (.*?)(?=\n|$)/g, '<li>$1</li>')
    .replace(/(?:^|\n)(?=<li>)/g, '<ul>')
    .replace(/(?<=<\/li>)(?!\n<li>)/g, '</ul>')
    .replace(/\n\n/g, '<br><br>')
    .replace(/\n/g, '<br>')
    .replace(/<br><br>/g, '<br>')
}

onMounted(() => {
  initializeResult()
})
</script>

<style scoped>
.result-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef7f0 0%, #fff5f5 100%);
  padding: 20px;
}

.result-header {
  text-align: center;
  margin-bottom: 30px;
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.result-icon {
  margin-bottom: 20px;
}

.icon-emoji {
  font-size: 64px;
  display: inline-block;
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-10px); }
  60% { transform: translateY(-5px); }
}

.result-title {
  font-size: 32px;
  font-weight: bold;
  color: #1f2937;
  margin: 0 0 10px 0;
}

.result-subtitle {
  font-size: 18px;
  color: #6b7280;
  margin: 0;
}

.score-section {
  margin-bottom: 30px;
}

.score-card {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.score-main {
  display: flex;
  align-items: center;
  gap: 40px;
  margin-bottom: 30px;
  justify-content: center;
}

.score-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  background: linear-gradient(135deg, #dc2626, #f59e0b);
  color: white;
  box-shadow: 0 8px 32px rgba(220, 38, 38, 0.3);
}

.score-circle.excellent {
  background: linear-gradient(135deg, #10b981, #059669);
  box-shadow: 0 8px 32px rgba(16, 185, 129, 0.3);
}

.score-circle.good {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.3);
}

.score-circle.average {
  background: linear-gradient(135deg, #f59e0b, #d97706);
  box-shadow: 0 8px 32px rgba(245, 158, 11, 0.3);
}

.score-number {
  font-size: 32px;
  font-weight: bold;
  line-height: 1;
}

.score-unit {
  font-size: 16px;
  font-weight: 500;
}

.score-info {
  text-align: left;
}

.score-level {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.score-level.excellent { color: #10b981; }
.score-level.good { color: #3b82f6; }
.score-level.average { color: #f59e0b; }
.score-level.poor { color: #dc2626; }

.score-details {
  color: #6b7280;
  font-size: 16px;
}

.score-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding-top: 30px;
  border-top: 1px solid #f0f0f0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
}

.stat-icon.correct {
  background: #dcfce7;
  color: #16a34a;
}

.stat-icon.wrong {
  background: #fef2f2;
  color: #dc2626;
}

.stat-icon.accuracy {
  background: #dbeafe;
  color: #2563eb;
}

.stat-number {
  font-size: 20px;
  font-weight: bold;
  color: #1f2937;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
}

.ai-advice-section {
  margin-bottom: 30px;
}

.ai-advice-card {
  max-width: 800px;
  margin: 0 auto;
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 2px solid #0ea5e9;
}

.ai-advice-header {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: 600;
  color: #0c4a6e;
  margin-bottom: 25px;
}

.ai-icon {
  font-size: 24px;
  color: #0ea5e9;
}

.ai-loading {
  text-align: center;
  padding: 40px 20px;
}

.loading-animation {
  margin-bottom: 20px;
}

.loading-dots {
  display: inline-flex;
  gap: 8px;
}

.loading-dots span {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #0ea5e9;
  animation: loading-bounce 1.4s ease-in-out infinite both;
}

.loading-dots span:nth-child(1) { animation-delay: -0.32s; }
.loading-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes loading-bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.loading-text {
  color: #0c4a6e;
  font-size: 16px;
  margin: 0;
}

.ai-advice-text {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.advice-content {
  color: #374151;
  line-height: 1.8;
  font-size: 15px;
}

.advice-content :deep(h2) {
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
  margin: 25px 0 15px 0;
  border-left: 4px solid #dc2626;
  padding-left: 12px;
}

.advice-content :deep(h3) {
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  margin: 20px 0 10px 0;
}

.advice-content :deep(h2):first-child,
.advice-content :deep(h3):first-child {
  margin-top: 0;
}

.advice-content :deep(ul) {
  padding-left: 20px;
  margin: 10px 0;
}

.advice-content :deep(li) {
  margin-bottom: 8px;
  line-height: 1.6;
}

.advice-content :deep(strong) {
  color: #dc2626;
  font-weight: 600;
}

.ai-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.ai-tag {
  background: #e0f2fe;
  color: #0c4a6e;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.ai-btn {
  background: linear-gradient(135deg, #0ea5e9, #0284c7);
  border-color: #0ea5e9;
  color: white;
}

.ai-btn:hover {
  background: linear-gradient(135deg, #0284c7, #0369a1);
  border-color: #0284c7;
}

.advice-section {
  margin-bottom: 30px;
}

.advice-card {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.advice-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 20px;
}

.advice-level {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.advice-level.excellent {
  background: #f0fdf4;
  border-left: 4px solid #10b981;
}

.advice-level.good {
  background: #eff6ff;
  border-left: 4px solid #3b82f6;
}

.advice-level.warning {
  background: #fff7ed;
  border-left: 4px solid #f59e0b;
}

.advice-level.danger {
  background: #fef2f2;
  border-left: 4px solid #dc2626;
}

.level-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.level-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 5px;
}

.level-desc {
  color: #6b7280;
  line-height: 1.6;
}

.advice-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.advice-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 8px;
  border-left: 3px solid #dc2626;
}

.advice-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.advice-text {
  color: #374151;
  line-height: 1.6;
}

.details-section {
  margin-bottom: 30px;
}

.details-header {
  max-width: 800px;
  margin: 0 auto 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.details-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.details-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.detail-item {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-left: 4px solid #e5e7eb;
}

.detail-item.correct {
  border-left-color: #10b981;
}

.detail-item.wrong {
  border-left-color: #dc2626;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 20px 0;
}

.question-info {
  display: flex;
  gap: 10px;
}

.question-number,
.question-type,
.question-score {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.question-number {
  background: #f3f4f6;
  color: #374151;
}

.question-type {
  background: #dbeafe;
  color: #1d4ed8;
}

.question-score {
  background: #fef3c7;
  color: #d97706;
}

.result-status {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  font-weight: 500;
}

.result-status.correct {
  color: #10b981;
}

.result-status.wrong {
  color: #dc2626;
}

.detail-content {
  padding: 20px;
}

.question-title {
  font-size: 16px;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 15px;
  line-height: 1.6;
}

.answer-comparison {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.answer-label {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 5px;
}

.answer-value {
  padding: 8px 12px;
  border-radius: 6px;
  font-weight: 500;
}

.answer-value.correct {
  background: #dcfce7;
  color: #166534;
}

.answer-value.wrong {
  background: #fef2f2;
  color: #991b1b;
}

.explanation,
.tips {
  margin-top: 15px;
  padding: 15px;
  border-radius: 8px;
}

.explanation {
  background: #f0f9ff;
  border-left: 3px solid #0ea5e9;
}

.tips {
  background: #fff7ed;
  border-left: 3px solid #f59e0b;
}

.explanation-header,
.tips-header {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 600;
  margin-bottom: 8px;
}

.explanation-header {
  color: #0c4a6e;
}

.tips-header {
  color: #92400e;
}

.explanation-content,
.tips-content {
  line-height: 1.6;
}

.explanation-content {
  color: #0c4a6e;
}

.tips-content {
  color: #92400e;
}

.action-section {
  margin-bottom: 30px;
}

.action-buttons {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  gap: 15px;
  flex-wrap: wrap;
}

.back-btn,
.retake-btn,
.share-btn {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.recommend-section {
  max-width: 800px;
  margin: 0 auto;
}

.recommend-header {
  text-align: center;
  margin-bottom: 25px;
}

.recommend-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 10px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.title-icon {
  font-size: 28px;
}

.recommend-subtitle {
  color: #6b7280;
  margin: 0;
}

.recommend-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.recommend-item {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 15px;
}

.recommend-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.recommend-icon {
  font-size: 32px;
  flex-shrink: 0;
}

.recommend-content {
  flex: 1;
}

.recommend-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 5px;
}

.recommend-desc {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.5;
}

.recommend-arrow {
  font-size: 18px;
  color: #dc2626;
  font-weight: bold;
}

.ai-no-advice {
  text-align: center;
  padding: 40px 20px;
  color: #6b7280;
  font-size: 16px;
}

@media (max-width: 768px) {
  .result-container {
    padding: 10px;
  }
  
  .header-content,
  .score-card,
  .advice-card {
    padding: 20px;
  }
  
  .score-main {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .score-stats {
    grid-template-columns: 1fr;
  }
  
  .answer-comparison {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .recommend-list {
    grid-template-columns: 1fr;
  }
  
  .recommend-item {
    flex-direction: column;
    text-align: center;
  }
}
</style>