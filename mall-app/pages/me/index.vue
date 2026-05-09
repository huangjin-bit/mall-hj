<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import { getOrderCount } from '@/api/order'

const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => userStore.userInfo)
const isLogin = computed(() => userStore.isLogin)
const nickname = computed(() => userStore.nickname)

// 订单数量统计
const orderCount = ref({
  pendingPayment: 0,
  pendingShipment: 0,
  shipped: 0,
  returns: 0
})

onMounted(async () => {
  if (isLogin.value) {
    await loadOrderCount()
  }
})

/**
 * 加载订单数量统计
 */
async function loadOrderCount() {
  try {
    const res = await getOrderCount()
    orderCount.value = res || {}
  } catch (error) {
    console.error('[LoadOrderCount Error]', error)
  }
}

/**
 * 跳转登录页
 */
function goToLogin() {
  uni.navigateTo({
    url: '/pages/auth/login'
  })
}

/**
 * 跳转到订单列表
 */
function goToOrders(status = '') {
  if (!checkLogin()) return

  uni.navigateTo({
    url: `/pages/order/list?status=${status}`
  })
}

/**
 * 跳转到我的优惠券
 */
function goToCoupons() {
  if (!checkLogin()) return

  uni.navigateTo({
    url: '/pages/user/coupons'
  })
}

/**
 * 跳转到我的收藏
 */
function goToCollect() {
  if (!checkLogin()) return

  uni.navigateTo({
    url: '/pages/user/collect'
  })
}

/**
 * 跳转到收货地址
 */
function goToAddress() {
  if (!checkLogin()) return

  uni.navigateTo({
    url: '/pages/user/address'
  })
}

/**
 * 跳转到个人信息
 */
function goToProfile() {
  if (!checkLogin()) return

  uni.navigateTo({
    url: '/pages/user/profile'
  })
}

/**
 * 检查登录状态
 */
function checkLogin() {
  if (!isLogin.value) {
    uni.showModal({
      title: '提示',
      content: '请先登录',
      success: (res) => {
        if (res.confirm) {
          goToLogin()
        }
      }
    })
    return false
  }
  return true
}

/**
 * 退出登录
 */
function handleLogout() {
  uni.showModal({
    title: '提示',
    content: '确定退出登录吗？',
    success: async (res) => {
      if (res.confirm) {
        await userStore.logout()
      }
    }
  })
}
</script>

<template>
  <view class="me-page">
    <!-- 用户信息头部 -->
    <view class="user-header">
      <view v-if="isLogin" class="user-info" @tap="goToProfile">
        <image
          :src="userInfo?.avatarUrl || '/static/images/default-avatar.png'"
          class="user-avatar"
          mode="aspectFill"
        />
        <view class="user-detail">
          <text class="user-name">{{ nickname }}</text>
          <text class="user-phone">{{ userInfo?.phone || '未绑定手机' }}</text>
        </view>
      </view>
      <view v-else class="user-login" @tap="goToLogin">
        <image src="/static/images/default-avatar.png" class="user-avatar" mode="aspectFill" />
        <view class="user-detail">
          <text class="user-name">未登录</text>
          <text class="login-tip">点击登录</text>
        </view>
      </view>
    </view>

    <!-- 订单管理 -->
    <view class="order-section">
      <view class="section-header">
        <text class="section-title">我的订单</text>
        <view class="more-btn" @tap="goToOrders()">
          <text>全部订单</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="order-status-grid">
        <view class="status-item" @tap="goToOrders('pending_payment')">
          <view class="status-icon">💳</view>
          <text class="status-name">待付款</text>
          <view v-if="orderCount.pendingPayment > 0" class="status-badge">
            {{ orderCount.pendingPayment > 99 ? '99+' : orderCount.pendingPayment }}
          </view>
        </view>
        <view class="status-item" @tap="goToOrders('pending_shipment')">
          <view class="status-icon">📦</view>
          <text class="status-name">待发货</text>
          <view v-if="orderCount.pendingShipment > 0" class="status-badge">
            {{ orderCount.pendingShipment > 99 ? '99+' : orderCount.pendingShipment }}
          </view>
        </view>
        <view class="status-item" @tap="goToOrders('shipped')">
          <view class="status-icon">🚚</view>
          <text class="status-name">待收货</text>
          <view v-if="orderCount.shipped > 0" class="status-badge">
            {{ orderCount.shipped > 99 ? '99+' : orderCount.shipped }}
          </view>
        </view>
        <view class="status-item" @tap="goToOrders('returns')">
          <view class="status-icon">🔄</view>
          <text class="status-name">退换货</text>
          <view v-if="orderCount.returns > 0" class="status-badge">
            {{ orderCount.returns > 99 ? '99+' : orderCount.returns }}
          </view>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-item" @tap="goToCollect">
        <view class="menu-left">
          <text class="menu-icon">⭐</text>
          <text class="menu-name">我的收藏</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>

      <view class="menu-item" @tap="goToCoupons">
        <view class="menu-left">
          <text class="menu-icon">🎟️</text>
          <text class="menu-name">我的优惠券</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>

      <view class="menu-item" @tap="goToAddress">
        <view class="menu-left">
          <text class="menu-icon">📍</text>
          <text class="menu-name">收货地址</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>

      <view class="menu-item" @tap="goToProfile">
        <view class="menu-left">
          <text class="menu-icon">👤</text>
          <text class="menu-name">个人信息</text>
        </view>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- 退出登录按钮 -->
    <view v-if="isLogin" class="logout-section">
      <button class="logout-btn" @tap="handleLogout">退出登录</button>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.me-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40rpx;
}

.user-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 40rpx;
  margin-bottom: 20rpx;
}

.user-info,
.user-login {
  display: flex;
  align-items: center;
  gap: 30rpx;
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  border: 4px solid rgba(255, 255, 255, 0.3);
  background-color: #fff;
}

.user-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.user-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.user-phone,
.login-tip {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.order-section,
.menu-section {
  background-color: #fff;
  margin-bottom: 20rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 40rpx;
  border-bottom: 1px solid #f0f0f0;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.more-btn {
  display: flex;
  align-items: center;
  gap: 10rpx;
  font-size: 28rpx;
  color: #999;
}

.arrow {
  font-size: 40rpx;
  font-weight: 300;
}

.order-status-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  padding: 40rpx 20rpx;
}

.status-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
}

.status-icon {
  font-size: 48rpx;
}

.status-name {
  font-size: 24rpx;
  color: #666;
}

.status-badge {
  position: absolute;
  top: -10rpx;
  right: 0;
  background-color: #e93b3d;
  color: #fff;
  font-size: 20rpx;
  min-width: 32rpx;
  height: 32rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 40rpx;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.menu-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.menu-icon {
  font-size: 40rpx;
}

.menu-name {
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 40rpx;
  color: #999;
  font-weight: 300;
}

.logout-section {
  padding: 40rpx;
}

.logout-btn {
  background-color: #fff;
  color: #e93b3d;
  border: 1px solid #e93b3d;
  border-radius: 40rpx;
  padding: 24rpx;
  font-size: 28rpx;
}
</style>
