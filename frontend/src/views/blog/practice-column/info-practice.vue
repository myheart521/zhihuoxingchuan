<template>
  <div class="practice-info-page">
    <!-- Header Section -->
    <header class="relative h-96 overflow-hidden">
      <img 
        src="@/assets/imgs/img_1.png"
        alt="实践活动背景"
        class="absolute inset-0 w-full h-full object-cover"
      />
      <div class="absolute inset-0 bg-gradient-to-b from-black/30 via-black/20 to-black/60"></div>
      <div class="absolute inset-0 flex items-center justify-center text-center text-white px-4">
        <div>
          <h1 class="text-4xl md:text-6xl font-bold mb-4 drop-shadow-lg">{{ competitionData.introduction.title }}</h1>
          <p class="text-lg md:text-xl max-w-3xl mx-auto opacity-90">青春足迹，时代印记</p>
        </div>
      </div>
    </header>

    <main class="max-w-6xl mx-auto px-4 py-8 space-y-8">
      <!-- Introduction Section -->
      <section id="introduction" class="content-section">
        <div class="content-card">
          <h2 class="text-3xl md:text-4xl font-bold mb-6 text-charcoal">{{ competitionData.introduction.title }}</h2>
          <p 
            v-for="(paragraph, index) in competitionData.introduction.paragraphs" 
            :key="index"
            class="text-base md:text-lg text-gray-700 mb-4"
          >
            {{ paragraph }}
          </p>
        </div>
      </section>

      <!-- Theme Section -->
      <section class="content-section">
        <div id="theme-card" class="content-card">
          <h3><i class="lucide-feather mr-2"></i>{{ competitionData.theme.title }}</h3>
          <p class="mb-4">{{ competitionData.theme.description }}</p>
          <ul>
            <li v-for="item in competitionData.theme.angles" :key="item.title">
              <strong>{{ item.title }}:</strong> {{ item.description }}
            </li>
          </ul>
        </div>
      </section>

      <!-- Participants Section -->
      <section class="content-section">
        <div id="participants-card" class="content-card">
          <h3><i class="lucide-users mr-2"></i>{{ competitionData.participants.title }}</h3>
          <p>{{ competitionData.participants.description }}</p>
        </div>
      </section>

      <!-- Timeline Section -->
      <section class="content-section">
        <div id="timeline-card" class="content-card">
          <h3><i class="lucide-calendar-days mr-2"></i>{{ competitionData.timeline.title }}</h3>
          <div class="timeline mt-6">
            <div 
              v-for="event in competitionData.timeline.events" 
              :key="event.phase"
              class="timeline-item"
            >
              <strong>{{ event.phase }}</strong>
              <span>{{ event.date }}</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Requirements Section -->
      <section class="content-section">
        <div id="requirements-card" class="content-card">
          <h3><i class="lucide-file-check-2 mr-2"></i>{{ competitionData.requirements.title }}</h3>
          <ul>
            <li v-for="rule in competitionData.requirements.rules" :key="rule" v-html="rule"></li>
          </ul>
        </div>
      </section>

      <!-- Submission Section -->
      <section class="content-section">
        <div id="submission-card" class="content-card">
          <h3><i class="lucide-send mr-2"></i>{{ competitionData.submission.title }}</h3>
          <p>{{ competitionData.submission.platform.description }}</p>
          <div class="text-center my-4">
            <button class="submit-btn" @click="handleSubmit">
              {{ competitionData.submission.platform.buttonText }}
            </button>
          </div>
          <div class="email-submission">
            <p class="font-semibold">{{ competitionData.submission.email.title }}</p>
            <p>{{ competitionData.submission.email.description }} <code>{{ competitionData.submission.email.address }}</code></p>
            <p class="mt-2">{{ competitionData.submission.email.format }}</p>
            <p class="mt-2">{{ competitionData.submission.email.fileNameFormat }}</p>
          </div>
        </div>
      </section>

      <!-- Rules Section -->
      <section class="content-section">
        <div id="rules-card" class="content-card">
          <h3><i class="lucide-scale mr-2"></i>{{ competitionData.rules.title }}</h3>
          <p>{{ competitionData.rules.description }}</p>
          <p class="font-bold my-4 text-center text-lg bg-cream p-3 rounded-lg">{{ competitionData.rules.formula }}</p>
          <ul>
            <li v-for="item in competitionData.rules.criteria" :key="item.title">
              <strong>{{ item.title }}:</strong> {{ item.description }}
            </li>
          </ul>
        </div>
      </section>

      <!-- Prizes Section -->
      <section class="content-section">
        <div id="prizes-card" class="content-card">
          <h3><i class="lucide-award mr-2"></i>{{ competitionData.prizes.title }}</h3>
          <div class="prize-grid mt-6">
            <div 
              v-for="award in competitionData.prizes.awards" 
              :key="award.name"
              class="prize-item"
            >
              <i :class="`lucide-${award.icon}`"></i>
              <h4>{{ award.name }}</h4>
              <p>{{ award.reward }}</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Consultation Section -->
      <section class="content-section">
        <div id="consultation-card" class="content-card">
          <h3><i class="lucide-messages-square mr-2"></i>{{ competitionData.consultation.title }}</h3>
          <p>{{ competitionData.consultation.description }}</p>
        </div>
      </section>

      <!-- Notes Section -->
      <section class="content-section">
        <div id="notes-card" class="content-card">
          <h3><i class="lucide-alert-triangle mr-2"></i>{{ competitionData.notes.title }}</h3>
          <ul>
            <li v-for="point in competitionData.notes.points" :key="point.title">
              <strong>{{ point.title }}:</strong> {{ point.description }}
            </li>
          </ul>
        </div>
      </section>

      <!-- Organizer Section -->
      <section class="content-section">
        <div id="organizer-card" class="content-card">
          <h3><i class="lucide-shield mr-2"></i>{{ competitionData.organizer.title }}</h3>
          <p class="text-2xl font-bold text-center py-8 text-accent-green">{{ competitionData.organizer.name }}</p>
        </div>
      </section>
    </main>

    <!-- Footer -->
    <footer class="bg-gray-100 py-8 mt-12">
      <div class="max-w-6xl mx-auto px-4 text-center">
        <div class="flex flex-wrap justify-center items-center gap-4 mb-4">
          <div class="flex items-center gap-2">
            <i class="lucide-users text-accent-green"></i>
            <span class="text-gray-700">生力军团队</span>
          </div>
          <div class="flex items-center gap-2">
            <i class="lucide-mail text-accent-green"></i>
            <span class="text-gray-700">maintainer@example.invalid</span>
          </div>
        </div>
        <p class="text-gray-600 text-sm">© 2025 生力军团队. 让青春的足迹，成为时代的印记</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import router from "@/router";

// 数据定义
const competitionData = ref({
  introduction: {
    title: `让青春的足迹，成为时代的印记`,
    paragraphs: [
      `那年夏天，我们背上行囊，奔赴山海。汗水浸湿过衣衫，泥土沾染过裤脚，星光见证过我们的彻夜长谈，蝉鸣是我们青春BGM里最动听的和弦。每一次"三下乡"，都是一次深入中国肌理的行走，一次与土地和人民的深刻对话。`,
      `在这里，你或许是讲台上播撒知识火种的"小老师"，是田埂间学习新农技的"新农人"，是镜头下记录非遗之美的"传承者"。这些独一无二的经历，不应只沉淀在记忆深处。现在，我们正式邀请你，用你的笔和镜头，将那些闪光的瞬间、深刻的感悟、动人的故事分享出来。让你的青春印记，成为激励更多人前行的力量；让你的乡土故事，汇成一幅壮丽的时代画卷。`
    ]
  },
  theme: {
    title: `征稿主题与创作角度`,
    description: `本次征稿以"我的三下乡故事"为核心，旨在发掘并分享青年学子在社会实践中的真实体验与深度思考。我们鼓励您从以下角度进行创作，但不限于此：`,
    angles: [
      { title: '人物志', description: '讲述你在乡间遇到的最难忘的一个人（如留守儿童、村支书、非遗传承人等），刻画其形象，展现其故事。' },
      { title: '成长录', description: '记录你在实践中遇到的挑战、获得的成长，以及对个人价值与社会责任的全新思考。' },
      { title: '观察记', description: '以青年视角观察和记录乡村在产业、文化、环境等方面的变迁与振兴，提出你的见解与思考。' },
      { title: '风物诗', description: '描绘当地独特的自然风光、风土人情或非遗文化，以散文或诗歌的形式，表达你对乡土中国的热爱。' }
    ]
  },
  participants: {
    title: '参与对象',
    description: `所有参与过（或正在参与）暑期"三下乡"社会实践的在校大学生。`
  },
  timeline: {
    title: '活动时间轴',
    events: [
      { phase: '稿件征集期', date: '2025年7月6日 - 2025年8月31日' },
      { phase: '作品展示与网络投票期', date: '2025年9月1日 - 2025年9月10日' },
      { phase: '专家评审期', date: '2025年9月11日 - 2025年9月17日' },
      { phase: '结果公示期', date: '2025年9月20日' }
    ]
  },
  requirements: {
    title: '作品要求',
    rules: [
      '<strong>体裁：</strong> 记叙文、散文、通讯稿、诗歌均可。',
      '<strong>字数：</strong> 散文、记叙文等建议在800-2500字之间；诗歌不限。',
      '<strong>真实性：</strong> 内容必须基于个人真实经历，情感真挚，严禁杜撰虚构。',
      '<strong>配图：</strong> 每篇稿件须配有3-5张与文章内容相关的原创高清图片，并附上简短图说(50字以内)。',
      '<strong>格式：</strong> 标题自拟，文末请注明作者真实姓名、学校、院系及联系方式。'
    ]
  },
  submission: {
    title: '投稿方式',
    platform: {
      description: `登录特别能战斗精神网站，在活动专区点击"我要投稿"，按照指引上传文字与图片。选择“实践主题”，发布时请务必添加活动标签 #青春乡土印记#。`,
      buttonText: '立即投稿'
    },
    email: {
      title: '备选：邮箱投稿',
      description: '将Word文档格式的稿件及图片打包发送至官方邮箱',
      address: 'maintainer@example.invalid',
      format: `邮件主题命名为："三下乡征稿+作品标题+作者姓名"。`,
      fileNameFormat: `压缩文件名格式："作品标题+作者姓名.zip/rar"`
    }
  },
  rules: {
    title: '评选规则',
    description: `为确保公平、公正与专业，本次评选采用"网络热度+专家评审"相结合的综合评分机制：`,
    formula: '总分 = 网络点赞得分 (占50%) + 专家评审得分 (占50%)',
    criteria: [
      { title: '网络点赞得分', description: '在投票期内，作品在特别能战斗精神网站上获得的点赞数将按比例换算为得分。（细则：将所有参赛作品的最高点赞数设为满分50分，其他作品得分 = (该作品点赞数 / 最高点赞数) * 50）' },
      { title: '专家评审得分', description: '平台将邀请资深媒体人、作家及高校教师组成评审团，从主题深度、叙事技巧、情感表达、思想价值、图文契合度五个维度对作品进行打分，满分50分。' }
    ]
  },
  prizes: {
    title: '奖项设置',
    awards: [
      { name: '一等奖 (1名)', reward: '现金200元 +  平台首页置顶推荐一周', icon: 'gem' },
      { name: '二等奖 (3名)', reward: '现金100元 +  平台专题报道', icon: 'medal' },
      { name: '三等奖 (5名)', reward: '现金50元', icon: 'verified' },
      { name: '优秀奖 (10名)', reward: '现金20元', icon: 'star' },
      { name: '最佳人气奖 (1名)（授予网络点赞数第一名的作品）', reward: '价值100元的精美文创礼品', icon: 'heart' }
    ]
  },
  consultation: {
    title: '活动咨询',
    description: '如有任何疑问，请通过特别能战斗精神网站官方渠道联系我们，或发送邮件至 maintainer@example.invalid 进行咨询。'
  },
  notes: {
    title: '注意事项',
    points: [
      { title: '原创承诺', description: '参赛作品必须为作者原创。严禁抄袭、套作，一经发现，立即取消参赛资格并予以公示。' },
      { title: '版权归属', description: '参赛者拥有作品的署名权和永久著作权。一旦投稿，即视为授权主办方特别能战斗精神网站拥有该作品的线上发布、宣传推广、汇编出版等使用权，不再另付稿酬。' },
      { title: '公平竞赛', description: '严禁任何形式的刷票行为，平台后台将对数据进行监控，违规者将被取消网络投票成绩。' },
      { title: '活动解释权', description: '本次活动的最终解释权归生力军团队所有。' }
    ]
  },
  organizer: {
    title: '主办单位',
    name: '生力军团队'
  }
})

// 方法定义
const handleSubmit = () => {

  // 这里可以添加路由跳转逻辑
  router.push('/content/add')
}

// 滚动动画
const animateOnScroll = () => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        const target = entry.target as HTMLElement
        target.style.opacity = '1'
        target.style.transform = 'translateY(0)'
      }
    })
  }, { threshold: 0.2 })

  const elements = document.querySelectorAll('.content-card, .content-section, header')
  elements.forEach((el) => {
    const element = el as HTMLElement
    element.style.opacity = '0'
    element.style.transform = 'translateY(30px)'
    element.style.transition = 'opacity 0.7s ease, transform 0.7s ease'
    observer.observe(element)
  })
}

onMounted(() => {
  // 延迟执行动画，确保DOM渲染完成
  setTimeout(() => {
    animateOnScroll()
  }, 100)
})
</script>

<style scoped lang="scss">
// CSS变量定义
:root {
  --cream: #fef7ed;
  --charcoal: #374151;
  --warm-yellow: #fbbf24;
  --accent-green: #10b981;
  --soft-blue: #3b82f6;
  --gentle-purple: #8b5cf6;
}

// 基础样式
.practice-info-page {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  line-height: 1.6;
  color: #374151;
  background: linear-gradient(135deg, #fef7ed 0%, #fff 100%);
  min-height: 100vh;
}

// 内容区域样式
.content-section {
  margin-bottom: 2rem;
}

.content-card {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(16, 185, 129, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  }

  h3 {
    color: var(--charcoal);
    font-size: 1.75rem;
    font-weight: 700;
    margin-bottom: 1.5rem;
    display: flex;
    align-items: center;
    
    i {
      color: var(--accent-green);
      margin-right: 0.5rem;
    }
  }

  ul {
    list-style: none;
    padding: 0;

    li {
      padding: 0.75rem 0;
      border-bottom: 1px solid #f3f4f6;
      
      &:last-child {
        border-bottom: none;
      }

      strong {
        color: var(--charcoal);
      }
    }
  }

  p {
    margin-bottom: 1rem;
    color: #6b7280;
  }

  code {
    background: var(--cream);
    padding: 0.25rem 0.5rem;
    border-radius: 4px;
    font-family: 'Monaco', 'Menlo', monospace;
    font-size: 0.9em;
    color: var(--charcoal);
  }
}

// 时间轴样式
.timeline {
  position: relative;

  &::before {
    content: '';
    position: absolute;
    left: 1rem;
    top: 0;
    bottom: 0;
    width: 2px;
    background: linear-gradient(to bottom, var(--accent-green), var(--soft-blue));
  }

  .timeline-item {
    position: relative;
    padding-left: 3rem;
    margin-bottom: 1.5rem;
    
    &::before {
      content: '';
      position: absolute;
      left: 0.5rem;
      top: 0.25rem;
      width: 1rem;
      height: 1rem;
      background: var(--accent-green);
      border-radius: 50%;
      border: 3px solid white;
      box-shadow: 0 0 0 3px var(--accent-green);
    }

    strong {
      display: block;
      color: var(--charcoal);
      font-size: 1.1rem;
      margin-bottom: 0.25rem;
    }

    span {
      color: #6b7280;
      font-size: 0.9rem;
    }
  }
}

// 奖项网格样式
.prize-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;

  .prize-item {
    background: linear-gradient(135deg, var(--cream) 0%, white 100%);
    padding: 2rem;
    border-radius: 12px;
    text-align: center;
    border: 2px solid transparent;
    transition: all 0.3s ease;

    &:hover {
      border-color: var(--accent-green);
      transform: translateY(-4px);
      box-shadow: 0 10px 30px rgba(16, 185, 129, 0.15);
    }

    i {
      font-size: 3rem;
      color: var(--accent-green);
      margin-bottom: 1rem;
      display: block;
    }

    h4 {
      color: var(--charcoal);
      font-size: 1.25rem;
      font-weight: 600;
      margin-bottom: 1rem;
    }

    p {
      color: #6b7280;
      font-size: 0.95rem;
      line-height: 1.5;
    }
  }
}

// 提交按钮样式
.submit-btn {
  background: linear-gradient(135deg, var(--accent-green) 0%, var(--soft-blue) 100%);
  color: red;
  padding: 1rem 2.5rem;
  border: none;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(16, 185, 129, 0.4);
  }

  &:active {
    transform: translateY(0);
  }
}

// 邮箱投稿区域样式
.email-submission {
  background: var(--cream);
  padding: 1.5rem;
  border-radius: 12px;
  margin-top: 2rem;
  border-left: 4px solid var(--accent-green);
}

// 特殊背景色
.bg-cream {
  background-color: var(--cream);
}

.text-accent-green {
  color: var(--accent-green);
}

.text-charcoal {
  color: var(--charcoal);
}

// 响应式设计
@media (max-width: 768px) {
  .content-card {
    padding: 1.5rem;
    margin: 0 1rem;
  }

  .prize-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .timeline {
    &::before {
      left: 0.5rem;
    }

    .timeline-item {
      padding-left: 2rem;

      &::before {
        left: 0;
      }
    }
  }
}

// Lucide图标基础样式
[class^="lucide-"] {
  width: 1.25rem;
  height: 1.25rem;
  stroke: currentColor;
  stroke-width: 2;
  fill: none;
}

// 特定图标大小调整
.prize-item [class^="lucide-"] {
  width: 3rem;
  height: 3rem;
}
</style>
