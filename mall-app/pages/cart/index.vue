<script setup>
import { ref, computed, onMounted } from 'vue'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'
import { getImageUrl } from '@/api/product'

const cartStore = useCartStore()
const userStore = useUserStore()

// 购物车数据
const cartItems = computed(() => cartStore.cartItems)
const totalCount = computed(() => cartStore.totalCount)
const checkedCount = computed(() => cartStore.checkedCount)
const totalPrice = computed(() => cartStore.totalPrice)
const isAllChecked = computed(() => cartStore.isAllChecked)

// 空购物车状态
const isEmpty = computed(() => cartItems.length === 0)

onMounted(async () => {
  if (userStore.isLogin) {
    await refreshCart()
  } else {
    // 未登录跳转登录页
    uni.showModal({
      title: '提示',
      content: '请先登录',
      success: (res) => {
        if (res.confirm) {
          uni.navigateTo({
            url: '/pages/auth/login'
          })
        }
      }
    })
  }
})

/**
 * 刷新购物车
 */
async function refreshCart() {
  try {
    uni.showLoading({ title: '加载中...' })
    await cartStore.fetchCart()
  } catch (error) {
    console.error('[RefreshCart Error]', error)
  } finally {
    uni.hideLoading()
  }
}

/**
 * 全选/取消全选
 */
async function handleCheckAll() {
  try {
    await cartStore.checkAll(!isAllChecked.value)
  } catch (error) {
    console.error('[CheckAll Error]', error)
  }
}

/**
 * 选中/取消选中商品
 */
async function handleCheckItem(item) {
  try {
    await cartStore.checkItem(item.skuId, !item.checked)
  } catch (error) {
    console.error('[CheckItem Error]', error)
  }
}

/**
 * 更新商品数量
 */
async function handleQuantityChange(item, newQuantity) {
  if (newQuantity < 1) return

  try {
    await cartStore.updateQuantity(item.skuId, newQuantity)
  } catch (error) {
    console.error('[UpdateQuantity Error]', error)
  }
}

/**
 * 删除商品
 */
async function handleDeleteItem(item) {
  try {
    await uni.showModal({
      title: '提示',
      content: '确定删除该商品吗？'
    })

    await cartStore.checkItem(item.skuId, true)
    await cartStore.deleteCheckedItems()
  } catch (error) {
    console.error('[DeleteItem Error]', error)
  }
}

/**
 * 批量删除选中商品
 */
async function handleBatchDelete() {
  try {
    await cartStore.deleteCheckedItems()
  } catch (error) {
    console.error('[BatchDelete Error]', error)
  }
}

/**
 * 跳转到商品详情
 */
function goToProductDetail(spuId) {
  uni.navigateTo({
    url: `/pages/product/detail?id=${spuId}`
  })
}

/**
 * 去结算
 */
function goToCheckout() {
  if (checkedCount.value === 0) {
    uni.showToast({
      title: '请选择要结算的商品',
      icon: 'none'
    })
    return
  }

  uni.navigateTo({
    url: '/pages/order/confirm'
  })
}

/**
 * 下拉刷新
 */
async function onPullDownRefresh() {
  await refreshCart()
  uni.stopPullDownRefresh()
}
</script>

<template>
  <view class="cart-page">
    <!-- 空购物车状态 -->
    <view v-if="isEmpty" class="empty-cart">
      <view class="empty-icon">🛒</view>
      <text class="empty-text">购物车还是空的</text>
      <view class="empty-action">
        <button class="go-shopping-btn" @tap="goToProductDetail">去逛逛</button>
      </view>
    </view>

    <!-- 购物车列表 -->
    <view v-else class="cart-list">
      <!-- 顶部操作栏 -->
      <view class="cart-header">
        <view class="header-left">
          <text class="header-title">购物车 ({{ totalCount }})</text>
        </view>
        <view class="header-right" @tap="handleBatchDelete">
          <text class="delete-btn">删除选中</text>
        </view>
      </view>

      <!-- 商品列表 -->
      <scroll-view class="cart-items" scroll-y @refresherrefresh="onPullDownRefresh" :refresher-enabled="true">
        <view
          v-for="item in cartItems"
          :key="item.skuId"
          class="cart-item"
        >
          <!-- 选择框 -->
          <view class="item-checkbox" @tap="handleCheckItem(item)">
            <view class="checkbox" :class="{ checked: item.checked }">
              <text v-if="item.checked" class="check-icon">✓</text>
            </view>
          </view>

          <!-- 商品信息 -->
          <view class="item-content" @tap="goToProductDetail(item.spuId)">
            <image :src="getImageUrl(item.image)" class="item-image" mode="aspectFill" />
            <view class="item-detail">
              <text class="item-name">{{ item.spuName }}</text>
              <text class="item-attrs">{{ item.saleAttrs }}</text>
              <view class="item-bottom">
                <text class="item-price">¥{{ item.salePrice }}</text>
              </view>
            </view>
          </view>

          <!-- 数量选择器 -->
          <view class="item-quantity">
            <view class="quantity-control">
              <button
                class="quantity-btn"
                :disabled="item.count <= 1"
                @tap="handleQuantityChange(item, item.count - 1)"
              >-</button>
              <text class="quantity-text">{{ item.count }}</text>
              <button
                class="quantity-btn"
                @tap="handleQuantityChange(item, item.count + 1)"
              >+</button>
            </view>
          </view>

          <!-- 删除按钮 -->
          <view class="item-delete" @tap="handleDeleteItem(item)">
            <text class="delete-icon">🗑️</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 底部结算栏 -->
    <view v-if="!isEmpty" class="cart-footer">
      <view class="footer-left" @tap="handleCheckAll">
        <view class="select-all">
          <view class="checkbox" :class="{ checked: isAllChecked }">
            <text v-if="isAllChecked" class="check-icon">✓</text>
          </view>
          <text class="select-all-text">全选</text>
        </view>
      </view>
      <view class="footer-right">
        <view class="total-info">
          <text class="total-label">合计:</text>
          <text class="total-price">¥{{ totalPrice.toFixed(2) }}</text>
        </view>
        <button class="checkout-btn" :disabled="checkedCount === 0" @tap="goToCheckout">
          结算({{ checkedCount }})
        </button>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.cart-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 40rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 60rpx;
}

.go-shopping-btn {
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 20rpx 60rpx;
  font-size: 28rpx;
  border: none;
}

.cart-list {
  min-height: 100vh;
}

.cart-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 20rpx;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.header-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.delete-btn {
  font-size: 28rpx;
  color: #e93b3d;
}

.cart-items {
  height: calc(100vh - 200rpx);
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #fff;
  margin-bottom: 20rpx;
  position: relative;
}

.item-checkbox {
  margin-right: 20rpx;
}

.checkbox {
  width: 40rpx;
  height: 40rpx;
  border: 2px solid #e0e0e0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  &.checked {
    background-color: #e93b3d;
    border-color: #e93b3d;
  }
}

.check-icon {
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}

.item-content {
  flex: 1;
  display: flex;
  gap: 20rpx;
}

.item-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  background-color: #f5f5f5;
}

.item-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.item-name {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-attrs {
  font-size: 24rpx;
  color: #999;
}

.item-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.item-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #e93b3d;
}

.item-quantity {
  margin-left: 20rpx;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.quantity-btn {
  width: 50rpx;
  height: 50rpx;
  border: 1px solid #e0e0e0;
  border-radius: 8rpx;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  color: #333;
  padding: 0;

  &:disabled {
    opacity: 0.5;
  }
}

.quantity-text {
  font-size: 28rpx;
  color: #333;
  min-width: 40rpx;
  text-align: center;
}

.item-delete {
  position: absolute;
  right: 20rpx;
  bottom: 20rpx;
}

.delete-icon {
  font-size: 40rpx;
}

.cart-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx;
  background-color: #fff;
  border-top: 1px solid #f0f0f0;
  z-index: 100;
}

.footer-left {
  flex: 1;
}

.select-all {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.select-all-text {
  font-size: 28rpx;
  color: #333;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.total-info {
  display: flex;
  align-items: baseline;
  gap: 10rpx;
}

.total-label {
  font-size: 28rpx;
  color: #333;
}

.total-price {
  font-size: 36rpx;
  font-weight: bold;
  color: #e93b3d;
}

.checkout-btn {
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
  border: none;

  &:disabled {
    opacity: 0.5;
  }
}
</style>
