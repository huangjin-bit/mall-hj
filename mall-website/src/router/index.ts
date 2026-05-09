import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('@/layouts/WebsiteLayout.vue'),
      children: [
        {
          path: '',
          name: 'Home',
          component: () => import('@/views/home/index.vue')
        },
        {
          path: 'search',
          name: 'Search',
          component: () => import('@/views/search/index.vue')
        },
        {
          path: 'product/list',
          name: 'ProductList',
          component: () => import('@/views/product/list.vue')
        },
        {
          path: 'product/detail/:id',
          name: 'ProductDetail',
          component: () => import('@/views/product/detail.vue')
        },
        {
          path: 'login',
          name: 'Login',
          component: () => import('@/views/auth/login.vue')
        },
        {
          path: 'register',
          name: 'Register',
          component: () => import('@/views/auth/register.vue')
        },
        {
          path: 'cart',
          name: 'Cart',
          component: () => import('@/views/cart/index.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'order/confirm',
          name: 'OrderConfirm',
          component: () => import('@/views/order/confirm.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'order/pay/:orderSn',
          name: 'OrderPay',
          component: () => import('@/views/order/pay.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'order/list',
          name: 'OrderList',
          component: () => import('@/views/order/list.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'order/detail/:id',
          name: 'OrderDetail',
          component: () => import('@/views/order/detail.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'user',
          name: 'User',
          component: () => import('@/views/user/index.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'user/profile',
          name: 'UserProfile',
          component: () => import('@/views/user/profile.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'user/address',
          name: 'UserAddress',
          component: () => import('@/views/user/address.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'user/collect',
          name: 'UserCollect',
          component: () => import('@/views/user/collect.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'user/coupons',
          name: 'UserCoupons',
          component: () => import('@/views/user/coupons.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'seckill',
          name: 'Seckill',
          component: () => import('@/views/seckill/index.vue')
        },
        {
          path: 'seckill/detail/:sessionId/:skuId',
          name: 'SeckillDetail',
          component: () => import('@/views/seckill/detail.vue'),
          meta: { requiresAuth: true }
        }
      ]
    }
  ]
})

// 导航守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    // 需要登录但未登录，跳转到登录页
    next({
      name: 'Login',
      query: { redirect: to.fullPath }
    })
  } else {
    next()
  }
})

export default router
