<template>
  <div class="test-container">
    <!-- 测试头部信息 -->
    <div class="test-header">
      <div class="header-content">
        <div class="test-info">
          <h1 class="test-title">
            <span class="back-btn" @click="goBack">
              <el-icon><ArrowLeft /></el-icon>
            </span>
            {{ currentTest?.title }}
          </h1>
          <div class="test-meta">
            <span class="category-tag" :class="`category-${currentTest?.category}`">
              {{ getCategoryLabel(currentTest?.category || '') }}
            </span>
            <span class="difficulty-tag" :class="`difficulty-${currentTest?.difficulty}`">
              {{ getDifficultyLabel(currentTest?.difficulty || '') }}
            </span>
          </div>
        </div>
        <div class="test-progress">
          <div class="progress-info">
            <span class="current-question">第 {{ currentQuestionIndex + 1 }} 题</span>
            <span class="total-questions">共 {{ questions.length }} 题</span>
          </div>
          <div class="progress-bar">
            <div 
              class="progress-fill" 
              :style="{ width: ((currentQuestionIndex + 1) / questions.length * 100) + '%' }"
            ></div>
          </div>
          <div class="time-info">
            <el-icon><Timer /></el-icon>
            <span class="time-left" :class="{ 'time-warning': timeLeft < 300 }">
              {{ formatTime(timeLeft) }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 题目内容区域 -->
    <div class="question-area">
      <div class="question-container">
        <!-- 当前题目 -->
        <div class="question-card" v-if="currentQuestion">
          <div class="question-header">
            <div class="question-type">
              <span class="type-icon">{{ getQuestionTypeIcon(currentQuestion.type) }}</span>
              <span class="type-text">{{ getQuestionTypeText(currentQuestion.type) }}</span>
            </div>
            <div class="question-score">
              <span>{{ currentQuestion.score }}分</span>
            </div>
          </div>
          
          <div class="question-content">
            <h3 class="question-title">{{ currentQuestion.title }}</h3>
            <div class="question-description" v-if="currentQuestion.description">
              {{ currentQuestion.description }}
            </div>
            
            <!-- 图片展示 -->
            <div class="question-image" v-if="currentQuestion.image">
              <img :src="currentQuestion.image" :alt="currentQuestion.title" />
            </div>
            
            <!-- 选项区域 -->
            <div class="options-area">
              <div 
                v-for="(option, index) in currentQuestion.options"
                :key="index"
                class="option-item"
                :class="{ 
                  'selected': selectedAnswers[currentQuestion.id] === option.label,
                  'correct': showAnswer && option.label === currentQuestion.correctAnswer,
                  'wrong': showAnswer && selectedAnswers[currentQuestion.id] === option.label && option.label !== currentQuestion.correctAnswer
                }"
                @click="selectOption(option.label)"
              >
                <div class="option-marker">{{ option.label }}</div>
                <div class="option-content">
                  <div class="option-text">{{ option.text }}</div>
                  <div class="option-desc" v-if="option.description">{{ option.description }}</div>
                </div>
                <div class="option-icon" v-if="showAnswer">
                  <el-icon v-if="option.label === currentQuestion.correctAnswer" class="correct-icon"><Check /></el-icon>
                  <el-icon v-else-if="selectedAnswers[currentQuestion.id] === option.label" class="wrong-icon"><Close /></el-icon>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 解析区域 -->
          <div class="explanation-area" v-if="showAnswer && currentQuestion.explanation">
            <div class="explanation-header">
              <el-icon><InfoFilled /></el-icon>
              <span>题目解析</span>
            </div>
            <div class="explanation-content">
              {{ currentQuestion.explanation }}
            </div>
            <div class="explanation-tips" v-if="currentQuestion.tips">
              <div class="tips-header">
                <el-icon><Warning /></el-icon>
                <span>知识要点</span>
              </div>
              <div class="tips-content">{{ currentQuestion.tips }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-area">
      <div class="action-buttons">
        <el-button 
          :disabled="currentQuestionIndex === 0" 
          @click="previousQuestion"
          class="prev-btn"
        >
          <el-icon><ArrowLeft /></el-icon>
          上一题
        </el-button>
        
        <el-button 
          v-if="!isLastQuestion" 
          type="primary" 
          @click="nextQuestion"
          class="next-btn"
        >
          下一题
          <el-icon><ArrowRight /></el-icon>
        </el-button>
        
        <el-button 
          v-else 
          type="success" 
          @click="finishTest"
          class="finish-btn"
        >
          完成测试
          <el-icon><Check /></el-icon>
        </el-button>
      </div>
      
      <!-- 题目导航 -->
      <div class="question-navigation">
        <div class="nav-title">题目导航</div>
        <div class="nav-items">
          <div
            v-for="(question, index) in questions"
            :key="question.id"
            class="nav-item"
            :class="{
              'current': index === currentQuestionIndex,
              'answered': selectedAnswers[question.id],
              'unanswered': !selectedAnswers[question.id]
            }"
            @click="goToQuestion(index)"
          >
            {{ index + 1 }}
          </div>
        </div>
      </div>
    </div>

    <!-- 提交确认对话框 -->
    <el-dialog
      v-model="showSubmitDialog"
      title="确认提交"
      width="400px"
      class="submit-dialog"
    >
      <div class="submit-content">
        <div class="submit-icon">
          <el-icon><Warning /></el-icon>
        </div>
        <div class="submit-text">
          <p>您确定要提交测试吗？</p>
          <p class="submit-stats">
            已答题：{{ answeredCount }} / {{ questions.length }} 题
            <span v-if="unansweredCount > 0" class="unanswered-warning">
              （还有 {{ unansweredCount }} 题未答）
            </span>
          </p>
        </div>
      </div>
      <template #footer>
        <el-button @click="showSubmitDialog = false">继续答题</el-button>
        <el-button type="primary" @click="submitTest">确认提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  ArrowLeft, ArrowRight, Timer, Check, Close, InfoFilled, Warning 
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { FraudCheckStorage } from '@/utils/storage/fraudCheck'
import { FraudCheckApi } from '@/api/blog/check'
import type { TestVO, TestQuestionVO, TestResultVO, TestAnswerDetailVO } from '@/api/blog/check'

const router = useRouter()
const route = useRoute()

// 响应式数据
const currentTest = ref<TestVO>()
const questions = ref<TestQuestionVO[]>([])
const currentQuestionIndex = ref(0)
const selectedAnswers = ref<Record<number, string>>({})
const timeLeft = ref(0)
const startTime = ref('')
const showAnswer = ref(false)
const showSubmitDialog = ref(false)
const timer = ref<NodeJS.Timeout>()
const categories = ref<Array<{ id: string; name: string; icon: string }>>([])

// 计算属性
const currentQuestion = computed(() => {
  return questions.value[currentQuestionIndex.value] || null
})

const isLastQuestion = computed(() => {
  return currentQuestionIndex.value === questions.value.length - 1
})

const answeredCount = computed(() => {
  if (!questions.value.length) return 0
  return questions.value.filter(q => selectedAnswers.value[q.id]).length
})

const unansweredCount = computed(() => {
  return questions.value.length - answeredCount.value
})

const allQuestionsAnswered = computed(() => {
  return answeredCount.value === questions.value.length
})

// 方法
const getCategoryLabel = (categoryId: string) => {
  if (!Array.isArray(categories.value) || categories.value.length === 0) {
    return categoryId // 返回原始ID作为备用
  }
  
  const category = categories.value.find(cat => cat.id === categoryId)
  return category ? category.name : categoryId
}

const getDifficultyLabel = (difficulty: string) => {
  const labels = {
    easy: '简单',
    medium: '中等', 
    hard: '困难'
  }
  return labels[difficulty as keyof typeof labels] || difficulty
}

const getQuestionTypeIcon = (type: string) => {
  const icons = {
    single: '📝',
    multiple: '☑️',
    judge: '⚖️',
    text: '✍️'
  }
  return icons[type as keyof typeof icons] || '❓'
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
  return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
}

/**
 * 初始化测试
 */
const initializeTest = async () => {
  const testId = parseInt(route.query.testId as string)
  
  if (!testId) {
    ElMessage.error('测试ID无效')
    router.push('/blog/check')
    return
  }

  try {
    // 获取测试详情（启用随机选题）
    const test = await FraudCheckApi.getTestDetail(testId, true)
    if (!test) {
      ElMessage.error('找不到指定的测试')
      router.push('/blog/check')
      return
    }

    currentTest.value = test
    questions.value = test.questions
    
    // 初始化答案对象 - 使用题目ID作为key
    const initialAnswers: Record<number, string> = {}
    questions.value.forEach(q => {
      initialAnswers[q.id] = ''
    })
    selectedAnswers.value = initialAnswers

    // 检查是否有保存的进度
    const savedProgress = FraudCheckStorage.getTestProgress(testId)
    if (savedProgress && savedProgress.currentQuestionIndex !== undefined) {  // 修复属性名
      selectedAnswers.value = savedProgress.answers
      currentQuestionIndex.value = savedProgress.currentQuestionIndex
      timeLeft.value = savedProgress.timeLeft
      
      ElMessage.success('已恢复上次的答题进度')
    } else {
      timeLeft.value = test.timeLimit * 60  // 转换为秒
    }

    // 开始计时
    startTimer()
    
  } catch (error) {
    console.error('初始化测试失败:', error)
    ElMessage.error('加载测试失败，请稍后重试')
    router.push('/blog/check')
  }
}

const startTimer = () => {
  timer.value = setInterval(() => {
    if (timeLeft.value > 0) {
      timeLeft.value--
      
      // 自动保存进度
      saveProgress()
    } else {
      // 时间到，自动提交
      ElMessage.warning('时间已到，自动提交测试')
      submitTest()
    }
  }, 1000)
}

const saveProgress = () => {
  if (!currentTest.value) return
  
  const progress = {
    testId: currentTest.value.id,
    currentQuestionIndex: currentQuestionIndex.value,
    answers: selectedAnswers.value,
    startTime: startTime.value,
    timeLeft: timeLeft.value
  }
  
  FraudCheckStorage.saveTestProgress(progress)
}

const selectOption = (optionLabel: string) => {
  if (showAnswer.value) return
  
  selectedAnswers.value = {
    ...selectedAnswers.value,
    [currentQuestion.value!.id]: optionLabel  // 使用题目ID作为key，而不是索引
  }
  
  // 保存进度
  saveProgress()
}

const previousQuestion = () => {
  if (currentQuestionIndex.value > 0) {
    currentQuestionIndex.value--
    showAnswer.value = false
  }
}

const nextQuestion = () => {
  if (currentQuestionIndex.value < questions.value.length - 1) {
    currentQuestionIndex.value++
    showAnswer.value = false
  }
}

const goToQuestion = (index: number) => {
  currentQuestionIndex.value = index
  showAnswer.value = false
}

const finishTest = () => {
  if (!allQuestionsAnswered.value) {
    showSubmitDialog.value = true
  } else {
    submitTest()
  }
}

const submitTest = async () => {
  if (!currentTest.value) return
  
  showSubmitDialog.value = false
  
  // 停止计时器
  if (timer.value) {
    clearInterval(timer.value)
  }
  
  // 计算成绩
  const result = calculateTestResult()
  
  // 保存结果
  FraudCheckStorage.saveTestResult(result)
  
  ElMessage.success('测试提交成功！')
  
  // 跳转到结果页面
  router.push({
    path: '/blog/check/result',
    query: {
      testId: currentTest.value.id,
      fromStorage: 'true'
    }
  })
}

const calculateTestResult = (): TestResultVO => {
  if (!currentTest.value) throw new Error('测试数据无效')
  
  let correctCount = 0
  let totalScore = 0
  const details: TestAnswerDetailVO[] = []
  
  questions.value.forEach((question, index) => {
    const userAnswer = selectedAnswers.value[question.id] || ''
    const correctAnswer = question.correctAnswer
    const isCorrect = userAnswer === correctAnswer
    
    if (isCorrect) {
      correctCount++
      totalScore += question.score
    }
    
    details.push({
      questionId: question.id,
      questionIndex: index,
      userAnswer,
      correctAnswer,
      isCorrect,
      question
    })
  })
  
  const accuracy = Math.round((correctCount / questions.value.length) * 100)
  const timeUsed = (currentTest.value.timeLimit * 60) - timeLeft.value
  
  return {
    testId: currentTest.value.id,
    testTitle: currentTest.value.title,
    score: totalScore,
    correctCount,
    totalCount: questions.value.length,
    wrongCount: questions.value.length - correctCount,
    accuracy,
    timeUsed,
    completedAt: new Date().toISOString(),
    answers: selectedAnswers.value,
    details
  }
}

const goBack = () => {
  if (answeredCount.value > 0) {
    ElMessageBox.confirm(
      '您的答题进度将会保存，下次可以继续完成测试。',
      '确认离开',
      {
        confirmButtonText: '确定离开',
        cancelButtonText: '继续答题',
        type: 'warning',
      }
    ).then(() => {
      saveProgress()
      router.push('/blog/check')
    })
  } else {
    router.push('/blog/check')
  }
}

// 页面卸载时保存进度
onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value)
  }
  
  // 如果测试未完成，保存进度
  if (!showSubmitDialog.value && answeredCount.value > 0) {
    saveProgress()
  }
})

// 在 onMounted 中加载分类
onMounted(async () => {
  await loadCategories()
  await initializeTest()
})

// 从 API 获取 categories
const loadCategories = async () => {
  try {
    const categoryData = await FraudCheckApi.getTestCategories()
    categories.value = categoryData.map(cat => ({
      id: cat.value,
      name: cat.label,
      icon: cat.icon
    }))
  } catch (error) {
    console.error('加载分类失败:', error)
    // 提供默认分类
    categories.value = [
      { id: 'telecom', name: '📱 电信诈骗', icon: '📱' },
      { id: 'financial', name: '💳 金融诈骗', icon: '💳' },
      { id: 'network', name: '🌐 网络诈骗', icon: '🌐' },
      { id: 'shopping', name: '🛒 购物诈骗', icon: '🛒' },
      { id: 'romance', name: '❤️ 情感诈骗', icon: '❤️' },
      { id: 'email', name: '📧 邮件诈骗', icon: '📧' },
      { id: 'job', name: '💼 招聘诈骗', icon: '💼' },
      { id: 'property', name: '🏠 房产诈骗', icon: '🏠' }
    ]
  }
}
</script>

<style scoped>
.test-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fef7f0 0%, #fff5f5 100%);
  display: flex;
  flex-direction: column;
}

.test-header {
  background: white;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.test-info {
  flex: 1;
}

.test-title {
  margin: 0;
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 10px;
}

.back-btn {
  cursor: pointer;
  color: #666;
  transition: color 0.3s;
  padding: 5px;
  border-radius: 4px;
}

.back-btn:hover {
  color: #dc2626;
  background: #fef2f2;
}

.test-meta {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.category-tag,
.difficulty-tag {
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
}

.category-tag {
  background: #dc2626;
  color: white;
}

.difficulty-easy { background: #10b981; color: white; }
.difficulty-medium { background: #f59e0b; color: white; }
.difficulty-hard { background: #ef4444; color: white; }

.test-progress {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  min-width: 200px;
}

.progress-info {
  display: flex;
  gap: 10px;
  font-size: 14px;
  color: #666;
}

.progress-bar {
  width: 200px;
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #dc2626, #f59e0b);
  transition: width 0.3s ease;
}

.time-info {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  font-weight: 500;
  color: #dc2626;
}

.time-warning {
  color: #ef4444 !important;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.question-area {
  flex: 1;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.question-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.question-card {
  padding: 30px;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.question-type {
  display: flex;
  align-items: center;
  gap: 8px;
}

.type-icon {
  font-size: 18px;
}

.type-text {
  background: #f8fafc;
  color: #475569;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.question-score {
  background: #dc2626;
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.question-content {
  margin-bottom: 20px;
}

.question-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
  line-height: 1.6;
}

.question-description {
  color: #6b7280;
  margin-bottom: 20px;
  line-height: 1.6;
}

.question-image {
  margin: 20px 0;
  text-align: center;
}

.question-image img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.options-area {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.option-item {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 16px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
}

.option-item:hover {
  border-color: #dc2626;
  box-shadow: 0 2px 8px rgba(220, 38, 38, 0.1);
}

.option-item.selected {
  border-color: #dc2626;
  background: #fef2f2;
}

.option-item.correct {
  border-color: #10b981;
  background: #f0fdf4;
}

.option-item.wrong {
  border-color: #ef4444;
  background: #fef2f2;
}

.option-marker {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  color: #374151;
  flex-shrink: 0;
}

.selected .option-marker {
  background: #dc2626;
  color: white;
}

.correct .option-marker {
  background: #10b981;
  color: white;
}

.wrong .option-marker {
  background: #ef4444;
  color: white;
}

.option-content {
  flex: 1;
}

.option-text {
  font-size: 15px;
  color: #1f2937;
  line-height: 1.5;
  margin-bottom: 4px;
}

.option-desc {
  font-size: 13px;
  color: #6b7280;
  line-height: 1.4;
}

.option-icon {
  margin-left: 10px;
  font-size: 18px;
}

.correct-icon {
  color: #10b981;
}

.wrong-icon {
  color: #ef4444;
}

.explanation-area {
  margin-top: 25px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 8px;
  border-left: 4px solid #dc2626;
}

.explanation-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-weight: 600;
  color: #1f2937;
}

.explanation-content {
  color: #374151;
  line-height: 1.6;
  margin-bottom: 15px;
}

.explanation-tips {
  margin-top: 15px;
  padding: 15px;
  background: #fff7ed;
  border-radius: 6px;
  border-left: 3px solid #f59e0b;
}

.tips-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  font-weight: 600;
  color: #92400e;
}

.tips-content {
  color: #92400e;
  line-height: 1.5;
  font-size: 14px;
}

.action-area {
  background: white;
  border-top: 1px solid #f0f0f0;
  padding: 20px;
  margin-top: auto;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 25px;
}

.prev-btn,
.next-btn,
.finish-btn {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.finish-btn {
  background: #10b981;
  border-color: #10b981;
}

.finish-btn:hover {
  background: #059669;
  border-color: #059669;
}

.question-navigation {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.nav-title {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 12px;
}

.nav-items {
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.nav-item {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid #e5e7eb;
  background: white;
  color: #6b7280;
}

.nav-item:hover {
  border-color: #dc2626;
  background: #fef2f2;
  color: #dc2626;
}

.nav-item.current {
  border-color: #dc2626;
  background: #dc2626;
  color: white;
}

.nav-item.answered:not(.current) {
  border-color: #10b981;
  background: #f0fdf4;
  color: #10b981;
}

.nav-item.unanswered:not(.current) {
  border-color: #f59e0b;
  background: #fff7ed;
  color: #f59e0b;
}

.submit-dialog {
  text-align: center;
}

.submit-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.submit-icon {
  font-size: 48px;
  color: #f59e0b;
}

.submit-text p {
  margin: 0;
  color: #374151;
}

.submit-stats {
  font-size: 14px;
  color: #6b7280;
}

.unanswered-warning {
  color: #ef4444;
  font-weight: 500;
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .test-progress {
    align-items: center;
    width: 100%;
  }
  
  .progress-bar {
    width: 100%;
  }
  
  .question-card {
    padding: 20px;
  }
  
  .option-item {
    padding: 12px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .nav-items {
    gap: 6px;
  }
  
  .nav-item {
    width: 32px;
    height: 32px;
  }
}
</style>