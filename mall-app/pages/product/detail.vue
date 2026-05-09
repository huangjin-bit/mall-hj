<script setup>
import { ref, computed, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getSpuDetail, getSkuDetail } from '@/api/product'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'
import { addCollect, checkCollect } from '@/api/collect'
import { getImageUrl } from '@/utils'

const cartStore = useCartStore()
const userStore = useUserStore()

// 商品数据
const spuId = ref(null)
const spuDetail = ref(null)
const skuList = ref([])
const currentSku = ref(null)

// 图片轮播
const currentImageIndex = ref(0)

// SKU选择器
const showSkuPopup = ref(false)
const selectedSku = ref(null)

// 收藏状态
const isCollected = ref(false)

// 数量
const quantity = ref(1)

// 操作类型：addCart-加购物车，buyNow-立即购买
const actionType = ref('addCart')

onLoad((options) => {
  if (options.id) {
    spuId.value = options.id
    loadProductDetail()
  }
})

onMounted(() => {
  checkCollectStatus()
})

/**
 * 加载商品详情
 */
async function loadProductDetail() {
  try {
    uni.showLoading({ title: '加载中...' })

    const detail = await getSpuDetail(spuId.value)
    spuDetail.value = detail
    skuList.value = detail.skuList || []

    // 默认选择第一个SKU
    if (skuList.value.length > 0) {
      currentSku.value = skuList.value[0]
      selectedSku.value = { ...skuList.value[0] }
    }
  } catch (error) {
    console.error('[LoadProductDetail Error]', error)
  } finally {
    uni.hideLoading()
  }
}

/**
 * 检查收藏状态
 */
async function checkCollectStatus() {
  if (!userStore.isLogin) return

  try {
    const res = await checkCollect(spuId.value)
    isCollected.value = res || false
  } catch (error) {
    console.error('[CheckCollect Error]', error)
  }
}

/**
 * 切换收藏
 */
async function toggleCollect() {
  if (!userStore.checkAuth()) return

  try {
    if (isCollected.value) {
      await addCollect(spuId.value)
      isCollected.value = false
      uni.showToast({ title: '已取消收藏', icon: 'success' })
    } else {
      await addCollect(spuId.value)
      isCollected.value = true
      uni.showToast({ title: '收藏成功', icon: 'success' })
    }
  } catch (error) {
    console.error('[ToggleCollect Error]', error)
  }
}

/**
 * 打开SKU选择器
 */
function openSkuPopup(type) {
  actionType.value = type
  showSkuPopup.value = true
}

/**
 * 关闭SKU选择器
 */
function closeSkuPopup() {
  showSkuPopup.value = false
}

/**
 * 确认选择
 */
function confirmSku() {
  if (!selectedSku.value) {
    uni.showToast({ title: '请选择商品规格', icon: 'none' })
    return
  }

  currentSku.value = selectedSku.value
  showSkuPopup.value = false

  if (actionType.value === 'addCart') {
    handleAddToCart()
  } else {
    handleBuyNow()
  }
}

/**
 * 加入购物车
 */
async function handleAddToCart() {
  try {
    await cartStore.addToCart(currentSku.value.id, quantity.value)
  } catch (error) {
    console.error('[AddToCart Error]', error)
  }
}

/**
 * 立即购买
 */
function handleBuyNow() {
  if (!userStore.checkAuth()) return

  uni.navigateTo({
    url: `/pages/order/confirm?skuId=${currentSku.value.id}&count=${quantity.value}`
  })
}

/**
 * 分享商品
 */
function onShareAppMessage() {
  return {
    title: spuDetail.value?.name || '商品分享',
    path: `/pages/product/detail?id=${spuId.value}`,
    imageUrl: spuDetail.value?.images?.[0] || ''
  }
}
</script>

<template>
  <view class="product-detail-page" v-if="spuDetail">
    <!-- 图片轮播 -->
    <swiper class="image-swiper" :current="currentImageIndex" @change="currentImageIndex = $event.detail.current">
      <swiper-item v-for="(image, index) in spuDetail.images" :key="index">
        <image :src="getImageUrl(image)" mode="aspectFill" class="product-image" @tap="previewImage(index)" />
      </swiper-item>
    </swiper>

    <!-- 图片指示器 -->
    <view class="image-indicator">
      <text>{{ currentImageIndex + 1 }}/{{ spuDetail.images.length }}</text>
    </view>

    <!-- 商品信息 -->
    <view class="product-info">
      <view class="price-section">
        <text class="current-price">¥{{ currentSku?.salePrice || spuDetail.minPrice }}</text>
        <text v-if="spuDetail.maxPrice > spuDetail.minPrice" class="price-range">
          - ¥{{ spuDetail.maxPrice }}
        </text>
        <text v-if="currentSku?.originalPrice" class="original-price">
          ¥{{ currentSku.originalPrice }}
        </text>
      </view>

      <view class="product-name">
        <text>{{ spuDetail.name }}</text>
      </view>

      <view class="product-tags">
        <text v-for="tag in spuDetail.tags" :key="tag" class="tag">{{ tag }}</text>
      </view>
    </view>

    <!-- SKU选择 -->
    <view class="sku-section" @tap="openSkuPopup('select')">
      <view class="sku-left">
        <text class="sku-label">选择</text>
        <text class="sku-value">{{ currentSku?.saleAttrs || '请选择规格' }}</text>
      </view>
      <text class="sku-arrow">›</text>
    </view>

    <!-- 商品详情 -->
    <view class="detail-section">
      <view class="section-title">商品详情</view>
      <view class="detail-content">
        <view v-for="(attr, index) in spuDetail.baseAttrs" :key="index" class="attr-item">
          <text class="attr-name">{{ attr.name }}:</text>
          <text class="attr-value">{{ attr.value }}</text>
        </view>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <view class="action-icons">
        <view class="action-item" @tap="toggleCollect">
          <text class="action-icon">{{ isCollected ? '⭐' : '☆' }}</text>
          <text class="action-text">收藏</text>
        </view>
        <view class="action-item" @tap="goToCart">
          <text class="action-icon">🛒</text>
          <text class="action-text">购物车</text>
          <view v-if="cartStore.totalCount > 0" class="cart-badge">{{ cartStore.totalCount }}</view>
        </view>
      </view>

      <view class="action-buttons">
        <button class="add-cart-btn" @tap="openSkuPopup('addCart')">加入购物车</button>
        <button class="buy-now-btn" @tap="openSkuPopup('buyNow')">立即购买</button>
      </view>
    </view>

    <!-- SKU选择弹窗 -->
    <view v-if="showSkuPopup" class="sku-popup-mask" @tap="closeSkuPopup">
      <view class="sku-popup" @tap.stop>
        <view class="popup-header">
          <image :src="getImageUrl(currentSku?.image)" class="popup-image" mode="aspectFill" />
          <view class="popup-info">
            <text class="popup-price">¥{{ currentSku?.salePrice }}</text>
            <text class="popup-stock">库存: {{ currentSku?.stock || 0 }}</text>
            <text class="popup-selected">已选: {{ currentSku?.saleAttrs || '请选择' }}</text>
          </view>
        </view>

        <view class="popup-content">
          <!-- SKU列表 -->
          <view class="sku-list">
            <view
              v-for="sku in skuList"
              :key="sku.id"
              class="sku-option"
              :class="{ active: selectedSku?.id === sku.id }"
              @tap="selectedSku = sku"
            >
              {{ sku.saleAttrs }}
            </view>
          </view>

          <!-- 数量选择 -->
          <view class="quantity-section">
            <text class="quantity-label">数量</text>
            <view class="quantity-control">
              <button class="quantity-btn" :disabled="quantity <= 1" @tap="quantity--">-</button>
              <text class="quantity-text">{{ quantity }}</text>
              <button class="quantity-btn" @tap="quantity++">+</button>
            </view>
          </view>
        </view>

        <view class="popup-footer">
          <button class="confirm-btn" @tap="confirmSku">确认</button>
        </view>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.product-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.image-swiper {
  width: 100%;
  height: 750rpx;
  background-color: #fff;
}

.product-image {
  width: 100%;
  height: 100%;
}

.image-indicator {
  position: absolute;
  top: 30rpx;
  right: 30rpx;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.product-info {
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.price-section {
  display: flex;
  align-items: baseline;
  gap: 10rpx;
  margin-bottom: 20rpx;
}

.current-price {
  font-size: 48rpx;
  font-weight: bold;
  color: #e93b3d;
}

.price-range {
  font-size: 32rpx;
  color: #e93b3d;
}

.original-price {
  font-size: 28rpx;
  color: #999;
  text-decoration: line-through;
}

.product-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  line-height: 1.5;
  margin-bottom: 20rpx;
}

.product-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
}

.tag {
  background-color: #fff3f0;
  color: #e93b3d;
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.sku-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.sku-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.sku-label {
  font-size: 28rpx;
  color: #999;
}

.sku-value {
  font-size: 28rpx;
  color: #333;
}

.sku-arrow {
  font-size: 40rpx;
  color: #999;
}

.detail-section {
  background-color: #fff;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.attr-item {
  display: flex;
  gap: 20rpx;
}

.attr-name {
  font-size: 28rpx;
  color: #999;
  min-width: 150rpx;
}

.attr-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  background-color: #fff;
  border-top: 1px solid #f0f0f0;
  padding: 16rpx 20rpx;
  z-index: 100;
}

.action-icons {
  display: flex;
  gap: 40rpx;
  padding-right: 40rpx;
}

.action-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5rpx;
}

.action-icon {
  font-size: 40rpx;
}

.action-text {
  font-size: 20rpx;
  color: #666;
}

.cart-badge {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
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

.action-buttons {
  flex: 1;
  display: flex;
  gap: 20rpx;
}

.add-cart-btn,
.buy-now-btn {
  flex: 1;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
  padding: 24rpx;
}

.add-cart-btn {
  background-color: #ff9800;
  color: #fff;
}

.buy-now-btn {
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
}

.sku-popup-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  align-items: flex-end;
}

.sku-popup {
  width: 100%;
  max-height: 80vh;
  background-color: #fff;
  border-radius: 24rpx 24rpx 0 0;
  display: flex;
  flex-direction: column;
}

.popup-header {
  display: flex;
  gap: 20rpx;
  padding: 30rpx;
  border-bottom: 1px solid #f0f0f0;
}

.popup-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  background-color: #f5f5f5;
}

.popup-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
  justify-content: center;
}

.popup-price {
  font-size: 40rpx;
  font-weight: bold;
  color: #e93b3d;
}

.popup-stock,
.popup-selected {
  font-size: 24rpx;
  color: #999;
}

.popup-content {
  flex: 1;
  overflow-y: auto;
  padding: 30rpx;
}

.sku-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 40rpx;
}

.sku-option {
  padding: 16rpx 32rpx;
  border: 1px solid #e0e0e0;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333;

  &.active {
    border-color: #e93b3d;
    background-color: #fff3f0;
    color: #e93b3d;
  }
}

.quantity-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.quantity-label {
  font-size: 28rpx;
  color: #333;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.quantity-btn {
  width: 60rpx;
  height: 60rpx;
  border: 1px solid #e0e0e0;
  border-radius: 8rpx;
  background-color: #fff;
  font-size: 32rpx;
  color: #333;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;

  &:disabled {
    opacity: 0.5;
  }
}

.quantity-text {
  font-size: 28rpx;
  color: #333;
  min-width: 60rpx;
  text-align: center;
}

.popup-footer {
  padding: 20rpx 30rpx;
  border-top: 1px solid #f0f0f0;
}

.confirm-btn {
  width: 100%;
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 28rpx;
  font-size: 28rpx;
  border: none;
}
</style>
