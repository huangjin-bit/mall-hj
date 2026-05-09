import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getRouters } from '@/api/sysAuth'
import router from '@/router'
import type { RouteRecordRaw } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'

const modules = import.meta.glob('../views/**/*.vue')

export const usePermissionStore = defineStore('permission', () => {
  const routes = ref<RouteRecordRaw[]>([])
  const permissions = ref<string[]>([])

  const generateRoutes = async () => {
    const data = await getRouters()
    const dynamicRoutes = buildRoutes(data)
    routes.value = dynamicRoutes
    dynamicRoutes.forEach(route => {
      router.addRoute(route)
    })
    // Add catch-all 404 last
    router.addRoute({ path: '/:pathMatched(.*)*', redirect: '/404' })
  }

  const buildRoutes = (data: any[]): RouteRecordRaw[] => {
    return data.map(item => {
      const route: RouteRecordRaw = {
        path: item.path,
        name: item.name,
        meta: { title: item.meta?.title, icon: item.meta?.icon },
        children: item.children ? buildRoutes(item.children) : undefined
      }
      if (item.component === 'Layout') {
        route.component = AdminLayout
      } else if (item.component) {
        const componentPath = `../views/${item.component}.vue`
        route.component = modules[componentPath] || (() => import('@/views/login/index.vue'))
      }
      if (item.redirect) route.redirect = item.redirect
      if (item.children && item.children.length > 0) {
        route.children = buildRoutes(item.children)
      }
      return route
    })
  }

  const setPermissions = (perms: string[]) => {
    permissions.value = perms
  }

  const hasPermission = (perm: string): boolean => {
    if (permissions.value.includes('*:*:*')) return true
    return permissions.value.includes(perm)
  }

  return { routes, permissions, generateRoutes, setPermissions, hasPermission }
})
