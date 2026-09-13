import request from '@/config/axios'
import { 
  testList, 
  categories, 
  getTestById, 
  getRandomQuestions,
  getQuestionsByCategory,
  getAllQuestions,
  getAllTests
} from './questions'

// 红色知识测试相关接口类型定义
export interface TestQuestionVO {
  id: number
  type: 'single' | 'multiple' | 'judge' | 'text'
  title: string
  description?: string
  image?: string
  score: number
  options: QuestionOptionVO[]
  explanation: string
  tips?: string
  category: string
  difficulty: 'easy' | 'medium' | 'hard'
  correctAnswer: string
}

export interface QuestionOptionVO {
  label: string
  text: string
  description?: string
  correct?: boolean
}

export interface TestVO {
  id: number
  title: string
  description: string
  category: string
  difficulty: 'easy' | 'medium' | 'hard'
  duration: number
  questionCount: number
  participantCount: number
  status: 'pending' | 'in-progress' | 'completed'
  progress: number
  userScore?: number
  questions: TestQuestionVO[]
  timeLimit: number
}

export interface TestResultVO {
  testId: number
  testTitle: string
  score: number
  correctCount: number
  totalCount: number
  wrongCount: number
  accuracy: number
  timeUsed: number
  completedAt: string
  answers: Record<number, string>
  details: TestAnswerDetailVO[]
}

export interface TestAnswerDetailVO {
  questionId: number
  questionIndex: number
  userAnswer: string
  correctAnswer: string
  isCorrect: boolean
  question: TestQuestionVO
}

export interface TestCategoryVO {
  label: string
  value: string
  icon: string
}

// 红色知识测试 API
export const FraudCheckApi = {
  /**
   * 获取红色知识测试分类列表
   */
  async getTestCategories(): Promise<TestCategoryVO[]> {
    return [
      { label: '🚩 党史学习', value: 'party-history', icon: '🚩' },
      { label: '⚔️ 革命历史', value: 'revolution', icon: '⚔️' },
      { label: '🎌 红色文化', value: 'red-culture', icon: '🎌' },
      { label: '🏆 英雄事迹', value: 'heroes', icon: '🏆' },
      { label: '📰 时政热点', value: 'current-affairs', icon: '📰' }
    ]
  },

  /**
   * 获取红色知识测试列表
   */
  async getTestList(params?: { category?: string; difficulty?: string }): Promise<TestVO[]> {
    // 使用 getAllTests 函数获取转换后的测试列表
    let result = getAllTests()
    
    if (params?.category) {
      result = result.filter(test => test.category === params.category)
    }
    
    if (params?.difficulty) {
      result = result.filter(test => test.difficulty === params.difficulty)
    }
    
    return result
  },

  /**
   * 获取红色知识测试详情（支持随机选题）
   */
  async getTestDetail(testId: number, randomQuestions: boolean = true): Promise<TestVO | null> {
    const test = getTestById(testId)
    if (!test) return null
    
    if (randomQuestions) {
      const questionCount = test.questions.length
      const randomQs = getRandomQuestions(test.category, questionCount)
      
      return {
        ...test,
        questions: randomQs
      }
    }
    
    return test
  },

  /**
   * 获取指定分类下的所有红色知识题目
   */
  async getCategoryQuestions(category: string): Promise<TestQuestionVO[]> {
    return getQuestionsByCategory(category)
  },

  /**
   * 获取所有红色知识题目
   */
  async getAllQuestions(): Promise<TestQuestionVO[]> {
    return getAllQuestions()
  },

  /**
   * 根据ID获取单个红色知识测试
   */
  async getTestById(testId: number): Promise<TestVO | null> {
    return getTestById(testId)
  },

  /**
   * 提交红色知识测试答案
   */
  async submitTestAnswers(
    testId: number,
    answers: Record<number, string>,
    timeUsed: number
  ): Promise<TestResultVO> {
    const test = getTestById(testId)
    if (!test) {
      throw new Error('测试不存在')
    }

    const currentQuestions = test.questions
    const details: TestAnswerDetailVO[] = []
    let correctCount = 0
    let totalScore = 0

    currentQuestions.forEach((question, index) => {
      const userAnswer = answers[question.id]
      const isCorrect = userAnswer === question.correctAnswer
      
      if (isCorrect) {
        correctCount++
        totalScore += question.score
      }

      details.push({
        questionId: question.id,
        questionIndex: index,
        userAnswer: userAnswer || '',
        correctAnswer: question.correctAnswer,
        isCorrect,
        question: question
      })
    })

    const totalQuestions = currentQuestions.length
    const wrongCount = totalQuestions - correctCount
    const accuracy = Math.round((correctCount / totalQuestions) * 100)

    const result: TestResultVO = {
      testId,
      testTitle: test.title,
      score: totalScore,
      correctCount,
      totalCount: totalQuestions,
      wrongCount,
      accuracy,
      timeUsed,
      completedAt: new Date().toISOString(),
      answers,
      details
    }

    return result
  },

  /**
   * 搜索红色知识题目
   */
  async searchQuestions(keyword: string, category?: string): Promise<TestQuestionVO[]> {
    let questions = getAllQuestions()
    
    if (category) {
      questions = getQuestionsByCategory(category)
    }
    
    return questions.filter(q => 
      q.title.includes(keyword) || 
      q.explanation?.includes(keyword) ||
      q.tips?.includes(keyword)
    )
  }
}

// 导出常用函数供其他地方使用
export { 
  getTestById, 
  getRandomQuestions, 
  getQuestionsByCategory, 
  getAllQuestions,
  getAllTests
}