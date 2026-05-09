import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginByUsername, loginByPhone, register, logout as apiLogout, sendSmsCode } from '@/api/auth'
import { getMemberInfo } from '@/api/member/member'
import type { Member } from '@/api/member/member'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('token') || '')
  const memberId = ref<number>(Number(localStorage.getItem('memberId')) || 0)
  const username = ref<string>(localStorage.getItem('username') || '')
  const memberInfo = ref<Member | null>(null)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)

  // 用户名密码登录
  const loginByUsernameAction = async (data: { username: string; password: string }) => {
    try {
      const res = await loginByUsername(data)
      token.value = res
      // 假设返回的 token 包含 memberId 和 username 信息，或者通过解析 JWT 获取
      // 这里简化处理，实际应该从后端返回或解析 token
      username.value = data.username
      localStorage.setItem('token', res)
      localStorage.setItem('username', data.username)
      return true
    } catch (error) {
      console.error('[User Store] Login failed', error)
      return false
    }
  }

  // 手机号登录
  const loginByPhoneAction = async (data: { phone: string; code: string }) => {
    try {
      const res = await loginByPhone(data)
      token.value = res
      username.value = data.phone
      localStorage.setItem('token', res)
      localStorage.setItem('username', data.phone)
      return true
    } catch (error) {
      console.error('[User Store] Phone login failed', error)
      return false
    }
  }

  // 注册
  const registerAction = async (data: { username: string; password: string; phone: string; code: string }) => {
    try {
      await register(data)
      return true
    } catch (error) {
      console.error('[User Store] Register failed', error)
      return false
    }
  }

  // 退出登录
  const logoutAction = async () => {
    try {
      await apiLogout()
    } catch (error) {
      console.error('[User Store] Logout failed', error)
    } finally {
      token.value = ''
      memberId.value = 0
      username.value = ''
      memberInfo.value = null
      localStorage.removeItem('token')
      localStorage.removeItem('memberId')
      localStorage.removeItem('username')
    }
  }

  // 获取会员信息
  const fetchMemberInfo = async () => {
    if (!memberId.value) return

    try {
      const res = await getMemberInfo(memberId.value)
      memberInfo.value = res
      return res
    } catch (error) {
      console.error('[User Store] Fetch member info failed', error)
      return null
    }
  }

  // 设置 memberId
  const setMemberId = (id: number) => {
    memberId.value = id
    localStorage.setItem('memberId', String(id))
  }

  return {
    token,
    memberId,
    username,
    memberInfo,
    isLoggedIn,
    loginByUsername: loginByUsernameAction,
    loginByPhone: loginByPhoneAction,
    register: registerAction,
    logout: logoutAction,
    fetchMemberInfo,
    setMemberId
  }
})
