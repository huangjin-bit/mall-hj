<script setup>
import { ref, computed, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useUserStore } from '@/store/user'
import { getAddressList } from '@/api/address'
import { createOrder } from '@/api/order'
import { getAvailableCoupons } from '@/api/seckill'

const userStore = useUserStore()

// 订单商品
const orderItems = ref([])
const totalAmount = ref(0)
const totalPay = ref(0)

// 收货地址
const addresses = ref([])
const selectedAddress = ref(null)

// 优惠券
const availableCoupons = ref([])
const selectedCoupon = ref(null)

// 备注
const remark = ref('')

// 加载状态
const loading = ref(false)

onLoad(async (options) => {
  if (!userStore.checkAuth()) return

  // 解析订单商品数据
  if (options.data) {
    try {
      const data = JSON.parse(decodeURIComponent(options.data))
      orderItems.value = data.items || []
      totalAmount.value = data.totalAmount || 0
      totalPay.value = data.totalPay || 0
    } catch (error) {
      console.error('[ParseOrderData Error]', error)
    }
  }

  // 单商品购买
  if (options.skuId && options.count) {
    const skuId = parseInt(options.skuId)
    const count = parseInt(options.count)
    // 这里应该根据skuId获取商品详情
    // 简化处理，实际应调用API
  }

  await loadData()
})

onMounted(() => {
  // 默认选择第一个地址
  if (addresses.value.length > 0) {
    const defaultAddr = addresses.value.find(addr => addr.isDefault)
    selectedAddress.value = defaultAddr || addresses.value[0]
  }
})

async function loadData() {
  await Promise.all([
    loadAddresses(),
    loadCoupons()
  ])
}

async function loadAddresses() {
  try {
    const res = await getAddressList()
    addresses.value = res || []

    // 默认选择地址
    const defaultAddr = addresses.value.find(addr => addr.isDefault)
    if (defaultAddr) {
      selectedAddress.value = defaultAddr
    } else if (addresses.value.length > 0) {
      selectedAddress.value = addresses.value[0]
    }
  } catch (error) {
    console.error('[LoadAddresses Error]', error)
  }
}

async function loadCoupons() {
  try {
    const res = await getAvailableCoupons({
      spuId: orderItems.value[0]?.spuId,
      price: totalAmount.value
    })
    availableCoupons.value = res || []
  } catch (error) {
    console.error('[LoadCoupons Error]', error)
  }
}

function selectAddress() {
  uni.navigateTo({
    url: '/pages/user/address?select=1'
  })
}

function selectCoupon(coupon) {
  selectedCoupon.value = coupon
  // 重新计算价格
  calculatePrice()
}

function calculatePrice() {
  let discount = 0
  if (selectedCoupon.value) {
    discount = selectedCoupon.value.discountAmount || 0
  }
  totalPay.value = totalAmount.value - discount
}

async function submitOrder() {
  if (!selectedAddress.value) {
    uni.showToast({
      title: '请选择收货地址',
      icon: 'none'
    })
    return
  }

  try {
    loading.value = true

    const orderData = {
      addressId: selectedAddress.value.id,
      items: orderItems.value,
      couponId: selectedCoupon.value?.id,
      remark: remark.value
    }

    const res = await createOrder(orderData)

    uni.showToast({
      title: '订单创建成功',
      icon: 'success'
    })

    // 跳转到支付页面
    setTimeout(() => {
      uni.redirectTo({
        url: `/pages/order/pay?orderSn=${res.orderSn}`
      })
    }, 1500)
  } catch (error) {
    console.error('[SubmitOrder Error]', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <view class="order-confirm-page">
    <!-- 收货地址 -->
    <view class="address-section" @tap="selectAddress">
      <view v-if="selectedAddress" class="address-card">
        <view class="address-header">
          <text class="receiver-name">{{ selectedAddress.receiverName }}</text>
          <text class="receiver-phone">{{ selectedAddress.receiverPhone }}</text>
        </view>
        <view class="address-detail">
          {{ selectedAddress.province }}{{ selectedAddress.city }}{{ selectedAddress.district }}{{ selectedAddress.detailAddress }}
        </view>
      </view>
      <view v-else class="address-empty">
        <text class="empty-text">请选择收货地址</text>
      </view>
      <text class="arrow">›</text>
    </view>

    <!-- 商品列表 -->
    <view class="goods-section">
      <view class="section-title">订单商品</view>
      <view
        v-for="item in orderItems"
        :key="item.skuId"
        class="goods-item"
      >
        <image :src="item.image" class="goods-image" mode="aspectFill" />
        <view class="goods-info">
          <text class="goods-name">{{ item.spuName }}</text>
          <text class="goods-attrs">{{ item.saleAttrs }}</text>
          <view class="goods-bottom">
            <text class="goods-price">¥{{ item.salePrice }}</text>
            <text class="goods-count">x{{ item.count }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 优惠券 -->
    <view class="coupon-section" @tap="showCouponPopup = true">
      <view class="coupon-left">
        <text class="coupon-label">优惠券</text>
        <text class="coupon-value">{{ selectedCoupon ? `-${selectedCoupon.discountAmount}元` : '暂无可用' }}</text>
      </view>
      <text class="arrow">›</text>
    </view>

    <!-- 备注 -->
    <view class="remark-section">
      <view class="remark-label">订单备注</view>
      <textarea
        v-model="remark"
        class="remark-input"
        placeholder="选填，对本次订单的说明"
        maxlength="200"
      />
    </view>

    <!-- 价格明细 -->
    <view class="price-section">
      <view class="price-item">
        <text class="price-label">商品金额</text>
        <text class="price-value">¥{{ totalAmount.toFixed(2) }}</text>
      </view>
      <view class="price-item" v-if="selectedCoupon">
        <text class="price-label">优惠券</text>
        <text class="price-value discount">-¥{{ selectedCoupon.discountAmount }}</text>
      </view>
      <view class="price-item total">
        <text class="price-label">实付款</text>
        <text class="price-value">¥{{ totalPay.toFixed(2) }}</text>
      </view>
    </view>

    <!-- 底部提交栏 -->
    <view class="submit-bar">
      <view class="price-info">
        <text class="total-label">合计:</text>
        <text class="total-price">¥{{ totalPay.toFixed(2) }}</text>
      </view>
      <button class="submit-btn" :disabled="loading" @tap="submitOrder">
        {{ loading ? '提交中...' : '提交订单' }}
      </button>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.order-confirm-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.address-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.address-card {
  flex: 1;
}

.address-header {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-bottom: 10rpx;
}

.receiver-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.receiver-phone {
  font-size: 28rpx;
  color: #666;
}

.address-detail {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
}

.address-empty {
  flex: 1;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.arrow {
  font-size: 40rpx;
  color: #999;
  margin-left: 20rpx;
}

.goods-section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.goods-item {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.goods-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  background-color: #f5f5f5;
}

.goods-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.goods-name {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}

.goods-attrs {
  font-size: 24rpx;
  color: #999;
}

.goods-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.goods-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #e93b3d;
}

.goods-count {
  font-size: 24rpx;
  color: #999;
}

.coupon-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.coupon-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.coupon-label {
  font-size: 28rpx;
  color: #333;
}

.coupon-value {
  font-size: 28rpx;
  color: #e93b3d;
}

.remark-section {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.remark-label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 20rpx;
}

.remark-input {
  width: 100%;
  min-height: 120rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
}

.price-section {
  background-color: #fff;
  padding: 30rpx;
}

.price-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;

  &:last-child {
    margin-bottom: 0;
  }

  &.total {
    padding-top: 20rpx;
    border-top: 1px solid #f0f0f0;
  }
}

.price-label {
  font-size: 28rpx;
  color: #666;
}

.price-value {
  font-size: 28rpx;
  color: #333;

  &.discount {
    color: #e93b3d;
  }
}

.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  background-color: #fff;
  border-top: 1px solid #f0f0f0;
  z-index: 100;
}

.price-info {
  display: flex;
  align-items: baseline;
  gap: 10rpx;
}

.total-label {
  font-size: 28rpx;
  color: #333;
}

.total-price {
  font-size: 40rpx;
  font-weight: bold;
  color: #e93b3d;
}

.submit-btn {
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 24rpx 60rpx;
  font-size: 28rpx;
  border: none;

  &:disabled {
    opacity: 0.5;
  }
}
</style>
