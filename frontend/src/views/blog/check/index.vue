<template>
  <div class="self-test-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <span class="title-icon">🚩</span>
            红色知识自测中心
          </h1>
          <p class="page-subtitle">检验您的红色文化知识水平，传承革命精神</p>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <div class="stat-number">{{ totalQuestions }}</div>
            <div class="stat-label">题目总数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ completedTests }}</div>
            <div class="stat-label">已完成</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ averageScore }}%</div>
            <div class="stat-label">平均分</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 筛选和搜索 -->
    <div class="filter-section">
      <div class="filter-container">
        <div class="filter-left">
          <div class="mb-4 flex justify-between items-center">
            <div class="flex items-center space-x-4">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索红色知识测试..."
                :prefix-icon="Search"
                style="width: 300px"
                @input="handleSearch"
              />
              <el-select
                v-model="selectedCategory"
                placeholder="选择分类"
                clearable
                style="width: 150px"
                @change="loadTests"
              >
                <el-option label="全部" value="" />
                <el-option label="党史学习" value="party-history" />
                <el-option label="革命历史" value="revolution" />
                <el-option label="红色文化" value="red-culture" />
                <el-option label="英雄事迹" value="heroes" />
                <el-option label="时政热点" value="current-affairs" />
              </el-select>
              <el-select
                v-model="selectedDifficulty"
                placeholder="选择难度"
                clearable
                style="width: 150px"
                @change="loadTests"
              >
                <el-option label="全部" value="" />
                <el-option label="简单" value="easy" />
                <el-option label="中等" value="medium" />
                <el-option label="困难" value="hard" />
              </el-select>
              <el-button @click="resetFilters" :icon="Download">重置筛选</el-button>
            </div>
            <div class="flex space-x-2">
              <!-- 调试按钮 -->
              <el-button 
                type="warning" 
                size="small"
                @click="addTestResult"
              >
                添加测试结果
              </el-button>
              <el-button 
                type="success" 
                size="small"
                @click="clearAllResults"
              >
                清空所有结果
              </el-button>
              <el-button type="primary" @click="createTest">创建新测试</el-button>
            </div>
          </div>
        </div>
        <div class="filter-right">
          <el-button type="primary" :icon="Plus" @click="showCreateDialog = true" class="create-btn">
            创建新题目
          </el-button>
        </div>
      </div>
    </div>

    <!-- 题目列表 -->
    <div class="test-list">
      <div class="list-container">
        <div
          v-for="test in filteredTests"
          :key="test.id"
          class="test-card"
          :class="{ 'completed': test.status === 'completed' }"
          @click="startTest(test)"
        >
          <div class="card-header">
            <div class="test-category" :class="`category-${test.category}`">
              {{ getCategoryLabel(test.category) }}
            </div>
            <div class="test-difficulty" :class="`difficulty-${test.difficulty}`">
              {{ getDifficultyLabel(test.difficulty) }}
            </div>
          </div>
          
          <div class="card-content">
            <h3 class="test-title">{{ test.title }}</h3>
            <p class="test-description">{{ test.description }}</p>
            
            <div class="test-meta">
              <div class="meta-item">
                <el-icon><Clock /></el-icon>
                <span>{{ test.duration }}分钟</span>
              </div>
              <div class="meta-item">
                <el-icon><Document /></el-icon>
                <span>{{ test.questionCount }}题</span>
              </div>
              <div class="meta-item">
                <el-icon><User /></el-icon>
                <span>{{ test.participantCount }}人参与</span>
              </div>
            </div>
          </div>
          
          <div class="card-footer">
            <div class="test-score" v-if="test.userScore !== null">
              <span class="score-label">您的成绩：</span>
              <span class="score-value" :class="getScoreClass(test.userScore)">
                {{ test.userScore }}分
              </span>
            </div>
            <div class="test-actions">
              <el-button 
                v-if="test.status !== 'completed'"
                type="primary" 
                size="small" 
                class="start-btn"
                :disabled="false"
              >
                {{ test.status === 'in-progress' ? '继续测试' : '开始测试' }}
              </el-button>
              
              <template v-if="test.status === 'completed'">
                <el-button 
                  type="info" 
                  size="small" 
                  plain
                  @click.stop="reviewTest(test)"
                >
                  查看解析
                </el-button>
                <el-button 
                  type="warning" 
                  size="small" 
                  plain
                  @click.stop="retakeTest(test)"
                >
                  重新测试
                </el-button>
              </template>
            </div>
          </div>
          
          <!-- 进度条 -->
          <div v-if="test.progress > 0 && test.progress < 100" class="progress-bar">
            <div class="progress-fill" :style="{ width: test.progress + '%' }"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="filteredTests.length === 0" class="empty-state">
      <div class="empty-icon">🚩</div>
      <div class="empty-title">暂无符合条件的红色知识测试题目</div>
      <div class="empty-description">尝试调整筛选条件或创建新的测试题目</div>
      <el-button type="primary" @click="resetFilters">重置筛选</el-button>
    </div>

    <!-- 创建题目对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      title="创建新的红色知识测试题目"
      width="600px"
      class="create-dialog"
    >
      <el-form :model="newTest" label-width="120px">
        <el-form-item label="题目标题">
          <el-input v-model="newTest.title" placeholder="请输入题目标题" />
        </el-form-item>
        <el-form-item label="题目描述">
          <el-input 
            v-model="newTest.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入题目描述"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="newTest.category" placeholder="选择分类">
            <el-option
              v-for="category in categories"
              :key="category.value"
              :label="category.label"
              :value="category.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="newTest.difficulty" placeholder="选择难度">
            <el-option label="简单" value="easy" />
            <el-option label="中等" value="medium" />
            <el-option label="困难" value="hard" />
          </el-select>
        </el-form-item>
        <el-form-item label="预计时长">
          <el-input-number v-model="newTest.duration" :min="1" :max="60" /> 分钟
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="createTest">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Plus, Clock, Document, User, Download } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { FraudCheckApi } from '@/api/blog/check'
import { FraudCheckStorage } from '@/utils/storage/fraudCheck'
import type { TestVO, TestCategoryVO, TestResultVO } from '@/api/blog/check'

const router = useRouter()

// 响应式数据
const testList = ref<TestVO[]>([])
const categories = ref<TestCategoryVO[]>([])
const userStats = ref<any>({})
const isLoading = ref(false)

// 筛选条件
const searchKeyword = ref('')
const selectedCategory = ref('')
const selectedDifficulty = ref('')

// 创建测试表单
const showCreateDialog = ref(false)
const newTest = ref({
  title: '',
  description: '',
  category: '',
  difficulty: 'medium' as 'easy' | 'medium' | 'hard',
  duration: 15
})

// 计算属性
const filteredTests = computed(() => {
  let result = testList.value
  
  if (searchKeyword.value) {
    result = result.filter(test => 
      test.title.includes(searchKeyword.value) || 
      test.description.includes(searchKeyword.value)
    )
  }
  
  if (selectedCategory.value) {
    result = result.filter(test => test.category === selectedCategory.value)
  }
  
  if (selectedDifficulty.value) {
    result = result.filter(test => test.difficulty === selectedDifficulty.value)
  }
  
  return result
})

const totalQuestions = computed(() => {
  return testList.value.length
})

const completedTests = computed(() => {
  return testList.value.filter(test => test.status === 'completed').length
})

const averageScore = computed(() => {
  const completedTestsWithScores = testList.value.filter(
    test => test.status === 'completed' && test.userScore !== undefined
  )
  
  if (completedTestsWithScores.length === 0) return 0
  
  const totalScore = completedTestsWithScores.reduce(
    (sum, test) => sum + (test.userScore || 0), 
    0
  )
  
  return Math.round(totalScore / completedTestsWithScores.length)
})

// 方法
const getCategoryLabel = (category: string) => {
  if (!Array.isArray(categories.value) || categories.value.length === 0) {
    return category
  }
  
  const found = categories.value.find(c => c.value === category)
  return found ? found.label : category
}

const getDifficultyLabel = (difficulty: string) => {
  const labels = {
    easy: '简单',
    medium: '中等',
    hard: '困难'
  }
  return labels[difficulty as keyof typeof labels] || difficulty
}

const getScoreClass = (score?: number) => {
  if (score === undefined) return ''
  
  if (score >= 90) return 'score-excellent'
  if (score >= 80) return 'score-good' 
  if (score >= 70) return 'score-average'
  return 'score-poor'
}

/**
 * 加载测试列表
 */
const loadTests = async () => {
  isLoading.value = true
  try {
    const tests = await FraudCheckApi.getTestList({
      category: selectedCategory.value || undefined,
      difficulty: selectedDifficulty.value || undefined
    })
    
    console.log('API返回的测试列表:', tests)
    
    // 为每个测试添加用户成绩信息
    const testsWithScores = tests.map(test => {
      const result = FraudCheckStorage.getTestResultById(test.id)
      console.log(`测试${test.id}的本地结果:`, result)
      
      const enhancedTest = {
        ...test,
        userScore: result?.score,
        status: result ? 'completed' as const : 'pending' as const
      }
      
      console.log(`增强后的测试${test.id}:`, enhancedTest)
      return enhancedTest
    })
    
    testList.value = testsWithScores
    console.log('最终测试列表:', testList.value)
    console.log('完成的测试数量:', testList.value.filter(t => t.status === 'completed').length)
    
  } catch (error) {
    console.error('加载测试列表失败:', error)
    
    // 如果API调用失败，提供一些红色知识测试数据
    console.log('使用红色知识测试数据')
    const mockTests = [
      {
        id: 1,
        title: '中国共产党党史知识测试',
        description: '测试您对中国共产党历史的了解程度',
        category: 'party-history',
        difficulty: 'medium' as const,
        duration: 20,
        questionCount: 15,
        participantCount: 2156,
        progress: 0,
        questions: [],
        timeLimit: 1200
      },
      {
        id: 2,
        title: '红军长征历史知识测试',
        description: '了解红军长征的伟大历程和历史意义',
        category: 'revolution',
        difficulty: 'hard' as const,
        duration: 25,
        questionCount: 20,
        participantCount: 1834,
        progress: 0,
        questions: [],
        timeLimit: 1500
      },
      {
        id: 3,
        title: '革命英雄事迹知识测试',
        description: '学习革命先烈的英勇事迹和崇高精神',
        category: 'heroes',
        difficulty: 'easy' as const,
        duration: 15,
        questionCount: 12,
        participantCount: 2487,
        progress: 0,
        questions: [],
        timeLimit: 900
      },
      {
        id: 4,
        title: '红色文化传承知识测试',
        description: '传承和弘扬红色文化的深刻内涵',
        category: 'red-culture',
        difficulty: 'medium' as const,
        duration: 18,
        questionCount: 16,
        participantCount: 1623,
        progress: 0,
        questions: [],
        timeLimit: 1080
      },
      {
        id: 5,
        title: '新时代党的理论知识测试',
        description: '学习新时代中国特色社会主义思想',
        category: 'current-affairs',
        difficulty: 'hard' as const,
        duration: 30,
        questionCount: 25,
        participantCount: 987,
        progress: 0,
        questions: [],
        timeLimit: 1800
      },
      {
        id: 6,
        title: '抗日战争历史知识测试',
        description: '铭记抗日战争历史，弘扬爱国主义精神',
        category: 'revolution',
        difficulty: 'medium' as const,
        duration: 22,
        questionCount: 18,
        participantCount: 1456,
        progress: 0,
        questions: [],
        timeLimit: 1320
      }
    ]
    
    // 添加本地存储的测试结果
    const testsWithScores = mockTests.map(test => {
      const result = FraudCheckStorage.getTestResultById(test.id)
      return {
        ...test,
        userScore: result?.score,
        status: result ? 'completed' as const : 'pending' as const
      }
    })
    
    testList.value = testsWithScores
    
    ElMessage.warning('无法加载在线数据，使用本地红色知识测试数据')
  } finally {
    isLoading.value = false
  }
}

/**
 * 加载分类列表
 */
const loadCategories = async () => {
  try {
    const categoryData = await FraudCheckApi.getTestCategories()
    categories.value = categoryData  // 直接赋值，不需要转换
  } catch (error) {
    console.error('加载分类失败:', error)
    // 提供红色知识分类
    categories.value = [
      { label: '🚩 党史学习', value: 'party-history', icon: '🚩' },
      { label: '⚔️ 革命历史', value: 'revolution', icon: '⚔️' },
      { label: '🎌 红色文化', value: 'red-culture', icon: '🎌' },
      { label: '🏆 英雄事迹', value: 'heroes', icon: '🏆' },
      { label: '📰 时政热点', value: 'current-affairs', icon: '📰' }
    ]
  }
}

/**
 * 加载用户统计数据
 */
const loadUserStats = () => {
  userStats.value = FraudCheckStorage.getUserStats()
}

/**
 * 开始测试
 */
const startTest = (test: TestVO) => {
  // 检查是否有进行中的测试进度
  const progress = FraudCheckStorage.getTestProgress(test.id)
  
  if (progress) {
    ElMessageBox.confirm(
      '检测到您有未完成的测试进度，是否继续之前的测试？',
      '继续测试',
      {
        confirmButtonText: '继续测试',
        cancelButtonText: '重新开始',
        type: 'warning',
      }
    ).then(() => {
      // 继续之前的测试
      router.push({
        path: '/blog/check/test',
        query: { testId: test.id, resume: 'true' }
      })
    }).catch(() => {
      // 清除进度，重新开始
      FraudCheckStorage.clearTestProgress(test.id)
      router.push({
        path: '/blog/check/test',
        query: { testId: test.id }
      })
    })
  } else {
    // 直接开始新测试
    router.push({
      path: '/blog/check/test',
      query: { testId: test.id }
    })
  }
}

/**
 * 查看测试解析
 */
const reviewTest = (test: TestVO) => {
  router.push({
    path: '/blog/check/result',
    query: {
      testId: test.id,
      fromStorage: 'true',
      reviewMode: 'true'
    }
  })
}

/**
 * 重做测试
 */
const retakeTest = (test: TestVO) => {
  ElMessageBox.confirm(
    '确定要重新测试吗？这将清除您之前的测试记录。',
    '确认重新测试',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 删除测试结果和进度
    FraudCheckStorage.deleteTestResult(test.id)
    FraudCheckStorage.clearTestProgress(test.id)
    
    ElMessage.success('已清除测试记录，开始重新测试')
    
    // 重新加载测试列表
    loadTests()
    
    // 跳转到测试页面
    router.push({
      path: '/blog/check/test',
      query: { testId: test.id }
    })
  })
}

/**
 * 搜索
 */
const handleSearch = () => {
  loadTests()
}

/**
 * 重置筛选
 */
const resetFilters = () => {
  searchKeyword.value = ''
  selectedCategory.value = ''
  selectedDifficulty.value = ''
  loadTests()
}

/**
 * 创建测试
 */
const createTest = () => {
  // 这里可以实现创建测试的逻辑
  ElMessage.info('创建测试功能开发中...')
}

/**
 * 添加测试结果（调试用）
 */
const addTestResult = () => {
  // 为前两个测试添加模拟结果
  if (testList.value.length > 0) {
    const testId = testList.value[0].id
    const mockResult: TestResultVO = {
      testId: testId,
      testTitle: testList.value[0].title,
      score: 85,
      correctCount: 8,
      totalCount: 10,
      wrongCount: 2,
      accuracy: 80,
      timeUsed: 600,
      completedAt: new Date().toISOString(),
      answers: {
        1: 'A', 2: 'B', 3: 'C', 4: 'A', 5: 'D',
        6: 'B', 7: 'C', 8: 'A', 9: 'D', 10: 'B'
      },
      details: []
    }
    
    // 保存结果到本地存储
    FraudCheckStorage.saveTestResult(mockResult)
    
    // 重新加载测试列表以更新状态
    loadTests()
    
    ElMessage.success(`已为测试 "${testList.value[0].title}" 添加模拟结果（85分）`)
  } else {
    ElMessage.warning('没有可用的测试，请先加载测试列表')
  }
}

/**
 * 清空所有结果（调试用）
 */
const clearAllResults = () => {
  ElMessageBox.confirm(
    '确定要清空所有测试结果吗？此操作不可撤销。',
    '确认清空',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 清空本地存储的所有测试结果
    FraudCheckStorage.clearAllData()
    
    // 重新加载测试列表
    loadTests()
    
    ElMessage.success('已清空所有测试结果')
  }).catch(() => {
    ElMessage.info('已取消清空操作')
  })
}

// 页面初始化
onMounted(async () => {
  await loadCategories()  // 先加载分类
  await loadTests()       // 再加载测试
  loadUserStats()         // 加载用户统计
})
</script>

<style scoped lang="scss">
// 红色知识主题色彩
$primary-red: #dc2626;
$secondary-red: #b91c1c;
$deep-red: #991b1b;
$light-red: #fca5a5;
$bg-light: #fef2f2;
$text-dark: #374151;
$border-light: #fecaca;
$gold-accent: #f59e0b;

.self-test-container {
  padding: 20px;
  background: linear-gradient(135deg, $bg-light 0%, #ffffff 100%);
  min-height: 100vh;
}

// 页面头部
.page-header {
  background: linear-gradient(135deg, $primary-red, $secondary-red);
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 24px;
  color: white;
  box-shadow: 0 8px 32px rgba($primary-red, 0.3);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .header-left {
    flex: 1;
  }

  .page-title {
    font-size: 32px;
    font-weight: 700;
    margin: 0 0 8px 0;
    display: flex;
    align-items: center;
    gap: 12px;

    .title-icon {
      font-size: 36px;
      animation: pulse 2s infinite;
    }
  }

  .page-subtitle {
    font-size: 16px;
    opacity: 0.9;
    margin: 0;
  }

  .header-stats {
    display: flex;
    gap: 32px;

    .stat-item {
      text-align: center;

      .stat-number {
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        opacity: 0.8;
      }
    }
  }
}

// 筛选区域
.filter-section {
  margin-bottom: 24px;

  .filter-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba($primary-red, 0.1);
    border: 1px solid $border-light;
  }

  .filter-left {
    display: flex;
    gap: 16px;
    flex: 1;

    .search-input {
      width: 300px;
    }

    .category-select,
    .difficulty-select {
      width: 140px;
    }
  }

  .filter-right {
    display: flex;
    gap: 12px;

    .create-btn {
      background: linear-gradient(135deg, $primary-red, $secondary-red);
      border: none;
      font-weight: 600;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px rgba($primary-red, 0.4);
      }
    }

    .reset-btn {
      border-color: $primary-red;
      color: $primary-red;

      &:hover {
        background: $primary-red;
        color: white;
      }
    }
  }
}

// 测试列表
.test-list {
  .list-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
    gap: 20px;
  }

  .test-card {
    background: white;
    border-radius: 16px;
    padding: 24px;
    box-shadow: 0 4px 20px rgba($primary-red, 0.1);
    border: 2px solid transparent;
    transition: all 0.3s ease;
    cursor: pointer;
    position: relative;
    overflow: hidden;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 32px rgba($primary-red, 0.2);
      border-color: $primary-red;
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .test-category {
        padding: 6px 12px;
        border-radius: 20px;
        font-size: 12px;
        font-weight: 600;

        // 红色知识主题分类样式
        &.category-party-history { 
          background: rgba(220, 38, 38, 0.15); 
          color: #991b1b; 
          border: 1px solid rgba(220, 38, 38, 0.3);
        }
        &.category-revolution { 
          background: rgba(185, 28, 28, 0.15); 
          color: #7f1d1d; 
          border: 1px solid rgba(185, 28, 28, 0.3);
        }
        &.category-red-culture { 
          background: rgba(239, 68, 68, 0.15); 
          color: #991b1b; 
          border: 1px solid rgba(239, 68, 68, 0.3);
        }
        &.category-heroes { 
          background: rgba(245, 158, 11, 0.15); 
          color: #92400e; 
          border: 1px solid rgba(245, 158, 11, 0.3);
        }
        &.category-current-affairs { 
          background: rgba(153, 27, 27, 0.15); 
          color: #7f1d1d; 
          border: 1px solid rgba(153, 27, 27, 0.3);
        }
      }

      .test-difficulty {
        padding: 4px 8px;
        border-radius: 12px;
        font-size: 11px;
        font-weight: 600;

        &.difficulty-easy { 
          background: rgba(220, 38, 38, 0.1); 
          color: #991b1b; 
        }
        &.difficulty-medium { 
          background: rgba(245, 158, 11, 0.1); 
          color: #92400e; 
        }
        &.difficulty-hard { 
          background: rgba(153, 27, 27, 0.1); 
          color: #7f1d1d; 
        }
      }
    }

    .card-content {
      margin-bottom: 20px;

      .test-title {
        font-size: 18px;
        font-weight: 600;
        color: $text-dark;
        margin-bottom: 8px;
        line-height: 1.4;
      }

      .test-description {
        font-size: 14px;
        color: #6b7280;
        line-height: 1.6;
        margin-bottom: 16px;
      }

      .test-meta {
        display: flex;
        gap: 16px;

        .meta-item {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 13px;
          color: #6b7280;

          .el-icon {
            font-size: 14px;
            color: $primary-red;
          }
        }
      }
    }

    .card-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .test-score {
        .score-label {
          font-size: 13px;
          color: #6b7280;
        }

        .score-value {
          font-size: 16px;
          font-weight: 600;
          margin-left: 4px;

          &.score-excellent { color: #dc2626; }
          &.score-good { color: #991b1b; }
          &.score-average { color: #f59e0b; }
          &.score-poor { color: #7f1d1d; }
        }
      }

      .test-actions {
        display: flex;
        gap: 8px;

        .start-btn {
          background: linear-gradient(135deg, $primary-red, $secondary-red);
          border: none;
          font-weight: 600;

          &:disabled {
            background: #e5e7eb;
            color: #9ca3af;
          }
        }
      }
    }

    .progress-bar {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 4px;
      background: #f3f4f6;

      .progress-fill {
        height: 100%;
        background: linear-gradient(90deg, $primary-red, $secondary-red);
        transition: width 0.3s ease;
      }
    }
  }
}

// 空状态
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;

  .empty-icon {
    font-size: 64px;
    margin-bottom: 16px;
    opacity: 0.5;
  }

  .empty-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 8px;
    color: $text-dark;
  }

  .empty-description {
    font-size: 14px;
    margin-bottom: 24px;
  }
}

// 对话框样式
.create-dialog {
  :deep(.el-dialog__header) {
    background: linear-gradient(135deg, $primary-red, $secondary-red);
    color: white;
    padding: 20px 24px;
    margin: 0;

    .el-dialog__title {
      color: white;
      font-weight: 600;
    }
  }

  :deep(.el-dialog__body) {
    padding: 24px;
  }
}

// 动画
@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

// 响应式设计
@media (max-width: 768px) {
  .self-test-container {
    padding: 16px;
  }

  .page-header {
    padding: 20px;

    .header-content {
      flex-direction: column;
      gap: 20px;
    }

    .header-stats {
      gap: 20px;
    }

    .page-title {
      font-size: 24px;
    }
  }

  .filter-container {
    flex-direction: column;
    gap: 16px;

    .filter-left {
      flex-direction: column;
      width: 100%;

      .search-input,
      .category-select,
      .difficulty-select {
        width: 100%;
      }
    }

    .filter-right {
      width: 100%;
      justify-content: stretch;

      .el-button {
        flex: 1;
      }
    }
  }

  .list-container {
    grid-template-columns: 1fr;
  }

  .test-card {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .test-card {
    .card-footer {
      flex-direction: column;
      gap: 12px;
      align-items: stretch;

      .test-actions {
        justify-content: stretch;

        .el-button {
          flex: 1;
        }
      }
    }
  }
}
</style>
