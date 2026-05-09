<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-box">
        <h1 class="login-title">用户登录</h1>

        <el-tabs v-model="loginType" stretch>
          <!-- 账号密码登录 -->
          <el-tab-pane label="账号登录" name="username">
            <el-form
              ref="usernameFormRef"
              :model="usernameForm"
              :rules="usernameRules"
              @submit.prevent="handleUsernameLogin"
            >
              <el-form-item prop="username">
                <el-input
                  v-model="usernameForm.username"
                  placeholder="请输入用户名"
                  size="large"
                  :prefix-icon="User"
                />
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="usernameForm.password"
                  type="password"
                  placeholder="请输入密码"
                  size="large"
                  :prefix-icon="Lock"
                  show-password
                  @keyup.enter="handleUsernameLogin"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size="large"
                  :loading="loading"
                  @click="handleUsernameLogin"
                  style="width: 100%"
                >
                  登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <!-- 手机号登录 -->
          <el-tab-pane label="手机登录" name="phone">
            <el-form
              ref="phoneFormRef"
              :model="phoneForm"
              :rules="phoneRules"
              @submit.prevent="handlePhoneLogin"
            >
              <el-form-item prop="phone">
                <el-input
                  v-model="phoneForm.phone"
                  placeholder="请输入手机号"
                  size="large"
                  :prefix-icon="Iphone"
                />
              </el-form-item>
              <el-form-item prop="code">
                <div style="display: flex; gap: 10px; width: 100%">
                  <el-input
                    v-model="phoneForm.code"
                    placeholder="请输入验证码"
                    size="large"
                    :prefix-icon="Key"
                  />
                  <el-button
                    :disabled="countdown > 0"
                    @click="handleSendCode"
                    size="large"
                    style="min-width: 120px"
                  >
                    {{ countdown > 0 ? `${countdown}s` : '发送验证码' }}
                  </el-button>
                </div>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size="large"
                  :loading="loading"
                  @click="handlePhoneLogin"
                  style="width: 100%"
                >
                  登录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>

        <div class="login-footer">
          <span>还没有账号？</span>
          <router-link to="/register" class="register-link">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Lock, Iphone, Key } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { sendSmsCode } from '@/api/auth'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginType = ref<'username' | 'phone'>('username')
const loading = ref(false)
const countdown = ref(0)

const usernameFormRef = ref<FormInstance>()
const phoneFormRef = ref<FormInstance>()

const usernameForm = reactive({
  username: '',
  password: ''
})

const phoneForm = reactive({
  phone: '',
  code: ''
})

const usernameRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const phoneRules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

// 账号密码登录
const handleUsernameLogin = async () => {
  if (!usernameFormRef.value) return

  try {
    await usernameFormRef.value.validate()
    loading.value = true

    const success = await userStore.loginByUsername(usernameForm)

    if (success) {
      ElMessage.success('登录成功')
      const redirect = route.query.redirect as string || '/'
      router.push(redirect)
    }
  } catch (error) {
    console.error('[Login] Username login failed', error)
  } finally {
    loading.value = false
  }
}

// 手机号登录
const handlePhoneLogin = async () => {
  if (!phoneFormRef.value) return

  try {
    await phoneFormRef.value.validate()
    loading.value = true

    const success = await userStore.loginByPhone(phoneForm)

    if (success) {
      ElMessage.success('登录成功')
      const redirect = route.query.redirect as string || '/'
      router.push(redirect)
    }
  } catch (error) {
    console.error('[Login] Phone login failed', error)
  } finally {
    loading.value = false
  }
}

// 发送验证码
const handleSendCode = async () => {
  if (!phoneForm.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }

  if (!/^1[3-9]\d{9}$/.test(phoneForm.phone)) {
    ElMessage.warning('手机号格式不正确')
    return
  }

  try {
    await sendSmsCode(phoneForm.phone)
    ElMessage.success('验证码已发送')

    // 开始倒计时
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value === 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    console.error('[Login] Send code failed', error)
  }
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  width: 100%;
  max-width: 450px;
  padding: 20px;
}

.login-box {
  background-color: #fff;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.login-title {
  margin: 0 0 30px 0;
  text-align: center;
  font-size: 28px;
  color: #333;
  font-weight: 500;
}

.login-footer {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;

  .register-link {
    color: #409eff;
    margin-left: 5px;

    &:hover {
      text-decoration: underline;
    }
  }
}

:deep(.el-tabs__header) {
  margin-bottom: 30px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>
