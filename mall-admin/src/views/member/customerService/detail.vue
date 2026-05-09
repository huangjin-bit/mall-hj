<template>
  <div class="customer-service-detail-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>工单详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-descriptions :column="2" border class="mb-20">
        <el-descriptions-item label="工单ID">{{ serviceData.id }}</el-descriptions-item>
        <el-descriptions-item label="会员">{{ serviceData.username }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          {{ serviceData.type === 1 ? '咨询' : serviceData.type === 2 ? '投诉' : '建议' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <StatusTag :status="serviceData.status" type="status" />
        </el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ serviceData.title }}</el-descriptions-item>
        <el-descriptions-item label="内容" :span="2">{{ serviceData.content }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2">{{ serviceData.handleResult || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <h3>消息记录</h3>
      <div class="message-list">
        <div
          v-for="msg in messages"
          :key="msg.id"
          :class="['message-item', msg.sender === 'admin' ? 'message-admin' : 'message-member']"
        >
          <div class="message-sender">{{ msg.sender === 'admin' ? '管理员' : serviceData.username }}</div>
          <div class="message-content">{{ msg.content }}</div>
          <div class="message-time">{{ msg.createTime }}</div>
        </div>
      </div>

      <div class="message-input mt-20">
        <el-input
          v-model="newMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入消息"
        />
        <el-button type="primary" @click="handleSendMessage" class="mt-10" v-hasPermi="['member:customer-service:handle']">发送</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCustomerServiceById, getServiceMessages, sendServiceMessage } from '@/api/member/customerService'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const serviceData = ref<any>({})
const messages = ref<any[]>([])
const newMessage = ref('')

onMounted(async () => {
  try {
    const serviceId = Number(route.params.id)
    serviceData.value = await getCustomerServiceById(serviceId)
    messages.value = await getServiceMessages(serviceId)
  } catch (error) {
    console.error('[CustomerService Detail] Failed to load data:', error)
  }
})

const handleSendMessage = async () => {
  if (!newMessage.value.trim()) {
    ElMessage.warning('请输入消息内容')
    return
  }

  try {
    await sendServiceMessage(serviceData.value.id, newMessage.value)
    ElMessage.success('发送成功')
    newMessage.value = ''
    messages.value = await getServiceMessages(serviceData.value.id)
  } catch (error) {
    console.error('[CustomerService Detail] Failed to send message:', error)
  }
}
</script>

<style scoped lang="scss">
.customer-service-detail-container {
  .message-list {
    max-height: 400px;
    overflow-y: auto;

    .message-item {
      margin-bottom: 15px;
      padding: 10px;
      border-radius: 4px;
      background-color: #f5f7fa;

      &.message-admin {
        background-color: #e1f3f8;
        margin-left: 50px;
      }

      &.message-member {
        background-color: #f5f7fa;
        margin-right: 50px;
      }

      .message-sender {
        font-weight: bold;
        margin-bottom: 5px;
      }

      .message-content {
        margin-bottom: 5px;
      }

      .message-time {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>
