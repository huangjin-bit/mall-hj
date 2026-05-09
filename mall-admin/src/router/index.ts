import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index.vue'),
      meta: { title: '登录' }
    },
    {
      path: '/404',
      name: '404',
      component: () => import('@/views/error/404.vue'),
      meta: { title: '页面不存在' }
    }
  ]
})

// Dynamic route guard
const whiteList = ['/login']
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('admin_token')
  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const { usePermissionStore } = await import('@/stores/permission')
      const permissionStore = usePermissionStore()
      if (permissionStore.routes.length > 0) {
        next()
      } else {
        try {
          const { useUserStore } = await import('@/stores/user')
          const userStore = useUserStore()
          await userStore.getUserInfoAction()
          permissionStore.setPermissions(userStore.permissions)
          await permissionStore.generateRoutes()
          next({ ...to, replace: true })
        } catch (error) {
          localStorage.removeItem('admin_token')
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
