<template>
  <div class="login-root" v-show="getShow">
 

    <!-- 登录表单区域 -->
    <div
      class="box-root padding-top--24 flex-flex flex-direction--column"
      style="flex-grow: 1; z-index: 9"
    >
      <div
        class="box-root padding-top--48 padding-bottom--24 flex-flex flex-justifyContent--center"
      >
        <h1 class="site-title">特别能战斗精神学习平台</h1>
      </div>
      <div class="formbg-outer">
        <div class="formbg">
          <div class="formbg-inner padding-horizontal--48">
            <span class="padding-bottom--15">{{ t('login.login') }}</span>
  <el-form
    ref="formLogin"
    :model="loginData.loginForm"
    :rules="LoginRules"
    class="login-form"
    label-position="top"
    label-width="120px"
    size="large"
  >
              <div class="field padding-bottom--24">
                <label>{{ t('login.usernamePlaceholder') }}</label>
          <el-input
            v-model="loginData.loginForm.username"
            :prefix-icon="iconAvatar"
                  class="custom-input"
                />
              </div>

              <div class="field padding-bottom--24">
                <div class="grid--50-50">
                  <label>{{ t('login.passwordPlaceholder') }}</label>
                  <div class="reset-pass">
                    <el-link type="primary" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
                      {{ t('login.forgetPassword') }}
                    </el-link>
                  </div>
                </div>
          <el-input
            v-model="loginData.loginForm.password"
            :prefix-icon="iconLock"
            show-password
            type="password"
                  class="custom-input"
            @keyup.enter="getCode()"
          />
              </div>

              <div class="field field-checkbox padding-bottom--24 flex-flex align-center">
              <el-checkbox v-model="loginData.loginForm.rememberMe">
                {{ t('login.remember') }}
              </el-checkbox>
              </div>

              <div class="field padding-bottom--24">
          <XButton
            :loading="loginLoading"
            :title="t('login.login')"
                  class="w-[100%] login-button"
            type="primary"
            @click="getCode()"
          />
              </div>

      <Verify
        v-if="loginData.captchaEnable === 'true'"
        ref="verify"
        :captchaType="captchaType"
        :imgSize="{ width: '400px', height: '200px' }"
        mode="pop"
        @success="handleLogin"
      />

              <div class="field">
              <XButton
                :title="t('login.btnRegister')"
                  class="w-[100%] register-button"
                @click="setLoginState(LoginStateEnum.REGISTER)"
              />
              </div>
  </el-form>
          </div>
        </div>
        <div class="footer-link padding-top--24">
          <span>{{ t('login.welcome') }}</span>
          <div class="listing padding-top--24 padding-bottom--24 flex-flex center-center">
            <span>© 特别能战斗精神学习平台</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ElLoading } from 'element-plus'
import type { RouteLocationNormalizedLoaded } from 'vue-router'

import { useIcon } from '@/hooks/web/useIcon'

import * as authUtil from '@/utils/auth'
import { usePermissionStore } from '@/store/modules/permission'
import * as LoginApi from '@/api/login'
import { LoginStateEnum, useFormValid, useLoginState } from './useLogin'

defineOptions({ name: 'LoginForm' })

const { t } = useI18n()
const iconAvatar = useIcon({ icon: 'ep:avatar' })
const iconLock = useIcon({ icon: 'ep:lock' })
const formLogin = ref()
const { validForm } = useFormValid(formLogin)
const { setLoginState, getLoginState } = useLoginState()
const { currentRoute, push } = useRouter()
const permissionStore = usePermissionStore()
const redirect = ref<string>('')
const loginLoading = ref(false)
const verify = ref()
const captchaType = ref('blockPuzzle') // blockPuzzle 滑块 clickWord 点击文字


const currentQuoteIndex = ref(0)

// 定时切换文字
let quoteInterval: any = null

onMounted(() => {
  getLoginFormCache()
  getTenantByWebsite()

})

onUnmounted(() => {
  // 清除定时器
  if (quoteInterval) {
    clearInterval(quoteInterval)
  }
})

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN)

const LoginRules = {
  tenantName: [{ required: true, message: '租户名称不能为空', trigger: 'blur' }],
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
}
const loginData = reactive({
  isShowPassword: false,
  captchaEnable: import.meta.env.VITE_APP_CAPTCHA_ENABLE,
  tenantEnable: import.meta.env.VITE_APP_TENANT_ENABLE,
  loginForm: {
    tenantName: import.meta.env.VITE_APP_DEFAULT_LOGIN_TENANT || '',
    username: '',
    password: '',
    captchaVerification: '',
    rememberMe: true // 默认记录我。如果不需要，可手动修改
  }
})

// 获取验证码
const getCode = async () => {
  // 情况一，未开启：则直接登录
  if (loginData.captchaEnable === 'false') {
    await handleLogin({})
  } else {
    // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
    // 弹出验证码
    verify.value.show()
  }
}
// 获取租户 ID
const getTenantId = async () => {
  if (loginData.tenantEnable === 'true') {
    const res = await LoginApi.getTenantIdByName(loginData.loginForm.tenantName)
    authUtil.setTenantId(res)
  }
}
// 记住我
const getLoginFormCache = () => {
  const loginForm = authUtil.getLoginForm()
  if (loginForm) {
    loginData.loginForm = {
      ...loginData.loginForm,
      username: loginForm.username ? loginForm.username : loginData.loginForm.username,
      password: loginForm.password ? loginForm.password : loginData.loginForm.password,
      rememberMe: loginForm.rememberMe,
      tenantName: loginForm.tenantName ? loginForm.tenantName : loginData.loginForm.tenantName
    }
  }
}
// 根据域名，获得租户信息
const getTenantByWebsite = async () => {
  const website = location.host
  const res = await LoginApi.getTenantByWebsite(website)
  if (res) {
    loginData.loginForm.tenantName = res.name
    authUtil.setTenantId(res.id)
  }
}
const loading = ref() // ElLoading.service 返回的实例
// 登录
const handleLogin = async (params: any) => {
  loginLoading.value = true
  try {
    await getTenantId()
    const data = await validForm()
    if (!data) {
      return
    }
    const loginDataLoginForm = { ...loginData.loginForm }
    loginDataLoginForm.captchaVerification = params.captchaVerification
    const res = await LoginApi.login(loginDataLoginForm)
    if (!res) {
      return
    }
    loading.value = ElLoading.service({
      lock: true,
      text: '正在加载系统中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    if (loginDataLoginForm.rememberMe) {
      authUtil.setLoginForm(loginDataLoginForm)
    } else {
      authUtil.removeLoginForm()
    }
    authUtil.setToken(res)
    if (!redirect.value) {
      redirect.value = '/'
    }
    // 判断是否为SSO登录
    if (redirect.value.indexOf('sso') !== -1) {
      window.location.href = window.location.href.replace('/login?redirect=', '')
    } else {
      await push({ path: redirect.value || permissionStore.addRouters[0].path })
    }
  } finally {
    loginLoading.value = false
    loading.value?.close()
  }
}

watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true
  }
)
</script>

<style lang="scss" scoped>
* {
  padding: 0;
  margin: 0;
  color: #1a1f36;
  box-sizing: border-box;
  word-wrap: break-word;
  font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Ubuntu, sans-serif;
}

.login-root {
  background: #fff;
  display: flex;
  width: 100%;
  min-height: 100vh;
  overflow: hidden;
  position: relative;
}

.loginbackground {
  min-height: 692px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  top: 0;
  z-index: 0;
  overflow: hidden;
}

.flex-flex {
  display: flex;
}

.align-center {
  align-items: center;
}

.center-center {
  align-items: center;
  justify-content: center;
}

.box-root {
  box-sizing: border-box;
}

.flex-direction--column {
  -ms-flex-direction: column;
  flex-direction: column;
}

.loginbackground-gridContainer {
  display: grid;
  grid-template-columns: [start] 1fr [left-gutter] repeat(16, 86.6px) [left-gutter] 1fr [end];
  grid-template-rows: [top] 1fr [top-gutter] repeat(8, 64px) [bottom-gutter] 1fr [bottom];
  justify-content: center;
  margin: 0 -2%;
  transform: rotate(-12deg) skew(-12deg);
}

.box-divider--light-all-2 {
  box-shadow: inset 0 0 0 2px #e3e8ee;
}

.box-background--red {
  background-color: #c52121;
}

.box-background--white {
  background-color: #ffffff;
}

.box-background--red800 {
  background-color: #8c1515;
}

.box-background--gray100 {
  background-color: #e3e8ee;
}

.box-background--red200 {
  background-color: #e63946;
}

.padding-top--64 {
  padding-top: 64px;
}

.padding-top--24 {
  padding-top: 24px;
}

.padding-top--48 {
  padding-top: 48px;
}

.padding-bottom--24 {
  padding-bottom: 24px;
}

.padding-horizontal--48 {
  padding: 48px;
}

.padding-bottom--15 {
  padding-bottom: 15px;
}

.flex-justifyContent--center {
  -ms-flex-pack: center;
  justify-content: center;
}

.formbg {
  margin: 0px auto;
  width: 100%;
  max-width: 448px;
  border-radius: 4px;
  box-shadow: rgba(60, 66, 87, 0.12) 0px 7px 14px 0px, rgba(0, 0, 0, 0.12) 0px 3px 6px 0px;
}

span {
  display: block;
  font-size: 20px;
  line-height: 28px;
  color: #1a1f36;
}

label {
  margin-bottom: 10px;
}

.reset-pass a,
label {
  font-size: 14px;
  font-weight: 600;
  display: block;
}

.reset-pass > a {
  text-align: right;
  margin-bottom: 10px;
}

.grid--50-50 {
  display: grid;
  grid-template-columns: 50% 50%;
  align-items: center;
}

.field {
  margin-bottom: 15px;
}

.custom-input {
  font-size: 16px;
  line-height: 28px;
  width: 100%;
  min-height: 44px;
  border: unset;
  border-radius: 4px;
  outline-color: rgb(84, 105, 212, 0.5);
  background-color: rgb(255, 255, 255);
  box-shadow: rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(60, 66, 87, 0.16) 0px 0px 0px 1px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px;
}

.login-button {
  background-color: #c52121 !important;
  border-color: #c52121 !important;
  box-shadow: rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0.12) 0px 1px 1px 0px, 
              #c52121 0px 0px 0px 1px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(0, 0, 0, 0) 0px 0px 0px 0px, 
              rgba(60, 66, 87, 0.08) 0px 2px 5px 0px;
  color: #fff;
  font-weight: 600;
  cursor: pointer;
}

.register-button {
  background-color: transparent !important;
  border: 1px solid #c52121 !important;
  color: #c52121 !important;
  font-weight: 600;
}

.field-checkbox input {
  width: 20px;
  height: 15px;
  margin-right: 5px;
  box-shadow: unset;
  min-height: unset;
}

.field-checkbox label {
  display: flex;
  align-items: center;
  margin: 0;
}

.footer-link span {
  font-size: 14px;
  text-align: center;
}

.listing a {
  color: #697386;
  font-weight: 600;
  margin: 0 10px;
}

.animationRightLeft {
  animation: animationRightLeft 2s ease-in-out infinite;
}

.animationLeftRight {
  animation: animationLeftRight 2s ease-in-out infinite;
}

.tans3s {
  animation: animationLeftRight 3s ease-in-out infinite;
}

.tans4s {
  animation: animationLeftRight 4s ease-in-out infinite;
}

@keyframes animationLeftRight {
  0% {
    transform: translateX(0px);
  }
  50% {
    transform: translateX(1000px);
  }
  100% {
    transform: translateX(0px);
  }
}

@keyframes animationRightLeft {
  0% {
    transform: translateX(0px);
  }
  50% {
    transform: translateX(-1000px);
  }
  100% {
    transform: translateX(0px);
  }
}

.spirit-quotes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
  pointer-events: none;
}

.quote-item {
  position: absolute;
  font-size: 2.5rem;
  font-weight: bold;
  color: rgba(197, 33, 33, 0.1);
  text-align: center;
  transition: opacity 1s ease;
    width: 100%;
  padding: 0 20px;
}

.site-title {
  color: #c52121;
  font-weight: bold;
  letter-spacing: 1px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

:deep(.el-input__wrapper) {
  box-shadow: none !important;
  background-color: transparent !important;
}

:deep(.el-input__inner) {
  height: 44px !important;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #c52121 !important;
  border-color: #c52121 !important;
}

:deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #c52121 !important;
}

:deep(.el-link.el-link--primary) {
  color: #c52121 !important;
}

:deep(.el-link.el-link--primary:hover) {
  color: #e63946 !important;
}

@media (max-width: 768px) {
  .padding-horizontal--48 {
    padding: 24px;
  }
  
  .quote-item {
    font-size: 1.5rem;
  }
}
</style>
