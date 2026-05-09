<script setup>
import { ref, onMounted } from 'vue'
import { getHotProducts, getRecommendedProducts, getSpuList } from '@/api/product'
import { getActiveSessions, getSessionSkus } from '@/api/seckill'
import { formatCountdown, getImageUrl } from '@/utils'

// 轮播图
const banners = ref([])

// 分类导航
const categories = ref([])

// 秒杀数据
const seckillSessions = ref([])
const currentSessionIndex = ref(0)
const seckillProducts = ref([])
const countdown = ref(0)

// 热门商品
const hotProducts = ref([])

// 推荐商品
const recommendedProducts = ref([])

// 加载状态
const loading = ref(false)

// 倒计时定时器
let countdownTimer = null

onMounted(() => {
  loadBanners()
  loadCategories()
  loadSeckillData()
  loadHotProducts()
  loadRecommendedProducts()
})

/**
 * 加载轮播图
 */
async function loadBanners() {
  // 这里应该调用广告位接口，暂时使用模拟数据
  banners.value = [
    { id: 1, image: '/static/images/banner1.jpg', link: '' },
    { id: 2, image: '/static/images/banner2.jpg', link: '' },
    { id: 3, image: '/static/images/banner3.jpg', link: '' }
  ]
}

/**
 * 加载分类导航
 */
async function loadCategories() {
  // 显示前10个一级分类
  try {
    const res = await getSpuList({ page: 1, size: 10 })
    // 从商品中提取分类信息，这里应该调用专门的分类接口
    categories.value = [
      { id: 1, name: '手机', icon: '📱' },
      { id: 2, name: '电脑', icon: '💻' },
      { id: 3, name: '家电', icon: '🎮' },
      { id: 4, name: '服装', icon: '👕' },
      { id: 5, name: '食品', icon: '🍔' },
      { id: 6, name: '美妆', icon: '💄' },
      { id: 7, name: '家居', icon: '🏠' },
      { id: 8, name: '母婴', icon: '👶' },
      { id: 9, name: '运动', icon: '⚽' },
      { id: 10, name: '图书', icon: '📚' }
    ]
  } catch (error) {
    console.error('[LoadCategories Error]', error)
  }
}

/**
 * 加载秒杀数据
 */
async function loadSeckillData() {
  try {
    const sessions = await getActiveSessions()
    if (sessions && sessions.length > 0) {
      seckillSessions.value = sessions
      currentSessionIndex.value = 0
      await loadSessionProducts(sessions[0].id)
      startCountdown(sessions[0].endTime)
    }
  } catch (error) {
    console.error('[LoadSeckillData Error]', error)
  }
}

/**
 * 加载场次商品
 */
async function loadSessionProducts(sessionId) {
  try {
    const products = await getSessionSkus(sessionId)
    seckillProducts.value = products || []
  } catch (error) {
    console.error('[LoadSessionProducts Error]', error)
  }
}

/**
 * 切换秒杀场次
 */
async function switchSession(index) {
  currentSessionIndex.value = index
  const session = seckillSessions.value[index]
  await loadSessionProducts(session.id)
  startCountdown(session.endTime)
}

/**
 * 开始倒计时
 */
function startCountdown(endTime) {
  if (countdownTimer) {
    clearInterval(countdownTimer)
  }

  const updateCountdown = () => {
    const now = Date.now()
    const end = new Date(endTime).getTime()
    const remaining = Math.floor((end - now) / 1000)

    if (remaining <= 0) {
      countdown.value = 0
      clearInterval(countdownTimer)
    } else {
      countdown.value = remaining
    }
  }

  updateCountdown()
  countdownTimer = setInterval(updateCountdown, 1000)
}

/**
 * 加载热门商品
 */
async function loadHotProducts() {
  try {
    const res = await getHotProducts({ page: 1, size: 10 })
    hotProducts.value = res.records || []
  } catch (error) {
    console.error('[LoadHotProducts Error]', error)
  }
}

/**
 * 加载推荐商品
 */
async function loadRecommendedProducts() {
  try {
    const res = await getRecommendedProducts({ page: 1, size: 10 })
    recommendedProducts.value = res.records || []
  } catch (error) {
    console.error('[LoadRecommendedProducts Error]', error)
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
 * 跳转到分类页面
 */
function goToCategory(categoryId) {
  uni.navigateTo({
    url: `/pages/search/index?categoryId=${categoryId}`
  })
}

/**
 * 跳转到秒杀详情
 */
function goToSeckillDetail(skuId) {
  uni.navigateTo({
    url: `/pages/seckill/detail?id=${skuId}`
  })
}

/**
 * 跳转到搜索页面
 */
function goToSearch() {
  uni.navigateTo({
    url: '/pages/search/index'
  })
}
</script>

<template>
  <view class="home-page">
    <!-- 顶部搜索栏 -->
    <view class="search-bar" @tap="goToSearch">
      <view class="search-input">
        <text class="search-icon">🔍</text>
        <text class="search-placeholder">搜索商品</text>
      </view>
    </view>

    <!-- 轮播图 -->
    <swiper class="banner-swiper" indicator-dots circular autoplay interval="3000">
      <swiper-item v-for="banner in banners" :key="banner.id">
        <image :src="banner.image" mode="aspectFill" class="banner-image" />
      </swiper-item>
    </swiper>

    <!-- 分类导航 -->
    <view class="category-grid">
      <view
        v-for="category in categories"
        :key="category.id"
        class="category-item"
        @tap="goToCategory(category.id)"
      >
        <text class="category-icon">{{ category.icon }}</text>
        <text class="category-name">{{ category.name }}</text>
      </view>
    </view>

    <!-- 秒杀专区 -->
    <view class="seckill-section" v-if="seckillSessions.length > 0">
      <view class="section-header">
        <view class="section-title">
          <text class="title-icon">⚡</text>
          <text class="title-text">限时秒杀</text>
        </view>
        <view class="countdown">
          <text class="countdown-text">{{ formatCountdown(countdown) }}</text>
        </view>
      </view>

      <!-- 场次切换 -->
      <scroll-view class="session-tabs" scroll-x>
        <view
          v-for="(session, index) in seckillSessions"
          :key="session.id"
          class="session-tab"
          :class="{ active: index === currentSessionIndex }"
          @tap="switchSession(index)"
        >
          <text class="session-time">{{ session.timeRange }}</text>
          <text class="session-status">{{ index === currentSessionIndex ? '抢购中' : '即将开始' }}</text>
        </view>
      </scroll-view>

      <!-- 秒杀商品列表 -->
      <scroll-view class="seckill-products" scroll-x>
        <view
          v-for="product in seckillProducts"
          :key="product.skuId"
          class="seckill-product"
          @tap="goToSeckillDetail(product.skuId)"
        >
          <image :src="getImageUrl(product.image)" class="product-image" mode="aspectFill" />
          <view class="product-info">
            <text class="product-price">¥{{ product.seckillPrice }}</text>
            <text class="product-original-price">¥{{ product.originalPrice }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 热门商品 -->
    <view class="hot-section" v-if="hotProducts.length > 0">
      <view class="section-header">
        <text class="section-title-text">🔥 热门推荐</text>
      </view>
      <view class="product-grid">
        <view
          v-for="product in hotProducts"
          :key="product.id"
          class="product-card"
          @tap="goToProductDetail(product.id)"
        >
          <image :src="getImageUrl(product.image)" class="product-image" mode="aspectFill" />
          <view class="product-detail">
            <text class="product-name">{{ product.name }}</text>
            <view class="product-bottom">
              <text class="product-price">¥{{ product.price }}</text>
              <text class="product-sales">已售{{ product.sales || 0 }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 推荐商品 -->
    <view class="recommend-section" v-if="recommendedProducts.length > 0">
      <view class="section-header">
        <text class="section-title-text">💖 为你推荐</text>
      </view>
      <view class="product-grid">
        <view
          v-for="product in recommendedProducts"
          :key="product.id"
          class="product-card"
          @tap="goToProductDetail(product.id)"
        >
          <image :src="getImageUrl(product.image)" class="product-image" mode="aspectFill" />
          <view class="product-detail">
            <text class="product-name">{{ product.name }}</text>
            <view class="product-bottom">
              <text class="product-price">¥{{ product.price }}</text>
              <text class="product-sales">已售{{ product.sales || 0 }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 100rpx;
}

.search-bar {
  padding: 20rpx;
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-input {
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 40rpx;
  padding: 16rpx 32rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.search-placeholder {
  font-size: 28rpx;
  color: #999;
}

.banner-swiper {
  width: 100%;
  height: 360rpx;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20rpx;
  padding: 30rpx 20rpx;
  background-color: #fff;
  margin-bottom: 20rpx;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
}

.category-icon {
  font-size: 48rpx;
}

.category-name {
  font-size: 24rpx;
  color: #333;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 20rpx 20rpx;
  background-color: #fff;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.title-icon {
  font-size: 36rpx;
}

.title-text {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-title-text {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.countdown {
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  padding: 8rpx 20rpx;
  border-radius: 40rpx;
}

.countdown-text {
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}

.session-tabs {
  display: flex;
  white-space: nowrap;
  padding: 0 20rpx;
  background-color: #fff;
}

.session-tab {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
  margin-right: 20rpx;
  border-radius: 16rpx;
  background-color: #f5f5f5;

  &.active {
    background: linear-gradient(135deg, #ff6b6b, #e93b3d);
    color: #fff;
  }
}

.session-time {
  font-size: 28rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.session-status {
  font-size: 20rpx;
}

.seckill-products {
  display: flex;
  white-space: nowrap;
  padding: 20rpx;
  background-color: #fff;
}

.seckill-product {
  display: inline-block;
  width: 200rpx;
  margin-right: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
  background-color: #f9f9f9;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  padding: 0 20rpx 20rpx;
}

.product-card {
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

.product-image {
  width: 100%;
  height: 340rpx;
  background-color: #f5f5f5;
}

.product-detail {
  padding: 20rpx;
}

.product-name {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
  height: 80rpx;
}

.product-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16rpx;
}

.product-price {
  color: #e93b3d;
  font-size: 32rpx;
  font-weight: bold;
}

.product-original-price {
  font-size: 24rpx;
  color: #999;
  text-decoration: line-through;
}

.product-sales {
  font-size: 24rpx;
  color: #999;
}

.seckill-section,
.hot-section,
.recommend-section {
  margin-bottom: 20rpx;
}
</style>
