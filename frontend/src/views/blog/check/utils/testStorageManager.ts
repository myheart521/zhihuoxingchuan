/**
 * 防诈测试本地存储管理工具
 * 管理测试结果、AI建议、测试进度等数据的本地缓存
 */

export interface TestResult {
  id: string
  testId: number
  score: number
  totalQuestions: number
  correctAnswers: number
  completedAt: string
  duration: number // 测试用时（秒）
  answers: TestAnswer[]
  category: string
  difficulty: 'easy' | 'medium' | 'hard'
}

export interface TestAnswer {
  questionId: number
  selectedOption: string
  isCorrect: boolean
  timeSpent: number // 答题用时（秒）
}

export interface TestProgress {
  testId: number
  currentQuestionIndex: number
  answers: TestAnswer[]
  startTime: string
  lastUpdated: string
}

export interface AIAdvice {
  testId: number
  resultId: string
  advice: string
  riskLevel: 'low' | 'medium' | 'high'
  suggestions: string[]
  createdAt: string
  category: string
}

export interface UserStats {
  totalTests: number
  totalScore: number
  averageScore: number
  completedCategories: string[]
  bestCategory: string
  worstCategory: string
  improvementAreas: string[]
  lastUpdated: string
}

class TestStorageManager {
  private readonly STORAGE_KEYS = {
    TEST_RESULTS: 'fraud_test_results',
    TEST_PROGRESS: 'fraud_test_progress',
    AI_ADVICE: 'fraud_ai_advice',
    USER_STATS: 'fraud_user_stats',
    SETTINGS: 'fraud_test_settings'
  }

  /**
   * 保存测试结果
   */
  saveTestResult(result: TestResult): void {
    try {
      const results = this.getAllTestResults()
      const existingIndex = results.findIndex(r => r.testId === result.testId)
      
      if (existingIndex >= 0) {
        results[existingIndex] = result
      } else {
        results.push(result)
      }
      
      localStorage.setItem(this.STORAGE_KEYS.TEST_RESULTS, JSON.stringify(results))
      this.updateUserStats()
      
      console.log('测试结果已保存:', result)
    } catch (error) {
      console.error('保存测试结果失败:', error)
    }
  }

  /**
   * 获取单个测试结果
   */
  getTestResult(testId: number): TestResult | null {
    try {
      const results = this.getAllTestResults()
      return results.find(r => r.testId === testId) || null
    } catch (error) {
      console.error('获取测试结果失败:', error)
      return null
    }
  }

  /**
   * 获取所有测试结果
   */
  getAllTestResults(): TestResult[] {
    try {
      const data = localStorage.getItem(this.STORAGE_KEYS.TEST_RESULTS)
      return data ? JSON.parse(data) : []
    } catch (error) {
      console.error('获取测试结果列表失败:', error)
      return []
    }
  }

  /**
   * 删除测试结果
   */
  deleteTestResult(testId: number): void {
    try {
      const results = this.getAllTestResults()
      const filteredResults = results.filter(r => r.testId !== testId)
      localStorage.setItem(this.STORAGE_KEYS.TEST_RESULTS, JSON.stringify(filteredResults))
      
      // 同时删除相关的AI建议
      this.deleteAIAdvice(testId)
      this.updateUserStats()
      
      console.log('测试结果已删除:', testId)
    } catch (error) {
      console.error('删除测试结果失败:', error)
    }
  }

  /**
   * 保存测试进度
   */
  saveTestProgress(progress: TestProgress): void {
    try {
      const allProgress = this.getAllTestProgress()
      const existingIndex = allProgress.findIndex(p => p.testId === progress.testId)
      
      if (existingIndex >= 0) {
        allProgress[existingIndex] = progress
      } else {
        allProgress.push(progress)
      }
      
      localStorage.setItem(this.STORAGE_KEYS.TEST_PROGRESS, JSON.stringify(allProgress))
      console.log('测试进度已保存:', progress)
    } catch (error) {
      console.error('保存测试进度失败:', error)
    }
  }

  /**
   * 获取测试进度
   */
  getTestProgress(testId: number): TestProgress | null {
    try {
      const allProgress = this.getAllTestProgress()
      return allProgress.find(p => p.testId === testId) || null
    } catch (error) {
      console.error('获取测试进度失败:', error)
      return null
    }
  }

  /**
   * 获取所有测试进度
   */
  getAllTestProgress(): TestProgress[] {
    try {
      const data = localStorage.getItem(this.STORAGE_KEYS.TEST_PROGRESS)
      return data ? JSON.parse(data) : []
    } catch (error) {
      console.error('获取测试进度列表失败:', error)
      return []
    }
  }

  /**
   * 清除测试进度
   */
  clearTestProgress(testId: number): void {
    try {
      const allProgress = this.getAllTestProgress()
      const filteredProgress = allProgress.filter(p => p.testId !== testId)
      localStorage.setItem(this.STORAGE_KEYS.TEST_PROGRESS, JSON.stringify(filteredProgress))
      console.log('测试进度已清除:', testId)
    } catch (error) {
      console.error('清除测试进度失败:', error)
    }
  }

  /**
   * 保存AI建议
   */
  saveAIAdvice(advice: AIAdvice): void {
    try {
      const allAdvice = this.getAllAIAdvice()
      const existingIndex = allAdvice.findIndex(a => a.testId === advice.testId)
      
      if (existingIndex >= 0) {
        allAdvice[existingIndex] = advice
      } else {
        allAdvice.push(advice)
      }
      
      localStorage.setItem(this.STORAGE_KEYS.AI_ADVICE, JSON.stringify(allAdvice))
      console.log('AI建议已保存:', advice)
    } catch (error) {
      console.error('保存AI建议失败:', error)
    }
  }

  /**
   * 获取AI建议
   */
  getAIAdvice(testId: number): AIAdvice | null {
    try {
      const allAdvice = this.getAllAIAdvice()
      return allAdvice.find(a => a.testId === testId) || null
    } catch (error) {
      console.error('获取AI建议失败:', error)
      return null
    }
  }

  /**
   * 获取所有AI建议
   */
  getAllAIAdvice(): AIAdvice[] {
    try {
      const data = localStorage.getItem(this.STORAGE_KEYS.AI_ADVICE)
      return data ? JSON.parse(data) : []
    } catch (error) {
      console.error('获取AI建议列表失败:', error)
      return []
    }
  }

  /**
   * 删除AI建议
   */
  deleteAIAdvice(testId: number): void {
    try {
      const allAdvice = this.getAllAIAdvice()
      const filteredAdvice = allAdvice.filter(a => a.testId !== testId)
      localStorage.setItem(this.STORAGE_KEYS.AI_ADVICE, JSON.stringify(filteredAdvice))
      console.log('AI建议已删除:', testId)
    } catch (error) {
      console.error('删除AI建议失败:', error)
    }
  }

  /**
   * 更新用户统计数据
   */
  private updateUserStats(): void {
    try {
      const results = this.getAllTestResults()
      
      if (results.length === 0) {
        localStorage.removeItem(this.STORAGE_KEYS.USER_STATS)
        return
      }
      
      const totalTests = results.length
      const totalScore = results.reduce((sum, r) => sum + r.score, 0)
      const averageScore = Math.round(totalScore / totalTests)
      
      // 按分类统计
      const categoryStats = results.reduce((acc, result) => {
        if (!acc[result.category]) {
          acc[result.category] = { scores: [], count: 0 }
        }
        acc[result.category].scores.push(result.score)
        acc[result.category].count++
        return acc
      }, {} as Record<string, { scores: number[], count: number }>)
      
      const completedCategories = Object.keys(categoryStats)
      
      // 找出最好和最差的分类
      let bestCategory = ''
      let worstCategory = ''
      let bestAvg = 0
      let worstAvg = 100
      
      Object.entries(categoryStats).forEach(([category, stats]) => {
        const avg = stats.scores.reduce((sum, score) => sum + score, 0) / stats.scores.length
        if (avg > bestAvg) {
          bestAvg = avg
          bestCategory = category
        }
        if (avg < worstAvg) {
          worstAvg = avg
          worstCategory = category
        }
      })
      
      // 识别需要改进的领域
      const improvementAreas = Object.entries(categoryStats)
        .filter(([, stats]) => {
          const avg = stats.scores.reduce((sum, score) => sum + score, 0) / stats.scores.length
          return avg < 70 // 平均分低于70的分类
        })
        .map(([category]) => category)
      
      const userStats: UserStats = {
        totalTests,
        totalScore,
        averageScore,
        completedCategories,
        bestCategory,
        worstCategory,
        improvementAreas,
        lastUpdated: new Date().toISOString()
      }
      
      localStorage.setItem(this.STORAGE_KEYS.USER_STATS, JSON.stringify(userStats))
      console.log('用户统计已更新:', userStats)
    } catch (error) {
      console.error('更新用户统计失败:', error)
    }
  }

  /**
   * 获取用户统计数据
   */
  getUserStats(): UserStats | null {
    try {
      const data = localStorage.getItem(this.STORAGE_KEYS.USER_STATS)
      return data ? JSON.parse(data) : null
    } catch (error) {
      console.error('获取用户统计失败:', error)
      return null
    }
  }

  /**
   * 清除所有数据
   */
  clearAllData(): void {
    try {
      Object.values(this.STORAGE_KEYS).forEach(key => {
        localStorage.removeItem(key)
      })
      console.log('所有防诈测试数据已清除')
    } catch (error) {
      console.error('清除数据失败:', error)
    }
  }

  /**
   * 导出数据（用于备份）
   */
  exportData(): object {
    try {
      const data = {}
      Object.entries(this.STORAGE_KEYS).forEach(([name, key]) => {
        const value = localStorage.getItem(key)
        if (value) {
          data[name] = JSON.parse(value)
        }
      })
      return data
    } catch (error) {
      console.error('导出数据失败:', error)
      return {}
    }
  }

  /**
   * 导入数据（用于恢复）
   */
  importData(data: object): boolean {
    try {
      Object.entries(data).forEach(([name, value]) => {
        const key = this.STORAGE_KEYS[name as keyof typeof this.STORAGE_KEYS]
        if (key) {
          localStorage.setItem(key, JSON.stringify(value))
        }
      })
      console.log('数据导入成功')
      return true
    } catch (error) {
      console.error('导入数据失败:', error)
      return false
    }
  }

  /**
   * 获取存储使用情况
   */
  getStorageInfo(): { used: number, total: number, percentage: number } {
    try {
      let used = 0
      Object.values(this.STORAGE_KEYS).forEach(key => {
        const value = localStorage.getItem(key)
        if (value) {
          used += value.length
        }
      })
      
      // 估算localStorage总容量（通常为5-10MB）
      const total = 5 * 1024 * 1024 // 5MB
      const percentage = (used / total) * 100
      
      return { used, total, percentage }
    } catch (error) {
      console.error('获取存储信息失败:', error)
      return { used: 0, total: 0, percentage: 0 }
    }
  }
}

// 创建单例实例
export const testStorageManager = new TestStorageManager()

// 默认导出
export default testStorageManager 