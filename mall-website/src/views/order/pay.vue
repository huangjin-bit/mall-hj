<template>
  <div class="order-pay-page">
    <div class="page-container">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>

      <div v-else class="pay-content">
        <!-- 订单信息 -->
        <div class="section order-info">
          <h2 class="section-title">订单信息</h2>
          <div class="info-item">
            <span class="label">订单编号：</span>
            <span class="value">{{ order?.orderSn }}</span>
          </div>
          <div class="info-item">
            <span class="label">应付金额：</span>
            <span class="amount">¥{{ order?.payAmount.toFixed(2) }}</span>
          </div>
        </div>

        <!-- 支付方式 -->
        <div class="section payment-method">
          <h2 class="section-title">选择支付方式</h2>
          <el-radio-group v-model="paymentMethod">
            <el-radio :label="1" border>
              <div class="payment-option">
                <img src="/alipay.png" alt="支付宝" class="payment-icon" />
                <span>支付宝</span>
              </div>
            </el-radio>
            <el-radio :label="2" border>
              <div class="payment-option">
                <img src="/wechat.png" alt="微信支付" class="payment-icon" />
                <span>微信支付</span>
              </div>
            </el-radio>
          </el-radio-group>
        </div>

        <!-- 支付按钮 -->
        <div class="pay-action">
          <el-button type="primary" size="large" @click="handlePay" :loading="paying">
            立即支付
          </el-button>
        </div>

        <!-- 支付成功对话框 -->
        <el-dialog v-model="showSuccessDialog" title="支付成功" width="400px" :show-close="false">
          <div class="success-content">
            <el-icon :size="60" color="#67c23a"><SuccessFilled /></el-icon>
            <p>支付成功！</p>
          </div>
          <template #footer>
            <el-button @click="handleViewOrder">查看订单</el-button>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { SuccessFilled } from '@element-plus/icons-vue'
import { getOrderDetail } from '@/api/order/order'
import type { Order } from '@/api/order/order'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const paying = ref(false)
const paymentMethod = ref(1)
const showSuccessDialog = ref(false)
const order = ref<Order | null>(null)

// 加载订单详情
const loadOrderDetail = async () => {
  loading.value = true
  try {
    const orderSn = route.params.orderSn as string
    // 这里需要根据 orderSn 查询订单
    // 简化处理，实际应该调用根据 orderSn 查询的接口
    // const res = await getOrderBySn(orderSn)
    // order.value = res.data

    // 临时模拟数据
    order.value = {
      id: 1,
      orderSn: orderSn,
      memberId: 0,
      totalAmount: 299.00,
      freightAmount: 0,
      discountAmount: 0,
      payAmount: 299.00,
      status: 0,
      receiverName: '张三',
      receiverPhone: '13800138000',
      receiverAddress: '北京市朝阳区xxx'
    }
  } catch (error) {
    console.error('[OrderPay] Load order failed', error)
  } finally {
    loading.value = false
  }
}

// 支付
const handlePay = async () => {
  if (!order.value) return

  paying.value = true

  // 模拟支付过程
  setTimeout(() => {
    paying.value = false
    showSuccessDialog.value = true
  }, 2000)
}

// 查看订单
const handleViewOrder = () => {
  router.push({
    name: 'OrderDetail',
    params: { id: order.value?.id }
  })
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped lang="scss">
.order-pay-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.loading-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 40px;
}

.pay-content {
  .section {
    background-color: #fff;
    border-radius: 8px;
    padding: 30px;
    margin-bottom: 20px;
  }

  .section-title {
    margin: 0 0 20px 0;
    font-size: 18px;
    color: #333;
    font-weight: 500;
  }
}

.order-info {
  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    font-size: 14px;

    .label {
      color: #666;
      min-width: 100px;
    }

    .value {
      color: #333;
    }

    .amount {
      font-size: 24px;
      color: #f56c6c;
      font-weight: 500;
    }
  }
}

.payment-method {
  :deep(.el-radio-group) {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }

  :deep(.el-radio) {
    margin-right: 0;
    padding: 15px 20px;
  }

  .payment-option {
    display: flex;
    align-items: center;
    gap: 10px;

    .payment-icon {
      width: 30px;
      height: 30px;
    }
  }
}

.pay-action {
  text-align: center;
  padding: 20px 0;
}

.success-content {
  text-align: center;
  padding: 20px;

  p {
    margin: 20px 0 0 0;
    font-size: 18px;
    color: #333;
  }
}
</style>
