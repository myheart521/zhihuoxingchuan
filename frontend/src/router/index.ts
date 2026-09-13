import type { App } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { createRouter, createWebHistory } from 'vue-router'
import remainingRouter from './modules/remaining'

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_BASE_PATH), // createWebHashHistory URL带#，createWebHistory URL不带#
  strict: true,
  routes: [
    {
      path: '/',
      redirect: '/index' // 将根路径重定向到 /index
    },
    {
      path: '/index',
      component: () => import('@/layout/Layout.vue'), // 如果有布局组件
      children: [
        {
          path: '',
          component: () => import('@/views/Home/Index.vue'),
          meta: {
            title: '首页',
            // 如果您希望不需要登录也能访问，设置为 false
            requiresAuth: false 
          }
        }
      ]
    },
    ...remainingRouter as RouteRecordRaw[]
  ],
  scrollBehavior: () => ({ left: 0, top: 0 })
})

export const resetRouter = (): void => {
  const resetWhiteNameList = ['Redirect', 'Login', 'NoFind', 'Root']
  router.getRoutes().forEach((route) => {
    const { name } = route
    if (name && !resetWhiteNameList.includes(name as string)) {
      router.hasRoute(name) && router.removeRoute(name)
    }
  })
}

export const setupRouter = (app: App<Element>) => {
  app.use(router)
}

export default router
