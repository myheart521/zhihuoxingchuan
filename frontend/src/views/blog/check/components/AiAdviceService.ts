import { ref } from 'vue'
import { callAiModel } from '@/views/ai/aigcuniversity/utils/aiApi'
import type { TestResultVO } from '@/api/blog/check'

// AI建议服务类
export class AiAdviceService {
  private static instance: AiAdviceService
  private isGenerating = ref(false)

  private constructor() {}

  public static getInstance(): AiAdviceService {
    if (!AiAdviceService.instance) {
      AiAdviceService.instance = new AiAdviceService()
    }
    return AiAdviceService.instance
  }

  /**
   * 生成AI个性化建议
   */
  async generatePersonalizedAdvice(testResult: TestResultVO): Promise<string> {
    this.isGenerating.value = true
    
    try {
      // 构建提示词
      const prompt = this.buildPrompt(testResult)
      
      // 调用AI模型
      const response = await this.callAiModelWithTestResult(prompt)
      
      return response
    } catch (error) {
      console.error('AI建议生成失败:', error)
      return this.getFallbackAdvice(testResult)
    } finally {
      this.isGenerating.value = false
    }
  }

  /**
   * 构建AI提示词
   */
  private buildPrompt(testResult: TestResultVO): string {
    const { score, correctCount, totalCount, accuracy, details } = testResult
    
    // 分析错误类别
    const wrongCategories = new Set<string>()
    const wrongQuestions: Array<{category: string, title: string, userAnswer: string, correctAnswer: string}> = []
    const correctQuestions: Array<{category: string, title: string}> = []
    
    details.forEach(detail => {
      if (!detail.isCorrect) {
        wrongCategories.add(detail.question.category)
        wrongQuestions.push({
          category: detail.question.category,
          title: detail.question.title,
          userAnswer: detail.userAnswer || '未作答',
          correctAnswer: detail.correctAnswer
        })
      } else {
        correctQuestions.push({
          category: detail.question.category,
          title: detail.question.title
        })
      }
    })

    const categoryNames = {
      'party-history': '党史学习',
      'revolution': '革命历史', 
      'red-culture': '红色文化',
      'heroes': '英雄事迹',
      'current-affairs': '时政热点'
    }

    const wrongCategoryList = Array.from(wrongCategories).map(cat => 
      categoryNames[cat as keyof typeof categoryNames] || cat
    ).join('、')

    return `你是一位专业的红色教育专家，请根据用户的红色知识测试结果，为其提供个性化的学习建议。

## 用户测试结果分析：
- 测试名称：${testResult.testTitle}
- 总得分：${score}分
- 答题情况：答对${correctCount}题，答错${totalCount - correctCount}题，共${totalCount}题
- 正确率：${accuracy}%

## 答错题目的类别：
${wrongCategoryList || '无（全部答对）'}

## 具体错误分析：
${wrongQuestions.length > 0 ? wrongQuestions.map((q, index) => 
  `${index + 1}. 【${categoryNames[q.category as keyof typeof categoryNames] || q.category}】${q.title}
     用户选择：${q.userAnswer}
     正确答案：${q.correctAnswer}`
).join('\n') : '用户全部答对，表现优秀！'}

## 答对题目情况：
${correctQuestions.length > 0 ? correctQuestions.slice(0, 3).map((q, index) => 
  `${index + 1}. 【${categoryNames[q.category as keyof typeof categoryNames] || q.category}】${q.title}`
).join('\n') + (correctQuestions.length > 3 ? '\n...(还有其他题目答对)' : '') : '无'}

## 请求：
请基于以上分析，生成一份个性化的红色知识学习建议，包含以下内容：

1. **总体评价**（2-3句话概括用户的红色知识掌握水平）
2. **表现亮点**（基于答对的题目，肯定用户在红色知识方面的优势）
3. **需要加强的领域**（基于答错的题目类别，指出需要重点学习的红色知识领域）
4. **具体学习建议**（3-5条针对性建议，每条建议包含具体的学习方法和资源）
5. **实践活动建议**（2-3条将红色知识运用到实际生活中的建议）
6. **深入学习方向**（推荐的进阶学习内容或方法）

## 要求：
- 语言要生动有趣，富有感染力和教育意义
- 建议要具体可操作，结合当代青年特点
- 重点关注用户的薄弱环节，但也要充分肯定优点
- 字数控制在400-600字
- 体现红色文化的传承意义和时代价值
- 结构清晰，使用标题分段

请开始生成建议：`
  }

  /**
   * 调用AI模型
   */
  private async callAiModelWithTestResult(prompt: string): Promise<string> {
    try {
      // 构建消息格式
      const messages = [
        {
          role: 'system' as const,
          content: '你是一位专业的红色教育专家，专门为用户提供个性化的红色知识学习建议和指导。你的建议应该富有感染力、教育意义，能够激发用户学习红色文化的热情，传承红色基因。'
        },
        {
          role: 'user' as const,
          content: prompt
        }
      ]

      // 使用现有的 aiApi 调用AI模型
      const response = await callAiModel(messages)
      
      if (!response || response.trim().length === 0) {
        throw new Error('AI响应为空')
      }
      
      return response
    } catch (error) {
      console.error('调用AI模型失败:', error)
      throw error
    }
  }

  /**
   * 获取备用建议（当AI服务不可用时）
   */
  private getFallbackAdvice(testResult: TestResultVO): string {
    const { accuracy, correctCount, totalCount, details } = testResult
    
    // 分析错误类别
    const wrongCategories = new Set<string>()
    details.forEach(detail => {
      if (!detail.isCorrect) {
        wrongCategories.add(detail.question.category)
      }
    })

    const categoryNames = {
      'party-history': '党史学习',
      'revolution': '革命历史', 
      'red-culture': '红色文化',
      'heroes': '英雄事迹',
      'current-affairs': '时政热点'
    }

    let advice = ''
    
    // 根据成绩生成总体评价
    if (accuracy >= 90) {
      advice += '## 总体评价\n\n🎉 恭喜您！您在红色知识测试中表现优秀，显示出深厚的红色文化底蕴和强烈的爱国情怀。您对党史、革命历史等重要知识都有准确的掌握，体现了新时代青年应有的政治觉悟。\n\n'
    } else if (accuracy >= 70) {
      advice += '## 总体评价\n\n👍 您的红色知识基础较为扎实，在大部分题目上都能做出正确判断，说明您对党的历史和革命传统有一定的了解。继续保持学习热情，相信您会在红色文化学习道路上更进一步。\n\n'
    } else {
      advice += '## 总体评价\n\n⚠️ 通过这次测试，我们发现您的红色知识还有很大的提升空间。红色文化是我们民族的宝贵财富，建议您系统性地学习党史、革命史，增强爱国主义情感和理想信念。\n\n'
    }

    // 表现亮点
    advice += '## 表现亮点\n\n'
    if (correctCount > 0) {
      advice += `✅ 您在本次测试中答对了${correctCount}道题目，说明您在这些红色知识领域有良好的基础。这些知识将成为您传承红色基因、坚定理想信念的重要支撑。\n\n`
    } else {
      advice += '⚠️ 本次测试表明您需要从基础的红色知识开始系统学习，建议从党的基本历史和革命传统开始。\n\n'
    }
    
    // 需要加强的领域
    if (wrongCategories.size > 0) {
      const wrongCategoryList = Array.from(wrongCategories).map(cat => 
        categoryNames[cat as keyof typeof categoryNames] || cat
      ).join('、')
      advice += `## 需要加强的领域\n\n根据您的答题情况，建议重点加强以下红色知识领域的学习：**${wrongCategoryList}**。这些领域是红色教育的核心内容，深入学习有助于您更好地理解中华民族的奋斗历程。\n\n`
    }
    
    // 具体学习建议
    advice += '## 具体学习建议\n\n'
    
    if (wrongCategories.has('party-history')) {
      advice += '• **系统学习党史**：阅读《中国共产党简史》，观看《觉醒年代》等优秀影视作品，深入了解党的光辉历程。\n'
    }
    
    if (wrongCategories.has('revolution')) {
      advice += '• **深入了解革命历史**：学习长征精神、延安精神等革命精神，参观革命纪念馆和红色教育基地。\n'
    }
    
    if (wrongCategories.has('red-culture')) {
      advice += '• **传承红色文化**：多听红色歌曲、阅读红色经典文学作品，感受红色文化的深刻内涵。\n'
    }
    
    if (wrongCategories.has('heroes')) {
      advice += '• **学习英雄事迹**：了解革命先烈和时代楷模的感人故事，从中汲取精神力量和人生启示。\n'
    }
    
    if (wrongCategories.has('current-affairs')) {
      advice += '• **关注时政热点**：学习习近平新时代中国特色社会主义思想，了解国家发展战略和重大成就。\n'
    }
    
    advice += '• **建立学习习惯**：每天抽出时间学习红色知识，可以通过"学习强国"APP等平台进行系统学习。\n'
    advice += '• **参与实践活动**：积极参加红色主题的社会实践活动，在实践中深化对红色文化的理解。\n\n'
    
    // 实践活动建议
    advice += '## 实践活动建议\n\n'
    advice += '• **红色旅游体验**：利用假期参观红色旅游景点，实地感受革命历史的厚重底蕴。\n'
    advice += '• **志愿服务实践**：参与社区服务、扶贫助困等志愿活动，践行为人民服务的宗旨。\n'
    advice += '• **分享传播红色文化**：向家人朋友分享红色故事，在日常交流中传播正能量。\n\n'
    
    // 深入学习方向
    advice += '## 深入学习方向\n\n'
    advice += '• **系统理论学习**：建议系统学习马克思主义基本原理和中国化马克思主义理论成果。\n'
    advice += '• **专题研究深入**：选择感兴趣的红色主题进行深入研究，如某个历史时期或英雄人物。\n'
    advice += '• **多媒体学习法**：结合书籍、影视、音频等多种形式，让红色学习更加生动有趣。\n'
    advice += '• **定期测评巩固**：建议每月进行一次红色知识自测，不断巩固和拓展知识面。\n\n'
    
    advice += '**记住：红色基因是我们的精神命脉，学习红色文化就是在传承民族精神，坚定理想信念！🚩**'
    
    return advice
  }

  /**
   * 获取生成状态
   */
  public get generating() {
    return this.isGenerating.value
  }
}

// 导出服务实例
export const aiAdviceService = AiAdviceService.getInstance() 