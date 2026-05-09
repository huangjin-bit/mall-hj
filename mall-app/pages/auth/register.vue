<script setup>
import { ref } from 'vue'
import { register, sendSmsCode } from '@/api/auth'
import { validatePhone, validatePassword } from '@/utils'

// 注册表单
const registerForm = ref({
  username: '',
  phone: '',
  password: '',
  confirmPassword: '',
  smsCode: ''
})

// 发送验证码状态
const sending = ref(false)
const countdown = ref(0)
const timer = ref(null)

// 同意协议
const agreed = ref(false)

/**
 * 发送验证码
 */
async function handleSendSms() {
  if (!validatePhone(registerForm.value.phone)) {
    uni.showToast({
      title: '请输入正确的手机号',
      icon: 'none'
    })
    return
  }

  try {
    sending.value = true
    await sendSmsCode({ phone: registerForm.value.phone })

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
 * 注册
 */
async function handleRegister() {
  const { username, phone, password, confirmPassword, smsCode } = registerForm.value

  // 表单验证
  if (!username) {
    uni.showToast({ title: '请输入用户名', icon: 'none' })
    return
  }

  if (username.length < 3) {
    uni.showToast({ title: '用户名至少3个字符', icon: 'none' })
    return
  }

  if (!validatePhone(phone)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }

  if (!validatePassword(password)) {
    uni.showToast({ title: '密码为6-20位字母和数字组合', icon: 'none' })
    return
  }

  if (password !== confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return
  }

  if (!smsCode) {
    uni.showToast({ title: '请输入验证码', icon: 'none' })
    return
  }

  if (!agreed.value) {
    uni.showToast({ title: '请阅读并同意用户协议', icon: 'none' })
    return
  }

  try {
    await register({ username, phone, password, smsCode })

    uni.showToast({
      title: '注册成功，请登录',
      icon: 'success',
      duration: 2000
    })

    // 跳转到登录页
    setTimeout(() => {
      uni.navigateBack()
    }, 2000)
  } catch (error) {
    console.error('[Register Error]', error)
  }
}

/**
 * 查看用户协议
 */
function viewAgreement() {
  uni.showToast({
    title: '用户协议功能开发中',
    icon: 'none'
  })
}
</script>

<template>
  <view class="register-page">
    <!-- Logo和标题 -->
    <view class="register-header">
      <text class="logo">🛍️</text>
      <text class="title">创建账号</text>
      <text class="subtitle">加入Mall-HJ</text>
    </view>

    <!-- 注册表单 -->
    <view class="register-form">
      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">👤</text>
          <input
            v-model="registerForm.username"
            class="form-input"
            placeholder="请输入用户名"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>

      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">📱</text>
          <input
            v-model="registerForm.phone"
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
            v-model="registerForm.smsCode"
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

      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">🔒</text>
          <input
            v-model="registerForm.password"
            class="form-input"
            type="password"
            placeholder="请输入密码(6-20位字母和数字)"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>

      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">🔒</text>
          <input
            v-model="registerForm.confirmPassword"
            class="form-input"
            type="password"
            placeholder="请再次输入密码"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>

      <!-- 用户协议 -->
      <view class="agreement">
        <view class="agreement-checkbox" @tap="agreed = !agreed">
          <view class="checkbox" :class="{ checked: agreed }">
            <text v-if="agreed" class="check-icon">✓</text>
          </view>
        </view>
        <text class="agreement-text">我已阅读并同意</text>
        <text class="agreement-link" @tap="viewAgreement">《用户协议》</text>
        <text class="agreement-text">和</text>
        <text class="agreement-link" @tap="viewAgreement">《隐私政策》</text>
      </view>

      <button class="register-btn" @tap="handleRegister">注册</button>
    </view>

    <!-- 登录链接 -->
    <view class="login-link">
      <text class="login-text">已有账号？</text>
      <text class="login-btn" @tap="uni.navigateBack()">立即登录</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 40rpx;
}

.register-header {
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

.register-form {
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

.agreement {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 30rpx;
  padding: 0 10rpx;
}

.agreement-checkbox {
  margin-right: 10rpx;
}

.checkbox {
  width: 36rpx;
  height: 36rpx;
  border: 2px solid #e0e0e0;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  &.checked {
    background-color: #e93b3d;
    border-color: #e93b3d;
  }
}

.check-icon {
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}

.agreement-text {
  font-size: 24rpx;
  color: #666;
}

.agreement-link {
  font-size: 24rpx;
  color: #667eea;
}

.register-btn {
  width: 100%;
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 28rpx;
  font-size: 32rpx;
  border: none;
  font-weight: bold;
}

.login-link {
  text-align: center;
}

.login-text {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-btn {
  font-size: 28rpx;
  color: #fff;
  font-weight: bold;
}
</style>
