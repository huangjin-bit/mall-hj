<template>
  <div class="order-confirm-page">
    <div class="page-container">
      <h1 class="page-title">确认订单</h1>

      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>

      <div v-else class="confirm-content">
        <!-- 收货地址 -->
        <div class="section">
          <h2 class="section-title">收货地址</h2>
          <AddressSelect @select="handleAddressSelect" />
        </div>

        <!-- 商品清单 -->
        <div class="section">
          <h2 class="section-title">商品清单</h2>
          <el-table :data="orderItems" border>
            <el-table-column label="商品信息" width="400">
              <template #default="{ row }">
                <div class="item-info">
                  <img :src="row.skuImage || defaultImage" class="item-image" />
                  <div class="item-details">
                    <div class="item-name">{{ row.spuName }}</div>
                    <div class="item-sku">{{ row.skuName }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="单价" align="center" width="120">
              <template #default="{ row }">
                <span class="price">¥{{ row.price.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数量" align="center" width="100">
              <template #default="{ row }">
                <span>{{ row.quantity }}</span>
              </template>
            </el-table-column>
            <el-table-column label="小计" align="center" width="120">
              <template #default="{ row }">
                <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 优惠券 -->
        <div class="section">
          <h2 class="section-title">优惠券</h2>
          <el-select v-model="selectedCouponId" placeholder="请选择优惠券" clearable style="width: 300px">
            <el-option
              v-for="coupon in availableCoupons"
              :key="coupon.id"
              :label="`${coupon.couponName} - ¥${coupon.amount}`"
              :value="coupon.id"
            />
          </el-select>
        </div>

        <!-- 价格明细 -->
        <div class="section">
          <h2 class="section-title">价格明细</h2>
          <div class="price-detail">
            <div class="detail-row">
              <span>商品总额：</span>
              <span class="value">¥{{ totalAmount.toFixed(2) }}</span>
            </div>
            <div class="detail-row">
              <span>运费：</span>
              <span class="value">¥{{ freightAmount.toFixed(2) }}</span>
            </div>
            <div v-if="discountAmount > 0" class="detail-row">
              <span>优惠：</span>
              <span class="value discount">-¥{{ discountAmount.toFixed(2) }}</span>
            </div>
            <div class="detail-row total">
              <span>应付金额：</span>
              <span class="value pay-amount">¥{{ payAmount.toFixed(2) }}</span>
            </div>
          </div>
        </div>

        <!-- 提交按钮 -->
        <div class="submit-section">
          <el-button type="primary" size="large" @click="handleSubmitOrder" :disabled="!selectedAddress">
            提交订单
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import AddressSelect from '@/components/AddressSelect.vue'
import { getAvailableCoupons } from '@/api/seckill/coupon'
import { createOrder } from '@/api/order/order'
import { getSkuDetail } from '@/api/product/sku'
import type { MemberAddress } from '@/api/member/address'
import type { Coupon } from '@/api/seckill/coupon'
import type { Sku } from '@/api/product/sku'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const selectedAddress = ref<MemberAddress | null>(null)
const selectedCouponId = ref<number>()
const availableCoupons = ref<Coupon[]>([])
const orderItems = ref<Array<{
  skuId: number
  spuName: string
  skuName: string
  skuImage?: string
  price: number
  quantity: number
}>>([])

const defaultImage = 'https://via.placeholder.com/80x80?text=No+Image'

// 计算金额
const totalAmount = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const freightAmount = computed(() => {
  // 简化处理，固定运费 10 元
  return totalAmount.value > 99 ? 0 : 10
})

const discountAmount = computed(() => {
  if (!selectedCouponId.value) return 0

  const coupon = availableCoupons.value.find(c => c.id === selectedCouponId.value)
  if (!coupon) return 0

  // 简化处理，直接减去优惠券金额
  return coupon.amount
})

const payAmount = computed(() => {
  return Math.max(0, totalAmount.value + freightAmount.value - discountAmount.value)
})

// 处理地址选择
const handleAddressSelect = (address: MemberAddress) => {
  selectedAddress.value = address
}

// 加载订单商品
const loadOrderItems = async () => {
  try {
    // 从路由参数获取商品信息
    // 支持两种方式：1. skuId + quantity（直接购买）2. skuIds（购物车结算）
    const skuId = route.query.skuId as string
    const quantity = route.query.quantity as string
    const skuIds = route.query.skuIds as string

    if (skuId && quantity) {
      // 直接购买
      const res = await getSkuDetail(Number(skuId))
      const sku = res.data
      if (sku) {
        orderItems.value = [{
          skuId: sku.id,
          spuName: sku.skuName.split(' ')[0], // 简化处理
          skuName: sku.skuName,
          skuImage: sku.image,
          price: sku.price,
          quantity: Number(quantity)
        }]
      }
    } else if (skuIds) {
      // 从购物车结算
      const ids = skuIds.split(',').map(id => Number(id))
      // 这里应该从购物车 store 中获取商品信息
      // 简化处理，假设可以从 cartStore 获取
      // 实际项目中需要从 cartStore.checkedItems 获取
      const { useCartStore } = await import('@/stores/cart')
      const cartStore = useCartStore()
      const items = cartStore.checkedItems.filter(item => ids.includes(item.skuId))

      orderItems.value = items.map(item => ({
        skuId: item.skuId,
        spuName: item.spuName,
        skuName: item.skuName,
        skuImage: item.skuImage,
        price: item.price,
        quantity: item.quantity
      }))
    }
  } catch (error) {
    console.error('[OrderConfirm] Load order items failed', error)
  } finally {
    loading.value = false
  }
}

// 加载可用优惠券
const loadCoupons = async () => {
  try {
    const res = await getAvailableCoupons()
    availableCoupons.value = res.data || []
  } catch (error) {
    console.error('[OrderConfirm] Load coupons failed', error)
  }
}

// 提交订单
const handleSubmitOrder = async () => {
  if (!selectedAddress.value) {
    ElMessage.warning('请选择收货地址')
    return
  }

  if (orderItems.value.length === 0) {
    ElMessage.warning('请选择商品')
    return
  }

  try {
    const orderSn = await createOrder({
      receiverAddressId: selectedAddress.value.id,
      items: orderItems.value.map(item => ({
        skuId: item.skuId,
        quantity: item.quantity
      })),
      couponId: selectedCouponId.value
    })

    ElMessage.success('订单创建成功')
    router.push({
      name: 'OrderPay',
      params: { orderSn }
    })
  } catch (error) {
    console.error('[OrderConfirm] Create order failed', error)
  }
}

onMounted(() => {
  loadOrderItems()
  loadCoupons()
})
</script>

<style scoped lang="scss">
.order-confirm-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  margin: 0 0 30px 0;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

.loading-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 40px;
}

.confirm-content {
  .section {
    background-color: #fff;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
  }

  .section-title {
    margin: 0 0 20px 0;
    font-size: 18px;
    color: #333;
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

    .item-sku {
      font-size: 12px;
      color: #999;
    }
  }
}

.price,
.subtotal {
  color: #f56c6c;
  font-weight: 500;
}

.price-detail {
  .detail-row {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    font-size: 14px;
    color: #666;

    .value {
      font-weight: 500;
      color: #333;

      &.discount {
        color: #67c23a;
      }
    }

    &.total {
      border-top: 1px solid #f0f0f0;
      margin-top: 10px;
      padding-top: 20px;
      font-size: 16px;

      .pay-amount {
        font-size: 24px;
        color: #f56c6c;
      }
    }
  }
}

.submit-section {
  text-align: right;
  padding: 20px 0;
}
</style>
