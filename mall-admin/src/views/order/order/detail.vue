<template>
  <div class="order-detail-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>订单详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ orderData.orderSn }}</el-descriptions-item>
            <el-descriptions-item label="会员">{{ orderData.username }}</el-descriptions-item>
            <el-descriptions-item label="订单总额">¥{{ orderData.totalAmount }}</el-descriptions-item>
            <el-descriptions-item label="应付金额">¥{{ orderData.payAmount }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <StatusTag :status="orderData.status" type="order" />
            </el-descriptions-item>
            <el-descriptions-item label="配送方式">
              {{ orderData.deliveryType === 1 ? '快递配送' : '门店自提' }}
            </el-descriptions-item>
            <el-descriptions-item label="收货人" :span="2">
              {{ orderData.receiverName }} {{ orderData.receiverPhone }}
            </el-descriptions-item>
            <el-descriptions-item label="收货地址" :span="2">
              {{ orderData.receiverAddress }}
            </el-descriptions-item>
            <el-descriptions-item label="订单备注" :span="2">
              {{ orderData.note || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ orderData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="支付时间">{{ orderData.payTime || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <el-tab-pane label="商品信息" name="items">
          <el-table :data="orderItems" border>
            <el-table-column prop="skuName" label="商品名称" />
            <el-table-column prop="skuImage" label="商品图片" width="100">
              <template #default="{ row }">
                <el-image :src="row.skuImage" style="width: 60px; height: 60px" fit="cover" />
              </template>
            </el-table-column>
            <el-table-column prop="skuPrice" label="单价" width="80" />
            <el-table-column prop="skuQuantity" label="数量" width="80" />
            <el-table-column prop="totalPrice" label="小计" width="100" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="支付信息" name="payment">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="支付方式">
              {{ paymentData.payType === 1 ? '支付宝' : paymentData.payType === 2 ? '微信' : '银行卡' }}
            </el-descriptions-item>
            <el-descriptions-item label="支付金额">¥{{ paymentData.payAmount }}</el-descriptions-item>
            <el-descriptions-item label="支付状态">
              <StatusTag :status="paymentData.status" type="status" />
            </el-descriptions-item>
            <el-descriptions-item label="支付时间">{{ paymentData.payTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="交易单号" :span="2">
              {{ paymentData.transactionId || '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderById } from '@/api/order/order'
import { getItemsByOrderId } from '@/api/order/orderItem'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const activeTab = ref('basic')
const orderData = ref<any>({})
const orderItems = ref<any[]>([])
const paymentData = ref<any>({})

onMounted(async () => {
  try {
    const orderId = Number(route.params.id)
    orderData.value = await getOrderById(orderId)
    orderItems.value = await getItemsByOrderId(orderId)
    // TODO: 加载支付信息
  } catch (error) {
    console.error('[Order Detail] Failed to load data:', error)
  }
})
</script>

<style scoped lang="scss">
.order-detail-container {
  :deep(.el-tabs__content) {
    padding-top: 20px;
  }
}
</style>
