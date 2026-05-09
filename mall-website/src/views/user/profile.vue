<template>
  <div class="user-profile-page">
    <div class="page-container">
      <div class="profile-content">
        <h1 class="page-title">个人资料</h1>

        <div class="profile-form">
          <el-form :model="form" label-width="100px">
            <el-form-item label="头像">
              <div class="avatar-upload">
                <el-avatar :size="80" :src="form.avatar || ''">
                  {{ userStore.username?.charAt(0) }}
                </el-avatar>
                <el-button size="small" @click="handleUploadAvatar">
                  更换头像
                </el-button>
              </div>
            </el-form-item>

            <el-form-item label="用户名">
              <el-input v-model="form.username" disabled />
            </el-form-item>

            <el-form-item label="昵称">
              <el-input v-model="form.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :label="0">保密</el-radio>
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="生日">
              <el-date-picker
                v-model="form.birthday"
                type="date"
                placeholder="请选择生日"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="loading">
                保存
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { updateMember } from '@/api/member/member'
import { uploadFile } from '@/api/upload'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

const loading = ref(false)

const form = reactive({
  id: userStore.memberId,
  username: userStore.username,
  nickname: '',
  phone: '',
  gender: 0,
  birthday: '',
  avatar: ''
})

// 加载用户信息
const loadUserInfo = async () => {
  await userStore.fetchMemberInfo()

  if (userStore.memberInfo) {
    form.nickname = userStore.memberInfo.nickname || ''
    form.phone = userStore.memberInfo.phone || ''
    form.gender = userStore.memberInfo.gender || 0
    form.birthday = userStore.memberInfo.birthday || ''
    form.avatar = userStore.memberInfo.avatar || ''
  }
}

// 上传头像
const handleUploadAvatar = async () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'

  input.onchange = async (e: Event) => {
    const target = e.target as HTMLInputElement
    const file = target.files?.[0]

    if (!file) return

    try {
      const url = await uploadFile(file)
      form.avatar = url
      ElMessage.success('上传成功')
    } catch (error) {
      console.error('[UserProfile] Upload avatar failed', error)
    }
  }

  input.click()
}

// 提交表单
const handleSubmit = async () => {
  try {
    loading.value = true
    await updateMember(form)
    await userStore.fetchMemberInfo()
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('[UserProfile] Update profile failed', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped lang="scss">
.user-profile-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 700px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  margin: 0 0 30px 0;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

.profile-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 40px;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 20px;

  .el-button {
    flex-shrink: 0;
  }
}
</style>
