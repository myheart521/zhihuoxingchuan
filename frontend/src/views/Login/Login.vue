<template>
  <div
    :class="prefixCls"
    class="relative h-[100%] lt-md:px-10px lt-sm:px-10px lt-xl:px-10px lt-xl:px-10px"
  >
    <!-- 添加全屏渐隐渐显的文字背景 -->
    <div class="floating-quotes-container">
      <div 
        v-for="(quote, index) in floatingQuotes" 
        :key="index"
        class="floating-quote"
        :style="{
          opacity: quote.opacity,
          left: quote.left + '%',
          top: quote.top + '%',
          fontSize: quote.size + 'px',
          transform: `rotate(${quote.rotation}deg)`
        }"
      >
        {{ quote.text }}
      </div>
    </div>
    
    <div class="relative mx-auto h-full flex">
      <div
        :class="`${prefixCls}__left flex-1 relative p-30px lt-xl:hidden overflow-x-hidden overflow-y-auto`"
      >
        <!-- 左上角的 logo + 系统标题 -->
        <div class="relative flex items-center text-white">
          <img alt="" class="mr-10px h-48px w-48px" src="@/assets/imgs/logo.png" />
          <span class="text-20px font-bold">特别能战斗精神学习平台</span>
        </div>
        <!-- 左边的背景图 + 欢迎语 -->
        <div class="h-[calc(100%-60px)] flex items-center justify-center">
          <TransitionGroup
            appear
            enter-active-class="animate__animated animate__bounceInLeft"
            tag="div"
            class="text-center"
          >
            <img key="1" alt="" class="w-350px mb-30px" src="/tebienengzhandoutimu.png" />
            <div key="2" class="text-3xl text-white font-bold red-text-shadow">传承红色基因 弘扬革命精神</div>
            <div key="3" class="mt-5 text-18px font-normal text-white">
              坚定信念，不怕牺牲，英勇顽强 - 特别能战斗精神
            </div>
            <!-- 添加红色精神标语轮播 -->
            <div key="4" class="spirit-quotes-container mt-30px">
              <div
class="spirit-quote" v-for="(quote, index) in spiritQuotes" :key="'quote-'+index"
                   :class="{ active: currentQuoteIndex === index }">
                {{ quote }}
              </div>
            </div>
          </TransitionGroup>
        </div>
      </div>
      <div
        class="relative flex-1 p-30px lt-sm:p-10px overflow-x-hidden overflow-y-auto"
        style="background-color: white"
      >
        <!-- 右上角的主题、语言选择 -->
        <div
          class="flex items-center justify-between at-2xl:justify-end at-xl:justify-end"
          style="color: var(--el-text-color-primary);"
        >
          <div class="flex items-center at-2xl:hidden at-xl:hidden">
            <img alt="" class="mr-10px h-48px w-48px" src="@/assets/imgs/logo.png" />
            <span class="text-20px font-bold" >特别能战斗精神学习平台</span>
          </div>
          <div class="flex items-center justify-end space-x-10px h-48px">
            <ThemeSwitch />
            <LocaleDropdown />
          </div>
        </div>
        <!-- 右边的登录界面 -->
        <Transition appear enter-active-class="animate__animated animate__bounceInRight">
          <div
            class="m-auto h-[calc(100%-60px)] w-[100%] flex items-center at-2xl:max-w-500px at-lg:max-w-500px at-md:max-w-500px at-xl:max-w-500px"
          >
            <!-- 账号登录 -->
            <LoginForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />
            <!-- 手机登录 -->
            <MobileForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />
            <!-- 二维码登录 -->
            <QrCodeForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />
            <!-- 注册 -->
            <RegisterForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />
            <!-- 三方登录 -->
            <SSOLoginVue class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />
            <!-- 忘记密码 -->
            <ForgetPasswordForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />
          </div>
        </Transition>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useDesign } from '@/hooks/web/useDesign'
import { ThemeSwitch } from '@/layout/components/ThemeSwitch'
import { LocaleDropdown } from '@/layout/components/LocaleDropdown'

import { LoginForm, MobileForm, QrCodeForm, RegisterForm, SSOLoginVue, ForgetPasswordForm } from './components'

defineOptions({ name: 'Login' })

const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('login')

// 特别能战斗精神相关文字
const spiritQuotes = ref([
  "特别能战斗精神是中国共产党人的宝贵精神财富",
  "不忘初心，牢记使命，永远跟党走",
  "发扬特别能战斗精神，勇于担当时代重任",
  "坚定理想信念，践行特别能战斗精神",
  "特别能战斗精神永远是我们的精神动力",
  "传承红色基因，赓续红色血脉"
])
const currentQuoteIndex = ref(0)

// 定时切换文字
let quoteInterval: ReturnType<typeof setInterval> | null = null

// 浮动文字数组
interface FloatingQuote {
  text: string;
  left: number;
  top: number;
  size: number;
  rotation: number;
  opacity: number;
  targetOpacity: number;
}

const floatingQuotes = ref<FloatingQuote[]>([])

// 特别能战斗精神相关的短语
const spiritPhrases = [
  "坚定信念",
  "不怕牺牲",
  "英勇顽强",
  "特别能战斗",
  "红色基因",
  "革命精神",
  "信仰力量",
  "为民服务",
  "艰苦奋斗",
  "无私奉献",
  "牢记使命",
  "初心不改",
  "赓续血脉",
  "永远跟党走",
  "奋勇前进",
  "敢于斗争",
  "实事求是",
  "与时俱进",
  "不畏艰险",
  "勇往直前",
  "团结一心",
  "众志成城",
  "自力更生",
  "艰苦创业",
  "百折不挠",
  "不屈不挠",
  "敢为人先",
  "开拓创新"
]

// 随机生成位置、大小、旋转角度
const generateRandomQuote = (): FloatingQuote => {
  // 安全区域：避开登录表单区域
  // 登录表单通常在右侧中间，所以我们避开这个区域
  let left, top
  
  // 随机决定是左侧还是右侧，但避开中间区域
  const side = Math.random() > 0.5 ? 'left' : 'right'
  
  if (side === 'left') {
    left = Math.random() * 30 // 0-30%
  } else {
    left = 70 + Math.random() * 25 // 70-95%
  }
  
  // 随机高度，但避开中间区域
  const verticalPosition = Math.random() > 0.5 ? 'top' : 'bottom'
  
  if (verticalPosition === 'top') {
    top = 5 + Math.random() * 20 // 5-25%
  } else {
    top = 70 + Math.random() * 25 // 70-95%
  }
  
  return {
    text: spiritPhrases[Math.floor(Math.random() * spiritPhrases.length)],
    left,
    top,
    size: 16 + Math.random() * 24, // 16-40px
    rotation: -20 + Math.random() * 40, // -20 to +20 degrees
    opacity: 0,
    targetOpacity: 0.05 + Math.random() * 0.3 // 0.15-0.35
  }
}

// 创建新的浮动文字
const createNewFloatingQuote = () => {
  // 生成一个新的随机浮动文字
  const newQuote = generateRandomQuote()
  floatingQuotes.value.push(newQuote)
  
  // 淡入效果 - 短暂延迟后设置目标透明度
  setTimeout(() => {
    // 检查引用是否仍然存在（防止组件已卸载）
    if (floatingQuotes.value.includes(newQuote)) {
      newQuote.opacity = newQuote.targetOpacity
    }
  }, 100)
  
  // 淡出并移除 - 在显示一段时间后
  setTimeout(() => {
    // 检查引用是否仍然存在（防止组件已卸载）
    if (floatingQuotes.value.includes(newQuote)) {
      // 开始淡出
      newQuote.opacity = 0
      
      // 淡出完成后从数组中移除
      setTimeout(() => {
        const index = floatingQuotes.value.indexOf(newQuote)
        if (index !== -1) {
          floatingQuotes.value.splice(index, 1)
        }
      }, 2000) // 淡出动画时间
    }
  }, 4000 + Math.random() * 3000) // 显示4-7秒
}

// 管理浮动文字的定时器
let floatingInterval: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  // 启动文字切换定时器
  quoteInterval = setInterval(() => {
    currentQuoteIndex.value = (currentQuoteIndex.value + 1) % spiritQuotes.value.length
  }, 3000)
  
  // 初始化更多浮动文字
  for (let i = 0; i < 6; i++) {
    setTimeout(() => {
      createNewFloatingQuote()
    }, i * 800)
  }
  
  // 定期添加新的浮动文字，频率更高
  floatingInterval = setInterval(() => {
    // 增加最大数量
    if (floatingQuotes.value.length < 12) {
      createNewFloatingQuote()
    }
  }, 1800)
})

onUnmounted(() => {
  // 清除定时器
  if (quoteInterval) {
    clearInterval(quoteInterval)
    quoteInterval = null
  }
  
  if (floatingInterval) {
    clearInterval(floatingInterval)
    floatingInterval = null
  }
})
</script>

<style lang="scss" scoped>
$prefix-cls: #{$namespace}-login;

.#{$prefix-cls} {
  overflow: auto;
  position: relative;

  &__left {
    background-color: #c52121;
    position: relative;
    
    &::before {
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100%;
      height: 100%;
      background-image: url('/bg.jpg');
      background-position: center;
      background-repeat: no-repeat;
      background-size: cover;
      opacity: 0.2;
      content: '';
    }
    
    &::after {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: linear-gradient(135deg, rgba(197, 33, 33, 0.9) 0%, rgba(140, 21, 21, 0.9) 100%);
      z-index: -1;
      content: '';
    }
  }
}

.red-text-shadow {
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.spirit-quotes-container {
  position: relative;
  height: 60px;
  overflow: hidden;
  width: 100%;
}

.spirit-quote {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  opacity: 0;
  transform: translateY(20px);
  transition: opacity 0.5s ease, transform 0.5s ease;
  text-align: center;
}

.spirit-quote.active {
  opacity: 1;
  transform: translateY(0);
}

/* 浮动文字样式 */
.floating-quotes-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 允许点击穿透 */
  z-index: 1;
  overflow: hidden;
}

.floating-quote {
  position: absolute;
  color: #c52121;
  font-weight: bold;
  white-space: nowrap;
  opacity: 0;
  transition: opacity 3s ease; /* 延长渐变时间 */
  pointer-events: none;
  z-index: 1;
  text-shadow: 0 0 2px rgba(255, 255, 255, 0.5); /* 添加文字阴影增强可读性 */
}
</style>

<style lang="scss">
.dark .login-form {
  .el-divider__text {
    background-color: var(--login-bg-color);
  }

  .el-card {
    background-color: var(--login-bg-color);
  }
}
</style>
