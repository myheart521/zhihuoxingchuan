<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import imag1 from '/public/aiguo.png'
import imag2 from '/public/xidajing.png'
import imag3 from '/public/tebienengzhandou.png'
import imag4 from '/public/tebienengzhandoutimu.png'

// 定义轮播图数据
const slides = ref([
  {
    id: 1,
    image: imag1,
    title: '"怀川灵蕴"',
    description: '焦作，古称山阳，因丰富的煤炭资源而闻名，是中国近代工业的重要发源地之一。这里不仅拥有悠久的历史文化，还孕育了独特的城市精神——“特别能战斗”。焦作的地理环境和人文背景为其提供了深厚的土壤。\n' +
      '\n' +
      '焦作地处豫北，自古以来就是华夏文明的重要发源地之一。这里保留了裴李岗文化、仰韶文化及龙山文化遗址，是中华民族早期文明的重要见证。同时，焦作还是历史上许多著名人物的活动地，如司马懿、李商隐、韩愈等，这些历史文化积淀为焦作注入了深厚的文化底蕴。焦作的煤炭资源自古以来便被开发利用，春秋战国时期便有煤炭发现并用于取暖和冶铁。这种资源的开发不仅推动了当地经济的发展，也培养了焦作人民吃苦耐劳、坚韧不拔的精神品质。这种精神在后来的焦作煤矿工人运动中得到了集中体现。\n' +
      '\n' +
      '焦作的地理环境也为“特别能战斗”精神的形成提供了条件。作为一座以矿起家的城市，焦作经历了从煤炭资源的开采到资源枯竭的转型过程。在资源枯竭后，焦作通过绿色转型实现了城市的可持续发展。这种在逆境中求生存、求发展的精神，正是“特别能战斗”精神的具体体现。',
    link: 'https://example.invalid/resource'
  },
  {
    id: 2,
    image: imag2,
    title: '"焦煤星火"',
    description: '英商福公司在焦作的开发对中国式现代化进程具有深远影响。福公司在焦作创办了路矿学堂（即示例高校前身），并修建了焦作煤矿，这不仅推动了当地经济的发展，也促进了教育和科技的进步。\n' +
      '\n' +
      '福公司的开发使焦作成为中国近代工业的重要基地之一。通过引进西方先进的技术和管理经验，福公司提升了焦作煤矿的生产效率，并推动了当地工业化进程。同时，福公司还创办了路矿学堂，培养了一批具有现代科学知识和技术能力的人才，为中国的现代化建设奠定了基础。\n' +
      '\n' +
      '示例高校作为福公司路矿学堂的继承者，承载了焦作工业文化和红色基因。学校不仅传承了“特别能战斗”的精神，还在新时代继续发扬光大。例如，学校通过建设焦作煤矿工人大罢工纪念馆，将红色文化融入教学和科研中，培养学生的爱国情怀和社会责任感。',
    link: 'https://example.invalid/resource'
  },
  {
    id: 3,
    image: imag3,
    title: '"惊世罢工"',
    description: '1925年7月9日，为声援上海“五卅”反帝运动，焦作煤矿工人在中国共产党的领导下爆发了历时8个月的大罢工。罢工由王封矿、李封矿等煤矿工人发起，共有11000余名工人参与。罢工期间，工人组织了夜校教育、工人夜校宣传马克思主义、募捐救济罢工家庭等活动，展现了高度的组织性和斗争精神。\n' +
      '\n' +
      '罢工过程分为爆发、坚持斗争和胜利三个阶段。罢工期间，英商福公司试图镇压工人运动，但工人通过坚持斗争和争取社会各界支持，最终迫使资本家接受了罢工条款，取得了完全胜利。值得一提的是，在整个罢工过程中，没有一名工人牺牲，这在中国工人运动史上堪称奇迹。\n' +
      '\n' +
      '罢工胜利后，毛泽东在《中国社会各阶级的分析》中盛赞焦作煤矿工人“特别能战斗”，并认为这是党领导工人运动的成功典范。此外，《焦作煤矿工人宣言》中提出的“全国工人大团结！全世界无产阶级联合起来！”等口号也极大地鼓舞了全国工人阶级的斗志。',
    link: 'https://example.invalid/resource'
  },
  {
    id: 4,
    image: imag4,
    title: '"精神永存"',
    description: '焦作的历史遗址和红色文化资源是新时代传承“特别能战斗”精神的重要载体。例如，位于示例高校北校区的焦作煤矿工人大罢工指挥部旧址是全国唯一设在高校内的工人大罢工指挥部遗址。这里不仅展示了罢工的历史背景和过程，还通过情景剧《井口怒吼》等形式生动再现了当年工人的斗争场景。\n' +
      '\n' +
      '示例高校作为传承红色基因的重要阵地，通过课程设计、社会实践和红色教育基地建设等方式，将“特别能战斗”精神融入学生的思想教育中。例如，学校组织师生参观西大井遗址和焦作煤矿工人大罢工纪念馆，让学生切身感受革命先烈的斗争精神。\n' +
      '\n' +
      '新时代的焦作人也在不断传承和发扬“特别能战斗”精神。无论是城市转型中的绿色发展理念，还是在抗击疫情、脱贫攻坚等重大任务中的表现，都体现了焦作人民吃苦耐劳、迎难而上的精神品质。\n' +
      '\n' +
      '焦作的历史和精神财富是新时代传承和发展的重要资源。通过保护历史遗址、弘扬红色文化以及培养新时代接班人，焦作将继续书写属于自己的辉煌篇章 ',
    link: 'https://example.invalid/resource'
  },
  // {
  //   id: 5,
  //   image: 'https://example.invalid/resource',
  //   title: '"城市衰落"',
  //   description: '见证城市的变迁，记录时代的演进。',
  //   link: 'https://example.invalid/resource'
  // },
  // {
  //   id: 6,
  //   image: 'https://example.invalid/resource',
  //   title: '"迁徙"',
  //   description: '跨越时空的旅程，探索未知的可能。',
  //   link: 'https://example.invalid/resource'
  // }
])

// 添加打开链接的方法
const openLink = (url: string) => {
  if (url) {
    window.open(url, '_blank')
  }
}

// 修改数组顺序以实现轮播效果
const nextSlide = () => {
  // 取出第一个元素
  const firstSlide = slides.value.shift()
  if (firstSlide) {
    // 添加到数组末尾
    slides.value.push(firstSlide)
  }
}

const prevSlide = () => {
  // 取出最后一个元素
  const lastSlide = slides.value.pop()
  if (lastSlide) {
    // 添加到数组开头
    slides.value.unshift(lastSlide)
  }
}

// 自动轮播
let autoplayInterval: number | null = null

const startAutoplay = () => {
  if (autoplayInterval) clearInterval(autoplayInterval)
  
  autoplayInterval = window.setInterval(() => {
    nextSlide()
  }, 5000) // 5秒切换一次
}

const stopAutoplay = () => {
  if (autoplayInterval) {
    clearInterval(autoplayInterval)
    autoplayInterval = null
  }
}

// 鼠标悬停时暂停自动轮播
const handleMouseEnter = () => {
  stopAutoplay()
}

const handleMouseLeave = () => {
  startAutoplay()
}

// 在组件挂载后启动自动轮播
onMounted(() => {
  startAutoplay()
})

// 在组件卸载前清除定时器
onUnmounted(() => {
  stopAutoplay()
})
</script>

<template>
  <div 
    class="red-showroom-container"
    @mouseenter="handleMouseEnter"
    @mouseleave="handleMouseLeave"
  >
    <main>
      <ul class="slider">
        <li 
          v-for="(slide, index) in slides" 
          :key="slide.id" 
          class="item" 
          :style="{ backgroundImage: `url('${slide.image}')` }"
          :class="{ 'active': index === 0 }"
        >
          <div class="content" :class="{ 'visible': index === 0 }">
            <h2 class="title">{{ slide.title }}</h2>
            <p class="description">
              {{ slide.description }}
            </p>
            <el-button 
              type="primary" 
              @click="openLink(slide.link)"
            >
              跳转到虚拟展厅
            </el-button>
          </div>
        </li>
      </ul>
      <nav class="nav">
        <div class="btn prev" @click="prevSlide">
          <el-icon><ArrowLeft /></el-icon>
        </div>
        <div class="btn next" @click="nextSlide">
          <el-icon><ArrowRight /></el-icon>
        </div>
      </nav>
    </main>
  </div>
</template>

<style scoped lang="scss">
.red-showroom-container {
  width: 100%;
  height: 80vh;
  display: grid;
  place-items: center;
  overflow: hidden;
  background-color: #1a1a1a;

  main {
    position: relative;
    width: 100%;
    height: 100%;
    box-shadow: 0 3px 10px rgba(0,0,0,0.3);
  }

  .slider {
    height: 100%;
    width: 100%;
    padding: 0;
    margin: 0;
    position: relative;
    list-style: none;
  }

  .item {
    width: 200px;
    height: 300px;
    list-style-type: none;
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    z-index: 1;
    background-position: center;
    background-size: cover;
    border-radius: 20px;
    box-shadow: 0 20px 30px rgba(255,255,255,0.3) inset;
    transition: transform 0.3s, left 0.75s, top 0.75s, width 0.75s, height 0.75s;

    &:nth-child(1) {
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      transform: none;
      border-radius: 0;
      box-shadow: none;
      opacity: 1;
      z-index: 2;
    }

    &:nth-child(2) { 
      left: 50%; 
      z-index: 3;
    }
    &:nth-child(3) { 
      left: calc(50% + 220px); 
      z-index: 2;
    }
    &:nth-child(4) { 
      left: calc(50% + 440px); 
      z-index: 1;
    }
    &:nth-child(5) { 
      left: calc(50% + 660px); 
      opacity: 0; 
    }
    &:nth-child(6) { 
      display: none; // 隐藏最后一个元素
    }
  }

  .content {
    width: min(30vw, 400px);
    position: absolute;
    top: 50%;
    left: 3rem;
    transform: translateY(-50%);
    color: white;
    text-shadow: 0 3px 8px rgba(0,0,0,0.5);
    opacity: 0;
    display: none;
    z-index: 10;
    pointer-events: none;

    &.visible {
      display: block;
      animation: show 0.75s ease-in-out 0.3s forwards;
    }

    .title {
      font-family: 'arial-black', sans-serif;
      text-transform: uppercase;
      margin-bottom: 1rem;
    }

    .description {
      line-height: 1.7;
      margin: 1rem 0 1.5rem;
      font-size: 0.9rem;
    }

    :deep(.el-button) {
      background-color: rgba(0,0,0,0.3);
      border: 2px solid white;
      color: white;
      pointer-events: auto;
      &:hover {
        background-color: rgba(255,255,255,0.1);
      }
    }
  }

  .nav {
    position: absolute;
    bottom: 2rem;
    left: 50%;
    transform: translateX(-50%);
    z-index: 50;
    user-select: none;
    display: flex;
    gap: 10px;

    .btn {
      width: 48px;
      height: 48px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: rgba(255,255,255,0.5);
      color: rgba(0,0,0,0.7);
      border: 2px solid rgba(0,0,0,0.6);
      border-radius: 50%;
      cursor: pointer;
      transition: all 0.3s ease;

      .el-icon {
        font-size: 20px;
      }

      &:hover {
        background-color: rgba(255,255,255,0.3);
      }
    }
  }

  @keyframes show {
    0% {
      filter: blur(5px);
      transform: translateY(calc(-50% + 75px));
      opacity: 0;
    }
    100% {
      opacity: 1;
      filter: blur(0);
      transform: translateY(-50%);
    }
  }

  // 响应式设计
  @media (width > 650px) and (width < 900px) {
    .content {
      .title { font-size: 1.2rem; }
      .description { font-size: 0.8rem; }
    }
    .item {
      width: 160px;
      height: 270px;

      &:nth-child(2) { left: 50%; }
      &:nth-child(3) { left: calc(50% + 170px); }
      &:nth-child(4) { left: calc(50% + 340px); }
      &:nth-child(5) { left: calc(50% + 510px); opacity: 0; }
    }
  }

  @media (width < 650px) {
    .content {
      width: 80%;
      .title { font-size: 1rem; }
      .description { font-size: 0.75rem; }
    }
    .item {
      width: 130px;
      height: 220px;

      &:nth-child(2) { left: 50%; }
      &:nth-child(3) { left: calc(50% + 140px); }
      &:nth-child(4) { left: calc(50% + 280px); }
      &:nth-child(5) { left: calc(50% + 420px); opacity: 0; }
    }
  }
}
</style>
