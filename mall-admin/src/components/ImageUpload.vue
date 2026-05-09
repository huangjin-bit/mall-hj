<template>
  <el-upload
    :action="uploadUrl"
    :headers="uploadHeaders"
    :file-list="fileList"
    :limit="limit"
    :on-success="handleSuccess"
    :on-remove="handleRemove"
    :before-upload="beforeUpload"
    list-type="picture-card"
    accept="image/*"
  >
    <el-icon><Plus /></el-icon>
  </el-upload>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

interface Props {
  modelValue: string | string[]
  limit?: number
}

interface Emits {
  (e: 'update:modelValue', value: string | string[]): void
}

const props = withDefaults(defineProps<Props>(), {
  limit: 1
})

const emit = defineEmits<Emits>()
const userStore = useUserStore()

const uploadUrl = '/api/thirdparty/oss/upload'
const uploadHeaders = {
  Authorization: `Bearer ${userStore.token}`
}

const fileList = ref<any[]>([])

// 初始化文件列表
watch(
  () => props.modelValue,
  (val) => {
    if (!val) {
      fileList.value = []
      return
    }

    if (Array.isArray(val)) {
      fileList.value = val.map((url, index) => ({
        name: `image-${index}`,
        url
      }))
    } else {
      fileList.value = [{ name: 'image', url: val }]
    }
  },
  { immediate: true }
)

const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return true
}

const handleSuccess = (response: any) => {
  if (response.code === 200) {
    const url = response.data.url

    if (props.limit === 1) {
      emit('update:modelValue', url)
    } else {
      const urls = fileList.value.map(item => item.url)
      urls.push(url)
      emit('update:modelValue', urls)
    }
  }
}

const handleRemove = (file: any) => {
  const index = fileList.value.findIndex(item => item.url === file.url)
  if (index > -1) {
    fileList.value.splice(index, 1)
    const urls = fileList.value.map(item => item.url)

    if (props.limit === 1) {
      emit('update:modelValue', '')
    } else {
      emit('update:modelValue', urls)
    }
  }
}
</script>

<style scoped>
:deep(.el-upload-list__item) {
  transition: none;
}
</style>
