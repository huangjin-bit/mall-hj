<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/store/user'
import { sendSmsCode } from '@/api/auth'
import { validatePhone, validatePassword } from '@/utils'

const userStore = useUserStore()

// 登录类型：username-用户名密码，phone-手机验证码
const loginType = ref('username')

// 用户名密码登录表单
const usernameForm = ref({
  username: '',
  password: ''
})

// 手机号登录表单
const phoneForm = ref({
  phone: '',
  smsCode: ''
})

// 发送验证码状态
const sending = ref(false)
const countdown = ref(0)
const timer = ref(null)

/**
 * 切换登录类型
 */
function switchLoginType(type) {
  loginType.value = type
}

/**
 * 发送验证码
 */
async function handleSendSms() {
  if (!validatePhone(phoneForm.value.phone)) {
    uni.showToast({
      title: '请输入正确的手机号',
      icon: 'none'
    })
    return
  }

  try {
    sending.value = true
    await sendSmsCode({ phone: phoneForm.value.phone })

    uni.showToast({
      title: '验证码已发送',
      icon: 'success'
    })

    // 开始倒计时
    countdown.value = 60
    timer.value = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer.value)
        timer.value = null
      }
    }, 1000)
  } catch (error) {
    console.error('[SendSms Error]', error)
  } finally {
    sending.value = false
  }
}

/**
 * 用户名密码登录
 */
async function handleUsernameLogin() {
  const { username, password } = usernameForm.value

  if (!username) {
    uni.showToast({ title: '请输入用户名', icon: 'none' })
    return
  }

  if (!password) {
    uni.showToast({ title: '请输入密码', icon: 'none' })
    return
  }

  try {
    await userStore.loginByUsernameAction({ username, password })

    // 登录成功，返回上一页或首页
    setTimeout(() => {
      uni.switchTab({
        url: '/pages/index/index'
      })
    }, 500)
  } catch (error) {
    console.error('[UsernameLogin Error]', error)
  }
}

/**
 * 手机号登录
 */
async function handlePhoneLogin() {
  const { phone, smsCode } = phoneForm.value

  if (!validatePhone(phone)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }

  if (!smsCode) {
    uni.showToast({ title: '请输入验证码', icon: 'none' })
    return
  }

  try {
    await userStore.loginByPhoneAction({ phone, smsCode })

    // 登录成功，返回上一页或首页
    setTimeout(() => {
      uni.switchTab({
        url: '/pages/index/index'
      })
    }, 500)
  } catch (error) {
    console.error('[PhoneLogin Error]', error)
  }
}

/**
 * 跳转注册页
 */
function goToRegister() {
  uni.navigateTo({
    url: '/pages/auth/register'
  })
}

/**
 * 忘记密码
 */
function forgetPassword() {
  uni.showToast({
    title: '请联系客服重置密码',
    icon: 'none'
  })
}
</script>

<template>
  <view class="login-page">
    <!-- Logo和标题 -->
    <view class="login-header">
      <text class="logo">🛍️</text>
      <text class="title">Mall-HJ</text>
      <text class="subtitle">欢迎登录</text>
    </view>

    <!-- 登录类型切换 -->
    <view class="login-tabs">
      <view
        class="tab-item"
        :class="{ active: loginType === 'username' }"
        @tap="switchLoginType('username')"
      >
        <text>账号登录</text>
      </view>
      <view
        class="tab-item"
        :class="{ active: loginType === 'phone' }"
        @tap="switchLoginType('phone')"
      >
        <text>手机登录</text>
      </view>
    </view>

    <!-- 用户名密码登录 -->
    <view v-if="loginType === 'username'" class="login-form">
      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">👤</text>
          <input
            v-model="usernameForm.username"
            class="form-input"
            placeholder="请输入用户名"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>

      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">🔒</text>
          <input
            v-model="usernameForm.password"
            class="form-input"
            type="password"
            placeholder="请输入密码"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>

      <view class="form-actions">
        <text class="forget-btn" @tap="forgetPassword">忘记密码？</text>
      </view>

      <button class="login-btn" @tap="handleUsernameLogin">登录</button>
    </view>

    <!-- 手机号登录 -->
    <view v-else class="login-form">
      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">📱</text>
          <input
            v-model="phoneForm.phone"
            class="form-input"
            type="number"
            maxlength="11"
            placeholder="请输入手机号"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>

      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">💬</text>
          <input
            v-model="phoneForm.smsCode"
            class="form-input"
            type="number"
            maxlength="6"
            placeholder="请输入验证码"
            placeholder-class="input-placeholder"
          />
          <button
            class="sms-btn"
            :disabled="sending || countdown > 0"
            @tap="handleSendSms"
          >
            {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
          </button>
        </view>
      </view>

      <button class="login-btn" @tap="handlePhoneLogin">登录</button>
    </view>

    <!-- 注册链接 -->
    <view class="register-link">
      <text class="register-text">还没有账号？</text>
      <text class="register-btn" @tap="goToRegister">立即注册</text>
    </view>

    <!-- 第三方登录 -->
    <view class="third-party">
      <view class="divider">
        <text class="divider-text">其他登录方式</text>
      </view>
      <view class="third-party-icons">
        <view class="third-party-item">
          <text class="third-party-icon">💬</text>
          <text class="third-party-text">微信</text>
        </view>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 40rpx;
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;
}

.logo {
  font-size: 120rpx;
  margin-bottom: 20rpx;
}

.title {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10rpx;
}

.subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-tabs {
  display: flex;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 16rpx;
  padding: 8rpx;
  margin-bottom: 40rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);

  &.active {
    background-color: #fff;
    color: #333;
    font-weight: bold;
  }
}

.login-form {
  background-color: #fff;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  padding: 24rpx 30rpx;
}

.input-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.form-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.input-placeholder {
  color: #999;
}

.sms-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 40rpx;
  padding: 16rpx 32rpx;
  font-size: 24rpx;
  border: none;
  white-space: nowrap;

  &:disabled {
    opacity: 0.5;
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 30rpx;
}

.forget-btn {
  font-size: 24rpx;
  color: #667eea;
}

.login-btn {
  width: 100%;
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 28rpx;
  font-size: 32rpx;
  border: none;
  font-weight: bold;
}

.register-link {
  text-align: center;
  margin-bottom: 60rpx;
}

.register-text {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.register-btn {
  font-size: 28rpx;
  color: #fff;
  font-weight: bold;
}

.third-party {
  background-color: #fff;
  border-radius: 24rpx;
  padding: 40rpx;
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background-color: #f0f0f0;
}

.divider-text {
  padding: 0 20rpx;
  font-size: 24rpx;
  color: #999;
}

.third-party-icons {
  display: flex;
  justify-content: center;
  gap: 60rpx;
}

.third-party-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
}

.third-party-icon {
  font-size: 60rpx;
}

.third-party-text {
  font-size: 24rpx;
  color: #666;
}
</style>
