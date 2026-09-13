import type { TestQuestionVO, QuestionOptionVO, TestVO as IndexTestVO, TestCategoryVO } from './index'

// 题目接口定义（与 index.ts 保持一致）
interface QuestionVO {
  id: number
  title: string
  type: 'single' | 'multiple' | 'judge' | 'text'
  category: string
  score: number
  options: Array<{ label: string; text: string }>
  correctAnswer: string
  explanation: string  // 改为必需字段
  tips?: string
  difficulty?: 'easy' | 'medium' | 'hard'
}

// 测试套题接口
interface TestVO {
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
  questions: QuestionVO[]
}

// 分类接口
interface CategoryVO {
  id: string
  name: string
  description: string
  icon: string
  color: string
  questionCount: number
}

// 党史学习测试题目
const partyHistoryQuestions: QuestionVO[] = [
  {
    id: 1,
    type: 'single',
    title: '中国共产党成立于哪一年？',
    score: 10,
    category: 'party-history',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '1919年' },
      { label: 'B', text: '1921年' },
      { label: 'C', text: '1922年' },
      { label: 'D', text: '1920年' }
    ],
    correctAnswer: 'B',
    explanation: '中国共产党成立于1921年7月23日，中国共产党第一次全国代表大会在上海召开。',
    tips: '1921年是中国共产党成立的重要年份，标志着中国革命有了坚强的领导核心。'
  },
  {
    id: 2,
    type: 'single',
    title: '中国共产党第一次全国代表大会在哪里召开？',
    score: 10,
    category: 'party-history',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '北京' },
      { label: 'B', text: '上海' },
      { label: 'C', text: '广州' },
      { label: 'D', text: '武汉' }
    ],
    correctAnswer: 'B',
    explanation: '中国共产党第一次全国代表大会于1921年7月23日在上海法租界望志路106号（今兴业路76号）召开。',
    tips: '一大会址现在是中共一大会址纪念馆，是重要的红色教育基地。'
  },
  {
    id: 3,
    type: 'single',
    title: '遵义会议召开于哪一年？',
    score: 10,
    category: 'party-history',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '1934年' },
      { label: 'B', text: '1935年' },
      { label: 'C', text: '1936年' },
      { label: 'D', text: '1937年' }
    ],
    correctAnswer: 'B',
    explanation: '遵义会议于1935年1月召开，是中国共产党历史上一个生死攸关的转折点。',
    tips: '遵义会议确立了毛泽东同志在党中央和红军的领导地位，挽救了党和红军。'
  },
  {
    id: 4,
    type: 'multiple',
    title: '中国共产党在新民主主义革命时期的三大法宝是什么？',
    score: 15,
    category: 'party-history',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '统一战线' },
      { label: 'B', text: '武装斗争' },
      { label: 'C', text: '党的建设' },
      { label: 'D', text: '土地革命' }
    ],
    correctAnswer: 'ABC',
    explanation: '毛泽东同志总结的新民主主义革命的三大法宝是：统一战线、武装斗争、党的建设。',
    tips: '三大法宝是中国共产党在革命斗争中总结出的宝贵经验。'
  },
  {
    id: 5,
    type: 'single',
    title: '中华人民共和国成立于哪一年？',
    score: 10,
    category: 'party-history',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '1948年' },
      { label: 'B', text: '1949年' },
      { label: 'C', text: '1950年' },
      { label: 'D', text: '1951年' }
    ],
    correctAnswer: 'B',
    explanation: '中华人民共和国于1949年10月1日成立，毛泽东主席在天安门城楼上庄严宣告。',
    tips: '1949年10月1日是中华民族历史上的重要里程碑。'
  },
  {
    id: 6,
    type: 'judge',
    title: '改革开放始于1978年。',
    score: 10,
    category: 'party-history',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '正确' },
      { label: 'B', text: '错误' }
    ],
    correctAnswer: 'A',
    explanation: '1978年12月召开的中共十一届三中全会，标志着我国改革开放的开始。',
    tips: '十一届三中全会是中国历史上具有重大意义的转折点。'
  }
]

// 革命历史测试题目
const revolutionQuestions: QuestionVO[] = [
  {
    id: 101,
    type: 'single',
    title: '红军长征开始于哪一年？',
    score: 10,
    category: 'revolution',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '1933年' },
      { label: 'B', text: '1934年' },
      { label: 'C', text: '1935年' },
      { label: 'D', text: '1936年' }
    ],
    correctAnswer: 'B',
    explanation: '红军长征始于1934年10月，中央红军从江西瑞金出发，开始了举世闻名的二万五千里长征。',
    tips: '长征是人类历史上的伟大壮举，体现了中国共产党人的坚强意志。'
  },
  {
    id: 102,
    type: 'single',
    title: '抗日战争全面爆发的标志是？',
    score: 10,
    category: 'revolution',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '九一八事变' },
      { label: 'B', text: '七七事变' },
      { label: 'C', text: '一二八事变' },
      { label: 'D', text: '八一三事变' }
    ],
    correctAnswer: 'B',
    explanation: '1937年7月7日的卢沟桥事变（七七事变）标志着中国抗日战争全面爆发。',
    tips: '七七事变是中华民族全面抗战的起点，展现了中华民族不屈的精神。'
  },
  {
    id: 103,
    type: 'single',
    title: '解放战争三大战役不包括哪一个？',
    score: 10,
    category: 'revolution',
    difficulty: 'hard',
    options: [
      { label: 'A', text: '辽沈战役' },
      { label: 'B', text: '淮海战役' },
      { label: 'C', text: '平津战役' },
      { label: 'D', text: '渡江战役' }
    ],
    correctAnswer: 'D',
    explanation: '解放战争三大战役是辽沈战役、淮海战役、平津战役。渡江战役虽然重要，但不属于三大战役。',
    tips: '三大战役奠定了人民解放战争在全国胜利的基础。'
  },
  {
    id: 104,
    type: 'multiple',
    title: '中国人民志愿军抗美援朝的主要意义包括？',
    score: 15,
    category: 'revolution',
    difficulty: 'hard',
    options: [
      { label: 'A', text: '保卫了新中国的安全' },
      { label: 'B', text: '维护了世界和平' },
      { label: 'C', text: '提高了中国的国际威望' },
      { label: 'D', text: '显示了中华民族的爱国主义精神' }
    ],
    correctAnswer: 'ABCD',
    explanation: '抗美援朝战争的胜利，保卫了新中国安全，维护了世界和平，提高了国际威望，展现了爱国主义精神。',
    tips: '抗美援朝战争是新中国的立国之战，意义重大而深远。'
  },
  {
    id: 105,
    type: 'judge',
    title: '井冈山是中国革命的摇篮。',
    score: 10,
    category: 'revolution',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '正确' },
      { label: 'B', text: '错误' }
    ],
    correctAnswer: 'A',
    explanation: '井冈山是中国革命的摇篮，毛泽东等老一辈革命家在这里创建了第一个农村革命根据地。',
    tips: '井冈山精神是中国共产党宝贵的精神财富。'
  }
]

// 红色文化测试题目
const redCultureQuestions: QuestionVO[] = [
  {
    id: 201,
    type: 'single',
    title: '《义勇军进行曲》的词作者是？',
    score: 10,
    category: 'red-culture',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '田汉' },
      { label: 'B', text: '聂耳' },
      { label: 'C', text: '冼星海' },
      { label: 'D', text: '贺绿汀' }
    ],
    correctAnswer: 'A',
    explanation: '《义勇军进行曲》由田汉作词，聂耳作曲，是中华人民共和国国歌。',
    tips: '国歌体现了中华民族勇敢顽强、不屈不挠的战斗精神。'
  },
  {
    id: 202,
    type: 'single',
    title: '电影《红色娘子军》反映的是哪个地区的革命故事？',
    score: 10,
    category: 'red-culture',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '江西' },
      { label: 'B', text: '湖南' },
      { label: 'C', text: '海南' },
      { label: 'D', text: '广东' }
    ],
    correctAnswer: 'C',
    explanation: '《红色娘子军》反映的是海南岛女战士的革命斗争故事。',
    tips: '红色娘子军体现了中国妇女在革命斗争中的英勇表现。'
  },
  {
    id: 203,
    type: 'multiple',
    title: '以下哪些是著名的红色经典歌曲？',
    score: 15,
    category: 'red-culture',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '《东方红》' },
      { label: 'B', text: '《歌唱祖国》' },
      { label: 'C', text: '《没有共产党就没有新中国》' },
      { label: 'D', text: '《我和我的祖国》' }
    ],
    correctAnswer: 'ABCD',
    explanation: '这些都是深受人民喜爱的红色经典歌曲，表达了对党和祖国的热爱。',
    tips: '红色歌曲是传承红色基因的重要载体。'
  },
  {
    id: 204,
    type: 'single',
    title: '小说《红岩》的主要背景是？',
    score: 10,
    category: 'red-culture',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '抗日战争时期' },
      { label: 'B', text: '解放战争时期' },
      { label: 'C', text: '土地革命时期' },
      { label: 'D', text: '新中国成立初期' }
    ],
    correctAnswer: 'B',
    explanation: '《红岩》描写的是解放战争时期重庆地下党的英勇斗争。',
    tips: '《红岩》塑造了江姐等众多革命英雄形象。'
  }
]

// 英雄事迹测试题目
const heroesQuestions: QuestionVO[] = [
  {
    id: 301,
    type: 'single',
    title: '被誉为"生的伟大，死的光荣"的女英雄是？',
    score: 10,
    category: 'heroes',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '刘胡兰' },
      { label: 'B', text: '江姐' },
      { label: 'C', text: '赵一曼' },
      { label: 'D', text: '向警予' }
    ],
    correctAnswer: 'A',
    explanation: '刘胡兰是著名的革命烈士，毛主席为她题词"生的伟大，死的光荣"。',
    tips: '刘胡兰15岁就英勇就义，体现了共产主义战士的崇高品质。'
  },
  {
    id: 302,
    type: 'single',
    title: '抗日英雄杨靖宇是哪个抗日联军的主要领导人？',
    score: 10,
    category: 'heroes',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '东北抗日联军' },
      { label: 'B', text: '华北抗日联军' },
      { label: 'C', text: '华中抗日联军' },
      { label: 'D', text: '华南抗日联军' }
    ],
    correctAnswer: 'A',
    explanation: '杨靖宇是东北抗日联军的主要创建者和领导人之一，在极其艰苦的条件下坚持抗日斗争。',
    tips: '杨靖宇将军宁死不屈的精神永远值得我们学习。'
  },
  {
    id: 303,
    type: 'single',
    title: '在抗美援朝战争中，哪位英雄用身体堵住敌人枪眼？',
    score: 10,
    category: 'heroes',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '邱少云' },
      { label: 'B', text: '黄继光' },
      { label: 'C', text: '罗盛教' },
      { label: 'D', text: '杨根思' }
    ],
    correctAnswer: 'B',
    explanation: '黄继光在上甘岭战役中用自己的胸膛堵住了敌人的枪眼，为部队冲锋开辟了道路。',
    tips: '黄继光的英雄事迹体现了志愿军战士的国际主义和爱国主义精神。'
  },
  {
    id: 304,
    type: 'multiple',
    title: '以下哪些是著名的革命烈士？',
    score: 15,
    category: 'heroes',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '董存瑞' },
      { label: 'B', text: '江姐' },
      { label: 'C', text: '方志敏' },
      { label: 'D', text: '雷锋' }
    ],
    correctAnswer: 'ABC',
    explanation: '董存瑞、江姐、方志敏都是著名的革命烈士。雷锋虽然是英雄，但不是烈士。',
    tips: '革命烈士为了人民的解放事业献出了宝贵的生命。'
  }
]

// 时政热点测试题目
const currentAffairsQuestions: QuestionVO[] = [
  {
    id: 401,
    type: 'single',
    title: '中国特色社会主义进入新时代的主要标志是？',
    score: 10,
    category: 'current-affairs',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '中共十八大的召开' },
      { label: 'B', text: '中共十九大的召开' },
      { label: 'C', text: '全面建成小康社会' },
      { label: 'D', text: '改革开放40周年' }
    ],
    correctAnswer: 'A',
    explanation: '党的十八大以来，中国特色社会主义进入了新时代，这是我国发展新的历史方位。',
    tips: '新时代是承前启后、继往开来的时代，是全面建设社会主义现代化国家的时代。'
  },
  {
    id: 402,
    type: 'single',
    title: '"两个一百年"奋斗目标中，第一个一百年是指？',
    score: 10,
    category: 'current-affairs',
    difficulty: 'medium',
    options: [
      { label: 'A', text: '到2020年全面建成小康社会' },
      { label: 'B', text: '到2021年全面建成小康社会' },
      { label: 'C', text: '到2035年基本实现现代化' },
      { label: 'D', text: '到2050年建成现代化强国' }
    ],
    correctAnswer: 'B',
    explanation: '第一个一百年奋斗目标是到中国共产党成立100年时（2021年）全面建成小康社会。',
    tips: '我们已经实现了第一个百年奋斗目标，正在向第二个百年奋斗目标迈进。'
  },
  {
    id: 403,
    type: 'multiple',
    title: '"四个全面"战略布局包括哪些内容？',
    score: 15,
    category: 'current-affairs',
    difficulty: 'hard',
    options: [
      { label: 'A', text: '全面建设社会主义现代化国家' },
      { label: 'B', text: '全面深化改革' },
      { label: 'C', text: '全面依法治国' },
      { label: 'D', text: '全面从严治党' }
    ],
    correctAnswer: 'ABCD',
    explanation: '"四个全面"战略布局是：全面建设社会主义现代化国家、全面深化改革、全面依法治国、全面从严治党。',
    tips: '"四个全面"是新时代中国特色社会主义的重要战略布局。'
  },
  {
    id: 404,
    type: 'judge',
    title: '中国已经实现了全面建成小康社会的目标。',
    score: 10,
    category: 'current-affairs',
    difficulty: 'easy',
    options: [
      { label: 'A', text: '正确' },
      { label: 'B', text: '错误' }
    ],
    correctAnswer: 'A',
    explanation: '2021年，我们如期实现了全面建成小康社会的第一个百年奋斗目标。',
    tips: '全面建成小康社会是中华民族发展史上的重要里程碑。'
  }
]

// 分类定义
export const categories: CategoryVO[] = [
  {
    id: 'party-history',
    name: '🚩 党史学习',
    description: '学习中国共产党的光辉历程',
    icon: '🚩',
    color: '#dc2626',
    questionCount: partyHistoryQuestions.length
  },
  {
    id: 'revolution',
    name: '⚔️ 革命历史',
    description: '了解中国革命的伟大历程',
    icon: '⚔️',
    color: '#b91c1c',
    questionCount: revolutionQuestions.length
  },
  {
    id: 'red-culture',
    name: '🎌 红色文化',
    description: '传承红色文化经典',
    icon: '🎌',
    color: '#ef4444',
    questionCount: redCultureQuestions.length
  },
  {
    id: 'heroes',
    name: '🏆 英雄事迹',
    description: '学习革命英雄的光辉事迹',
    icon: '🏆',
    color: '#f59e0b',
    questionCount: heroesQuestions.length
  },
  {
    id: 'current-affairs',
    name: '📰 时政热点',
    description: '关注新时代发展成就',
    icon: '📰',
    color: '#991b1b',
    questionCount: currentAffairsQuestions.length
  }
]

// 所有题目汇总
const allQuestions = [
  ...partyHistoryQuestions,
  ...revolutionQuestions,
  ...redCultureQuestions,
  ...heroesQuestions,
  ...currentAffairsQuestions
]

// 测试套题定义
export const testList: TestVO[] = [
  {
    id: 1,
    title: '中国共产党党史知识测试',
    description: '测试您对中国共产党历史的了解程度',
    category: 'party-history',
    difficulty: 'medium',
    duration: 20,
    questionCount: 15,
    participantCount: 2156,
    status: 'pending',
    progress: 0,
    questions: partyHistoryQuestions.slice(0, 15)
  },
  {
    id: 2,
    title: '红军长征历史知识测试',
    description: '了解红军长征的伟大历程和历史意义',
    category: 'revolution',
    difficulty: 'hard',
    duration: 25,
    questionCount: 20,
    participantCount: 1834,
    status: 'pending',
    progress: 0,
    questions: revolutionQuestions.slice(0, 20)
  },
  {
    id: 3,
    title: '革命英雄事迹知识测试',
    description: '学习革命先烈的英勇事迹和崇高精神',
    category: 'heroes',
    difficulty: 'easy',
    duration: 15,
    questionCount: 12,
    participantCount: 2487,
    status: 'pending',
    progress: 0,
    questions: heroesQuestions.slice(0, 12)
  },
  {
    id: 4,
    title: '红色文化传承知识测试',
    description: '传承和弘扬红色文化的深刻内涵',
    category: 'red-culture',
    difficulty: 'medium',
    duration: 18,
    questionCount: 16,
    participantCount: 1623,
    status: 'pending',
    progress: 0,
    questions: redCultureQuestions.slice(0, 16)
  },
  {
    id: 5,
    title: '新时代党的理论知识测试',
    description: '学习新时代中国特色社会主义思想',
    category: 'current-affairs',
    difficulty: 'hard',
    duration: 30,
    questionCount: 25,
    participantCount: 987,
    status: 'pending',
    progress: 0,
    questions: currentAffairsQuestions.slice(0, 25)
  },
  {
    id: 6,
    title: '抗日战争历史知识测试',
    description: '铭记抗日战争历史，弘扬爱国主义精神',
    category: 'revolution',
    difficulty: 'medium',
    duration: 22,
    questionCount: 18,
    participantCount: 1456,
    status: 'pending',
    progress: 0,
    questions: revolutionQuestions.slice(0, 18)
  }
]

// 类型转换函数，将内部QuestionVO转换为API的TestQuestionVO
function convertToTestQuestionVO(question: QuestionVO): TestQuestionVO {
  return {
    id: question.id,
    type: question.type,
    title: question.title,
    score: question.score,
    options: question.options.map(opt => ({
      label: opt.label,
      text: opt.text
    })),
    explanation: question.explanation,
    tips: question.tips,
    category: question.category,
    difficulty: question.difficulty || 'medium',
    correctAnswer: question.correctAnswer
  }
}

// 类型转换函数，将内部TestVO转换为API的TestVO
function convertToIndexTestVO(test: TestVO): IndexTestVO {
  return {
    id: test.id,
    title: test.title,
    description: test.description,
    category: test.category,
    difficulty: test.difficulty,
    duration: test.duration,
    questionCount: test.questionCount,
    participantCount: test.participantCount,
    status: test.status,
    progress: test.progress,
    questions: test.questions.map(convertToTestQuestionVO),
    timeLimit: test.duration
  }
}

// 导出的工具函数 - 返回转换后的类型
export function getTestById(testId: number): IndexTestVO | null {
  const test = testList.find(test => test.id === testId)
  return test ? convertToIndexTestVO(test) : null
}

export function getRandomQuestions(category: string, count: number): TestQuestionVO[] {
  const questionMap = {
    'party-history': partyHistoryQuestions,
    'revolution': revolutionQuestions,
    'red-culture': redCultureQuestions,
    'heroes': heroesQuestions,
    'current-affairs': currentAffairsQuestions
  }
  
  const questions = questionMap[category as keyof typeof questionMap] || []
  const shuffled = [...questions].sort(() => Math.random() - 0.5)
  return shuffled.slice(0, Math.min(count, questions.length)).map(convertToTestQuestionVO)
}

export function getQuestionsByCategory(category: string): TestQuestionVO[] {
  const questionMap = {
    'party-history': partyHistoryQuestions,
    'revolution': revolutionQuestions,
    'red-culture': redCultureQuestions,
    'heroes': heroesQuestions,
    'current-affairs': currentAffairsQuestions
  }
  
  const questions = questionMap[category as keyof typeof questionMap] || []
  return questions.map(convertToTestQuestionVO)
}

export function getAllQuestions(): TestQuestionVO[] {
  return allQuestions.map(convertToTestQuestionVO)
}

// 添加缺失的 getAllTests 函数
export function getAllTests(): IndexTestVO[] {
  return testList.map(convertToIndexTestVO)
}

// 导出题目数组
export { 
  partyHistoryQuestions,
  revolutionQuestions, 
  redCultureQuestions,
  heroesQuestions,
  currentAffairsQuestions,
  allQuestions
}