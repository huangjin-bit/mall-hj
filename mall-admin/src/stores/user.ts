import { defineStore } from 'pinia'
import { adminLogin, getAdminInfo, adminLogout } from '@/api/sysAuth'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('admin_token') || '')
  const userId = ref<number | null>(null)
  const username = ref<string>('')
  const nickname = ref<string>('')
  const avatar = ref<string>('')
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])

  const loginAction = async (data: { username: string; password: string }) => {
    const result = await adminLogin(data)
    token.value = result.token
    userId.value = result.userId
    username.value = result.username
    nickname.value = result.nickname
    avatar.value = result.avatar || ''
    localStorage.setItem('admin_token', result.token)
    return result
  }

  const getUserInfoAction = async () => {
    const result = await getAdminInfo()
    userId.value = result.user.id
    username.value = result.user.username
    nickname.value = result.user.nickname
    avatar.value = result.user.avatar || ''
    roles.value = result.roles || []
    permissions.value = result.permissions || []
  }

  const logoutAction = async () => {
    try {
      await adminLogout()
    } catch {}
    token.value = ''
    userId.value = null
    username.value = ''
    nickname.value = ''
    avatar.value = ''
    roles.value = []
    permissions.value = []
    localStorage.removeItem('admin_token')
  }

  return {
    token,
    userId,
    username,
    nickname,
    avatar,
    roles,
    permissions,
    loginAction,
    getUserInfoAction,
    logoutAction
  }
})
