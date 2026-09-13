import type { TestResultVO, TestVO } from '@/api/blog/check'

// 本地存储键名
const STORAGE_KEYS = {
  TEST_RESULTS: 'fraud_check_test_results',
  TEST_PROGRESS: 'fraud_check_test_progress',
  USER_STATS: 'fraud_check_user_stats'
}

// 用户统计数据接口
export interface UserStatsVO {
  totalTests: number
  completedTests: number
  averageScore: number
  totalScore: number
  lastTestTime: string
  bestScore: number
  testHistory: TestHistoryVO[]
}

export interface TestHistoryVO {
  testId: number
  testTitle: string
  score: number
  completedAt: string
  timeUsed: number
}

export interface TestProgressVO {
  testId: number
  currentQuestionIndex: number
  answers: Record<number, string>
  startTime: string
  timeLeft: number
}

/**
 * 防诈骗测试本地存储工具类
 */
export class FraudCheckStorage {
  
  /**
   * 保存测试结果
   */
  static saveTestResult(result: TestResultVO): void {
    try {
      const results = this.getTestResults()
      
      // 检查是否已存在该测试结果，如果存在则更新
      const existingIndex = results.findIndex(r => r.testId === result.testId)
      if (existingIndex !== -1) {
        results[existingIndex] = result
      } else {
        results.push(result)
      }
      
      localStorage.setItem(STORAGE_KEYS.TEST_RESULTS, JSON.stringify(results))
      
      // 更新用户统计数据
      this.updateUserStats(result)
      
      // 清除该测试的进度数据
      this.clearTestProgress(result.testId)
      
    } catch (error) {
      console.error('保存测试结果失败:', error)
    }
  }
  
  /**
   * 获取所有测试结果
   */
  static getTestResults(): TestResultVO[] {
    try {
      const results = localStorage.getItem(STORAGE_KEYS.TEST_RESULTS)
      return results ? JSON.parse(results) : []
    } catch (error) {
      console.error('获取测试结果失败:', error)
      return []
    }
  }
  
  /**
   * 根据测试ID获取测试结果
   */
  static getTestResultById(testId: number): TestResultVO | null {
    try {
      const results = this.getTestResults()
      return results.find(r => r.testId === testId) || null
    } catch (error) {
      console.error('获取测试结果失败:', error)
      return null
    }
  }
  
  /**
   * 删除测试结果（用于重新测试）
   */
  static deleteTestResult(testId: number): void {
    try {
      const results = this.getTestResults()
      const filteredResults = results.filter(r => r.testId !== testId)
      localStorage.setItem(STORAGE_KEYS.TEST_RESULTS, JSON.stringify(filteredResults))
      
      // 重新计算用户统计数据
      this.recalculateUserStats()
    } catch (error) {
      console.error('删除测试结果失败:', error)
    }
  }
  
  /**
   * 保存测试进度
   */
  static saveTestProgress(progress: TestProgressVO): void {
    try {
      const allProgress = this.getAllTestProgress()
      const existingIndex = allProgress.findIndex(p => p.testId === progress.testId)
      
      if (existingIndex !== -1) {
        allProgress[existingIndex] = progress
      } else {
        allProgress.push(progress)
      }
      
      localStorage.setItem(STORAGE_KEYS.TEST_PROGRESS, JSON.stringify(allProgress))
    } catch (error) {
      console.error('保存测试进度失败:', error)
    }
  }
  
  /**
   * 获取测试进度
   */
  static getTestProgress(testId: number): TestProgressVO | null {
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
  static getAllTestProgress(): TestProgressVO[] {
    try {
      const progress = localStorage.getItem(STORAGE_KEYS.TEST_PROGRESS)
      return progress ? JSON.parse(progress) : []
    } catch (error) {
      console.error('获取测试进度失败:', error)
      return []
    }
  }
  
  /**
   * 清除测试进度
   */
  static clearTestProgress(testId: number): void {
    try {
      const allProgress = this.getAllTestProgress()
      const filteredProgress = allProgress.filter(p => p.testId !== testId)
      localStorage.setItem(STORAGE_KEYS.TEST_PROGRESS, JSON.stringify(filteredProgress))
    } catch (error) {
      console.error('清除测试进度失败:', error)
    }
  }
  
  /**
   * 获取用户统计数据
   */
  static getUserStats(): UserStatsVO {
    try {
      const stats = localStorage.getItem(STORAGE_KEYS.USER_STATS)
      if (stats) {
        return JSON.parse(stats)
      } else {
        // 如果没有统计数据，则初始化
        const initialStats: UserStatsVO = {
          totalTests: 0,
          completedTests: 0,
          averageScore: 0,
          totalScore: 0,
          lastTestTime: '',
          bestScore: 0,
          testHistory: []
        }
        this.saveUserStats(initialStats)
        return initialStats
      }
    } catch (error) {
      console.error('获取用户统计数据失败:', error)
      return {
        totalTests: 0,
        completedTests: 0,
        averageScore: 0,
        totalScore: 0,
        lastTestTime: '',
        bestScore: 0,
        testHistory: []
      }
    }
  }
  
  /**
   * 保存用户统计数据
   */
  static saveUserStats(stats: UserStatsVO): void {
    try {
      localStorage.setItem(STORAGE_KEYS.USER_STATS, JSON.stringify(stats))
    } catch (error) {
      console.error('保存用户统计数据失败:', error)
    }
  }
  
  /**
   * 更新用户统计数据
   */
  static updateUserStats(result: TestResultVO): void {
    try {
      const stats = this.getUserStats()
      
      // 检查是否是重新测试
      const isRetake = stats.testHistory.some(h => h.testId === result.testId)
      
      if (!isRetake) {
        stats.totalTests++
      }
      
      stats.completedTests++
      stats.totalScore += result.score
      stats.averageScore = Math.round(stats.totalScore / stats.completedTests)
      stats.lastTestTime = result.completedAt
      stats.bestScore = Math.max(stats.bestScore, result.score)
      
      // 更新测试历史
      const existingHistoryIndex = stats.testHistory.findIndex(h => h.testId === result.testId)
      const historyItem: TestHistoryVO = {
        testId: result.testId,
        testTitle: result.testTitle,
        score: result.score,
        completedAt: result.completedAt,
        timeUsed: result.timeUsed
      }
      
      if (existingHistoryIndex !== -1) {
        stats.testHistory[existingHistoryIndex] = historyItem
      } else {
        stats.testHistory.push(historyItem)
      }
      
      // 按完成时间排序，最新的在前
      stats.testHistory.sort((a, b) => new Date(b.completedAt).getTime() - new Date(a.completedAt).getTime())
      
      this.saveUserStats(stats)
    } catch (error) {
      console.error('更新用户统计数据失败:', error)
    }
  }
  
  /**
   * 重新计算用户统计数据
   */
  static recalculateUserStats(): void {
    try {
      const results = this.getTestResults()
      
      if (results.length === 0) {
        const emptyStats: UserStatsVO = {
          totalTests: 0,
          completedTests: 0,
          averageScore: 0,
          totalScore: 0,
          lastTestTime: '',
          bestScore: 0,
          testHistory: []
        }
        this.saveUserStats(emptyStats)
        return
      }
      
      const totalScore = results.reduce((sum, r) => sum + r.score, 0)
      const bestScore = Math.max(...results.map(r => r.score))
      const lastTestTime = results.reduce((latest, r) => 
        new Date(r.completedAt) > new Date(latest) ? r.completedAt : latest, 
        results[0].completedAt
      )
      
      const testHistory: TestHistoryVO[] = results.map(r => ({
        testId: r.testId,
        testTitle: r.testTitle,
        score: r.score,
        completedAt: r.completedAt,
        timeUsed: r.timeUsed
      })).sort((a, b) => new Date(b.completedAt).getTime() - new Date(a.completedAt).getTime())
      
      const stats: UserStatsVO = {
        totalTests: results.length,
        completedTests: results.length,
        averageScore: Math.round(totalScore / results.length),
        totalScore,
        lastTestTime,
        bestScore,
        testHistory
      }
      
      this.saveUserStats(stats)
    } catch (error) {
      console.error('重新计算用户统计数据失败:', error)
    }
  }
  
  /**
   * 清除所有数据
   */
  static clearAllData(): void {
    try {
      localStorage.removeItem(STORAGE_KEYS.TEST_RESULTS)
      localStorage.removeItem(STORAGE_KEYS.TEST_PROGRESS)
      localStorage.removeItem(STORAGE_KEYS.USER_STATS)
    } catch (error) {
      console.error('清除数据失败:', error)
    }
  }
  
  /**
   * 导出数据（用于备份）
   */
  static exportData(): string {
    try {
      const data = {
        testResults: this.getTestResults(),
        testProgress: this.getAllTestProgress(),
        userStats: this.getUserStats(),
        exportTime: new Date().toISOString()
      }
      return JSON.stringify(data, null, 2)
    } catch (error) {
      console.error('导出数据失败:', error)
      return ''
    }
  }
  
  /**
   * 导入数据（用于恢复）
   */
  static importData(dataString: string): boolean {
    try {
      const data = JSON.parse(dataString)
      
      if (data.testResults) {
        localStorage.setItem(STORAGE_KEYS.TEST_RESULTS, JSON.stringify(data.testResults))
      }
      if (data.testProgress) {
        localStorage.setItem(STORAGE_KEYS.TEST_PROGRESS, JSON.stringify(data.testProgress))
      }
      if (data.userStats) {
        localStorage.setItem(STORAGE_KEYS.USER_STATS, JSON.stringify(data.userStats))
      }
      
      return true
    } catch (error) {
      console.error('导入数据失败:', error)
      return false
    }
  }
}