<template>
  <div class="order-list-page">
    <div class="page-container">
      <h1 class="page-title">我的订单</h1>

      <!-- 状态标签 -->
      <el-tabs v-model="activeStatus" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name=""></el-tab-pane>
        <el-tab-pane label="待付款" name="0"></el-tab-pane>
        <el-tab-pane label="待发货" name="1"></el-tab-pane>
        <el-tab-pane label="待收货" name="2"></el-tab-pane>
        <el-tab-pane label="已完成" name="3"></el-tab-pane>
        <el-tab-pane label="已取消" name="4"></el-tab-pane>
      </el-tabs>

      <!-- 订单列表 -->
      <div v-if="orders.length > 0" class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-card" @click="handleViewDetail(order.id)">
          <div class="order-header">
            <span class="order-sn">订单号：{{ order.orderSn }}</span>
            <span class="order-time">{{ formatDate(order.createTime) }}</span>
          </div>
          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-item">
              <img :src="item.skuImage || defaultImage" class="item-image" />
              <div class="item-info">
                <div class="item-name">{{ item.spuName }}</div>
                <div class="item-specs">{{ item.skuName }}</div>
                <div class="item-price">¥{{ item.price.toFixed(2) }} × {{ item.quantity }}</div>
              </div>
            </div>
          </div>
          <div class="order-footer">
            <div class="order-status">
              <span :class="getStatusClass(order.status)">{{ getStatusText(order.status) }}</span>
            </div>
            <div class="order-actions">
              <el-button v-if="order.status === 0" type="primary" size="small" @click.stop="handlePay(order)">
                去支付
              </el-button>
              <el-button v-if="order.status === 0" size="small" @click.stop="handleCancel(order)">
                取消订单
              </el-button>
              <el-button v-if="order.status === 2" type="primary" size="small" @click.stop="handleConfirmReceive(order)">
                确认收货
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <EmptyState v-else message="暂无订单" />

      <!-- 分页 -->
      <Pagination
        v-if="total > 0"
        :total="total"
        :page="currentPage"
        :size="pageSize"
        @change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Pagination from '@/components/Pagination.vue'
import EmptyState from '@/components/EmptyState.vue'
import { getOrderList, cancelOrder, confirmReceive, type Order, OrderStatus } from '@/api/order/order'
import { useUserStore } from '@/stores/user'
import { formatDate } from '@/utils'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeStatus = ref<string>('')
const orders = ref<Order[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const defaultImage = 'https://via.placeholder.com/80x80?text=No+Image'

// 加载订单列表
const loadOrders = async () => {
  try {
    const res = await getOrderList({
      memberId: userStore.memberId,
      status: activeStatus.value ? Number(activeStatus.value) as OrderStatus : undefined,
      page: currentPage.value,
      size: pageSize.value
    })
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('[OrderList] Load orders failed', error)
  }
}

// 切换标签
const handleTabChange = () => {
  currentPage.value = 1
  loadOrders()
}

// 查看详情
const handleViewDetail = (orderId: number) => {
  router.push({
    name: 'OrderDetail',
    params: { id: orderId }
  })
}

// 去支付
const handlePay = (order: Order) => {
  router.push({
    name: 'OrderPay',
    params: { orderSn: order.orderSn }
  })
}

// 取消订单
const handleCancel = async (order: Order) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      type: 'warning'
    })
    await cancelOrder(order.id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    // 用户取消操作
  }
}

// 确认收货
const handleConfirmReceive = async (order: Order) => {
  try {
    await ElMessageBox.confirm('确认已收到商品？', '提示', {
      type: 'warning'
    })
    await confirmReceive(order.id)
    ElMessage.success('确认收货成功')
    loadOrders()
  } catch (error) {
    // 用户取消操作
  }
}

// 分页变化
const handlePageChange = (page: number, size: number) => {
  currentPage.value = page
  pageSize.value = size
  loadOrders()
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

// 获取状态样式类
const getStatusClass = (status: OrderStatus) => {
  const classMap: Record<OrderStatus, string> = {
    [OrderStatus.PENDING_PAYMENT]: 'status-warning',
    [OrderStatus.PENDING_SHIPMENT]: 'status-info',
    [OrderStatus.SHIPPED]: 'status-primary',
    [OrderStatus.COMPLETED]: 'status-success',
    [OrderStatus.CANCELLED]: 'status-default'
  }
  return classMap[status] || ''
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped lang="scss">
.order-list-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 20px;
}

.order-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .order-header {
    display: flex;
    justify-content: space-between;
    padding-bottom: 15px;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 15px;
    font-size: 14px;
    color: #666;

    .order-sn {
      color: #333;
      font-weight: 500;
    }
  }

  .order-items {
    display: flex;
    flex-direction: column;
    gap: 15px;
    margin-bottom: 15px;
  }

  .order-item {
    display: flex;
    gap: 15px;

    .item-image {
      width: 80px;
      height: 80px;
      object-fit: cover;
      border-radius: 4px;
    }

    .item-info {
      flex: 1;

      .item-name {
        font-size: 14px;
        color: #333;
        margin-bottom: 5px;
      }

      .item-specs {
        font-size: 12px;
        color: #999;
        margin-bottom: 5px;
      }

      .item-price {
        font-size: 14px;
        color: #f56c6c;
      }
    }
  }

  .order-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 15px;
    border-top: 1px solid #f0f0f0;

    .order-status {
      font-size: 16px;
      font-weight: 500;

      .status-warning { color: #e6a23c; }
      .status-info { color: #909399; }
      .status-primary { color: #409eff; }
      .status-success { color: #67c23a; }
      .status-default { color: #c0c4cc; }
    }

    .order-actions {
      display: flex;
      gap: 10px;
    }
  }
}
</style>
