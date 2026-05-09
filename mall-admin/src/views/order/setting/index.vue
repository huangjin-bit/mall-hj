<template>
  <div class="order-setting-container">
    <el-card>
      <template #header>
        <span>订单设置</span>
      </template>

      <el-form ref="formRef" :model="form" label-width="150px" style="max-width: 600px">
        <el-form-item label="订单超时时间(分钟)">
          <el-input-number v-model="form.orderOverdueMinutes" :min="1" />
          <span class="ml-10">订单未支付超时自动取消时间</span>
        </el-form-item>
        <el-form-item label="确认收货超时(天)">
          <el-input-number v-model="form.confirmOverdueDays" :min="1" />
          <span class="ml-10">发货后超时未确认收货自动完成时间</span>
        </el-form-item>
        <el-form-item label="订单完成超时(天)">
          <el-input-number v-model="form.finishOverdueDays" :min="1" />
          <span class="ml-10">订单完成后可评价超时时间</span>
        </el-form-item>
        <el-form-item label="评价超时(天)">
          <el-input-number v-model="form.commentOverdueDays" :min="1" />
          <span class="ml-10">订单完成后可评价时间</span>
        </el-form-item>
        <el-form-item label="退货超时(天)">
          <el-input-number v-model="form.returnOverdueDays" :min="1" />
          <span class="ml-10">订单完成后可申请退货时间</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderSetting, updateOrderSetting } from '@/api/order/orderSetting'

const form = reactive({
  orderOverdueMinutes: 30,
  confirmOverdueDays: 15,
  finishOverdueDays: 7,
  commentOverdueDays: 7,
  returnOverdueDays: 7
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const data = await getOrderSetting()
    if (data) {
      Object.assign(form, data)
    }
  } catch (error) {
    console.error('[OrderSetting] Failed to load data:', error)
  }
}

const handleSubmit = async () => {
  try {
    await updateOrderSetting(form)
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('[OrderSetting] Failed to save:', error)
  }
}
</script>

<style scoped lang="scss">
.order-setting-container {
  :deep(.el-input-number) {
    width: 200px;
  }
}
</style>
