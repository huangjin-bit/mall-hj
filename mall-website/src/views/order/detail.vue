<template>
  <div class="order-detail-page">
    <div class="page-container">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>

      <div v-else-if="order" class="detail-content">
        <!-- 订单状态 -->
        <div class="section status-section">
          <div class="status-icon">
            <el-icon :size="50" :color="getStatusColor(order.status)">
              <component :is="getStatusIcon(order.status)" />
            </el-icon>
          </div>
          <div class="status-info">
            <h2 class="status-title">{{ getStatusText(order.status) }}</h2>
            <p v-if="order.status === OrderStatus.PENDING_PAYMENT" class="status-tip">
              请在 24 小时内完成支付，超时订单将自动取消
            </p>
          </div>
        </div>

        <!-- 订单信息 -->
        <div class="section">
          <h2 class="section-title">订单信息</h2>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">订单编号：</span>
              <span class="value">{{ order.orderSn }}</span>
            </div>
            <div class="info-item">
              <span class="label">下单时间：</span>
              <span class="value">{{ formatDate(order.createTime) }}</span>
            </div>
          </div>
        </div>

        <!-- 收货信息 -->
        <div class="section">
          <h2 class="section-title">收货信息</h2>
          <div class="address-info">
            <p class="receiver">{{ order.receiverName }} {{ order.receiverPhone }}</p>
            <p class="address">{{ order.receiverAddress }}</p>
          </div>
        </div>

        <!-- 商品信息 -->
        <div class="section">
          <h2 class="section-title">商品信息</h2>
          <el-table :data="order.items" border>
            <el-table-column label="商品信息" width="400">
              <template #default="{ row }">
                <div class="item-info">
                  <img :src="row.skuImage || defaultImage" class="item-image" />
                  <div class="item-details">
                    <div class="item-name">{{ row.spuName }}</div>
                    <div class="item-specs">{{ row.skuName }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="单价" align="center" width="120">
              <template #default="{ row }">
                <span>¥{{ row.price.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数量" align="center" width="100">
              <template #default="{ row }">
                <span>{{ row.quantity }}</span>
              </template>
            </el-table-column>
            <el-table-column label="小计" align="center" width="120">
              <template #default="{ row }">
                <span>¥{{ (row.price * row.quantity).toFixed(2) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 费用明细 -->
        <div class="section">
          <h2 class="section-title">费用明细</h2>
          <div class="cost-detail">
            <div class="cost-row">
              <span>商品总额：</span>
              <span>¥{{ order.totalAmount.toFixed(2) }}</span>
            </div>
            <div class="cost-row">
              <span>运费：</span>
              <span>¥{{ order.freightAmount.toFixed(2) }}</span>
            </div>
            <div v-if="order.discountAmount > 0" class="cost-row">
              <span>优惠：</span>
              <span class="discount">-¥{{ order.discountAmount.toFixed(2) }}</span>
            </div>
            <div class="cost-row total">
              <span>实付金额：</span>
              <span class="pay-amount">¥{{ order.payAmount.toFixed(2) }}</span>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-section">
          <el-button @click="router.back()">返回</el-button>
          <el-button v-if="order.status === OrderStatus.PENDING_PAYMENT" type="primary" @click="handlePay">
            去支付
          </el-button>
          <el-button v-if="order.status === OrderStatus.SHIPPED" type="primary" @click="handleConfirmReceive">
            确认收货
          </el-button>
        </div>
      </div>

      <EmptyState v-else message="订单不存在" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  getOrderDetail,
  cancelOrder,
  confirmReceive,
  type Order,
  OrderStatus
} from '@/api/order/order'
import {
  Clock,
  SuccessFilled,
  WarningFilled,
  CircleCheckFilled,
  CircleCloseFilled
} from '@element-plus/icons-vue'
import EmptyState from '@/components/EmptyState.vue'
import { formatDate } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const order = ref<Order | null>(null)

const defaultImage = 'https://via.placeholder.com/80x80?text=No+Image'

// 加载订单详情
const loadOrderDetail = async () => {
  loading.value = true
  try {
    const orderId = Number(route.params.id)
    const res = await getOrderDetail(orderId)
    order.value = res.data || null
  } catch (error) {
    console.error('[OrderDetail] Load order failed', error)
  } finally {
    loading.value = false
  }
}

// 获取状态文本
const getStatusText = (status: OrderStatus) => {
  const statusMap: Record<OrderStatus, string> = {
    [OrderStatus.PENDING_PAYMENT]: '待付款',
    [OrderStatus.PENDING_SHIPMENT]: '待发货',
    [OrderStatus.SHIPPED]: '待收货',
    [OrderStatus.COMPLETED]: '已完成',
    [OrderStatus.CANCELLED]: '已取消'
  }
  return statusMap[status] || '未知状态'
}

// 获取状态图标
const getStatusIcon = (status: OrderStatus) => {
  const iconMap: Record<OrderStatus, any> = {
    [OrderStatus.PENDING_PAYMENT]: Clock,
    [OrderStatus.PENDING_SHIPMENT]: WarningFilled,
    [OrderStatus.SHIPPED]: SuccessFilled,
    [OrderStatus.COMPLETED]: CircleCheckFilled,
    [OrderStatus.CANCELLED]: CircleCloseFilled
  }
  return iconMap[status] || Clock
}

// 获取状态颜色
const getStatusColor = (status: OrderStatus) => {
  const colorMap: Record<OrderStatus, string> = {
    [OrderStatus.PENDING_PAYMENT]: '#e6a23c',
    [OrderStatus.PENDING_SHIPMENT]: '#909399',
    [OrderStatus.SHIPPED]: '#409eff',
    [OrderStatus.COMPLETED]: '#67c23a',
    [OrderStatus.CANCELLED]: '#c0c4cc'
  }
  return colorMap[status] || '#909399'
}

// 去支付
const handlePay = () => {
  if (!order.value) return
  router.push({
    name: 'OrderPay',
    params: { orderSn: order.value.orderSn }
  })
}

// 确认收货
const handleConfirmReceive = async () => {
  if (!order.value) return

  try {
    await ElMessageBox.confirm('确认已收到商品？', '提示', {
      type: 'warning'
    })
    await confirmReceive(order.value.id)
    ElMessage.success('确认收货成功')
    loadOrderDetail()
  } catch (error) {
    // 用户取消操作
  }
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<style scoped lang="scss">
.order-detail-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.loading-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 40px;
}

.detail-content {
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

.status-section {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 40px;

  .status-icon {
    flex-shrink: 0;
  }

  .status-info {
    .status-title {
      margin: 0 0 10px 0;
      font-size: 24px;
      color: #333;
      font-weight: 500;
    }

    .status-tip {
      margin: 0;
      font-size: 14px;
      color: #999;
    }
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;

  .info-item {
    font-size: 14px;

    .label {
      color: #666;
    }

    .value {
      color: #333;
    }
  }
}

.address-info {
  p {
    margin: 0 0 10px 0;
    font-size: 14px;
    color: #333;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .receiver {
    font-weight: 500;
  }
}

.item-info {
  display: flex;
  gap: 15px;
  padding: 10px 0;

  .item-image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 4px;
  }

  .item-details {
    flex: 1;

    .item-name {
      font-size: 14px;
      color: #333;
      margin-bottom: 5px;
    }

    .item-specs {
      font-size: 12px;
      color: #999;
    }
  }
}

.cost-detail {
  .cost-row {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    font-size: 14px;
    color: #666;

    &.total {
      border-top: 1px solid #f0f0f0;
      margin-top: 10px;
      padding-top: 20px;
      font-size: 16px;
      font-weight: 500;
      color: #333;
    }

    .pay-amount {
      color: #f56c6c;
      font-size: 24px;
    }

    .discount {
      color: #67c23a;
    }
  }
}

.action-section {
  text-align: center;
  padding: 20px 0;

  .el-button {
    margin: 0 10px;
  }
}
</style>
