import { defineStore } from 'pinia'
import { loginByUsername, loginByPhone, logout as logoutApi } from '@/api/auth'
import { getMemberInfo } from '@/api/member'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: uni.getStorageSync('token') || '',
    memberId: uni.getStorageSync('memberId') || null,
    username: uni.getStorageSync('username') || '',
    userInfo: null
  }),

  getters: {
    // 是否已登录
    isLogin: (state) => !!state.token,
    // 获取用户昵称
    nickname: (state) => state.userInfo?.nickname || state.username || '未登录'
  },

  actions: {
    /**
     * 用户名密码登录
     */
    async loginByUsernameAction(loginForm) {
      try {
        const res = await loginByUsername(loginForm)
        this.token = res.token
        this.memberId = res.memberId
        this.username = res.username

        // 持久化存储
        uni.setStorageSync('token', res.token)
        uni.setStorageSync('memberId', res.memberId)
        uni.setStorageSync('username', res.username)

        // 获取用户详细信息
        await this.fetchUserInfo()

        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })

        return res
      } catch (error) {
        console.error('[Login Error]', error)
        throw error
      }
    },

    /**
     * 手机号登录
     */
    async loginByPhoneAction(loginForm) {
      try {
        const res = await loginByPhone(loginForm)
        this.token = res.token
        this.memberId = res.memberId
        this.username = res.username

        // 持久化存储
        uni.setStorageSync('token', res.token)
        uni.setStorageSync('memberId', res.memberId)
        uni.setStorageSync('username', res.username)

        // 获取用户详细信息
        await this.fetchUserInfo()

        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })

        return res
      } catch (error) {
        console.error('[Login Error]', error)
        throw error
      }
    },

    /**
     * 退出登录
     */
    async logout() {
      try {
        await logoutApi()
      } catch (error) {
        console.error('[Logout Error]', error)
      } finally {
        // 清除状态
        this.token = ''
        this.memberId = null
        this.username = ''
        this.userInfo = null

        // 清除本地存储
        uni.removeStorageSync('token')
        uni.removeStorageSync('memberId')
        uni.removeStorageSync('username')

        // 跳转到登录页
        uni.reLaunch({
          url: '/pages/auth/login'
        })
      }
    },

    /**
     * 获取用户信息
     */
    async fetchUserInfo() {
      if (!this.token) return

      try {
        const res = await getMemberInfo()
        this.userInfo = res
      } catch (error) {
        console.error('[FetchUserInfo Error]', error)
        // 如果获取用户信息失败，可能是token过期，清除登录状态
        if (error.message?.includes('401') || error.message?.includes('登录')) {
          await this.logout()
        }
      }
    },

    /**
     * 检查登录状态
     */
    checkAuth() {
      if (!this.token) {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        uni.navigateTo({
          url: '/pages/auth/login'
        })
        return false
      }
      return true
    }
  }
})
