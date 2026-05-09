<script setup>
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { search, getHotKeywords } from '@/api/search'
import { getImageUrl } from '@/utils'

// 搜索关键词
const keyword = ref('')
const categoryId = ref(null)
const categoryName = ref('')

// 搜索历史
const searchHistory = ref([])

// 热门搜索
const hotKeywords = ref([])

// 搜索结果
const searchResults = ref([])
const hasMore = ref(true)
const loading = ref(false)

// 分页参数
const page = ref(1)
const size = ref(20)

// 排序方式
const sortType = ref('default') // default, price_asc, price_desc, sales

onLoad((options) => {
  if (options.keyword) {
    keyword.value = options.keyword
  }
  if (options.categoryId) {
    categoryId.value = options.categoryId
  }
  if (options.categoryName) {
    categoryName.value = options.categoryName
  }

  loadHotKeywords()
  loadSearchHistory()

  // 如果有关键词，直接搜索
  if (keyword.value) {
    handleSearch()
  }
})

onMounted(() => {
  // 自动聚焦搜索框
})

/**
 * 加载热门搜索
 */
async function loadHotKeywords() {
  try {
    const res = await getHotKeywords()
    hotKeywords.value = res || []
  } catch (error) {
    console.error('[LoadHotKeywords Error]', error)
  }
}

/**
 * 加载搜索历史
 */
function loadSearchHistory() {
  try {
    const history = uni.getStorageSync('searchHistory') || []
    searchHistory.value = history.slice(0, 10)
  } catch (error) {
    console.error('[LoadSearchHistory Error]', error)
  }
}

/**
 * 保存搜索历史
 */
function saveSearchHistory(key) {
  try {
    let history = uni.getStorageSync('searchHistory') || []
    history = history.filter(item => item !== key)
    history.unshift(key)
    history = history.slice(0, 10)
    uni.setStorageSync('searchHistory', history)
    searchHistory.value = history
  } catch (error) {
    console.error('[SaveSearchHistory Error]', error)
  }
}

/**
 * 清除搜索历史
 */
function clearHistory() {
  uni.showModal({
    title: '提示',
    content: '确定清空搜索历史吗？',
    success: (res) => {
      if (res.confirm) {
        searchHistory.value = []
        uni.removeStorageSync('searchHistory')
      }
    }
  })
}

/**
 * 搜索
 */
async function handleSearch() {
  if (!keyword.value.trim()) {
    uni.showToast({ title: '请输入搜索关键词', icon: 'none' })
    return
  }

  // 保存搜索历史
  saveSearchHistory(keyword.value)

  // 重置状态
  page.value = 1
  searchResults.value = []
  hasMore.value = true

  await loadSearchResults()
}

/**
 * 加载搜索结果
 */
async function loadSearchResults() {
  if (loading.value || !hasMore.value) return

  try {
    loading.value = true

    const params = {
      keyword: keyword.value,
      categoryId: categoryId.value,
      sort: sortType.value,
      page: page.value,
      size: size.value
    }

    const res = await search(params)

    if (page.value === 1) {
      searchResults.value = res.records || []
    } else {
      searchResults.value.push(...(res.records || []))
    }

    hasMore.value = res.records?.length >= size.value
  } catch (error) {
    console.error('[LoadSearchResults Error]', error)
  } finally {
    loading.value = false
  }
}

/**
 * 加载更多
 */
function loadMore() {
  if (!hasMore.value || loading.value) return

  page.value++
  loadSearchResults()
}

/**
 * 切换排序
 */
function changeSort(type) {
  sortType.value = type
  page.value = 1
  searchResults.value = []
  hasMore.value = true
  loadSearchResults()
}

/**
 * 点击热门搜索
 */
function clickHotKeyword(key) {
  keyword.value = key
  handleSearch()
}

/**
 * 跳转到商品详情
 */
function goToDetail(spuId) {
  uni.navigateTo({
    url: `/pages/product/detail?id=${spuId}`
  })
}

/**
 * 返回
 */
function goBack() {
  uni.navigateBack()
}
</script>

<template>
  <view class="search-page">
    <!-- 搜索头部 -->
    <view class="search-header">
      <view class="back-btn" @tap="goBack">
        <text>‹</text>
      </view>
      <view class="search-bar">
        <input
          v-model="keyword"
          class="search-input"
          placeholder="搜索商品"
          confirm-type="search"
          @confirm="handleSearch"
        />
        <view v-if="keyword" class="clear-btn" @tap="keyword = ''">
          <text>✕</text>
        </view>
      </view>
      <view class="search-btn" @tap="handleSearch">
        <text>搜索</text>
      </view>
    </view>

    <!-- 搜索结果 -->
    <view v-if="searchResults.length > 0" class="search-results">
      <!-- 排序选项 -->
      <view class="sort-bar">
        <view
          class="sort-item"
          :class="{ active: sortType === 'default' }"
          @tap="changeSort('default')"
        >
          <text>综合</text>
        </view>
        <view
          class="sort-item"
          :class="{ active: sortType === 'sales' }"
          @tap="changeSort('sales')"
        >
          <text>销量</text>
        </view>
        <view class="sort-item" @tap="changeSort(sortType === 'price_asc' ? 'price_desc' : 'price_asc')">
          <text>价格</text>
          <text class="sort-icon">{{ sortType === 'price_asc' ? '↑' : sortType === 'price_desc' ? '↓' : '' }}</text>
        </view>
      </view>

      <!-- 商品列表 -->
      <view class="product-list">
        <view
          v-for="product in searchResults"
          :key="product.id"
          class="product-item"
          @tap="goToDetail(product.id)"
        >
          <image :src="getImageUrl(product.image)" class="product-image" mode="aspectFill" />
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <view class="product-bottom">
              <text class="product-price">¥{{ product.price }}</text>
              <text class="product-sales">已售{{ product.sales || 0 }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 加载状态 -->
      <view class="load-status">
        <text v-if="loading">加载中...</text>
        <text v-else-if="!hasMore">没有更多了</text>
      </view>
    </view>

    <!-- 搜索建议（无搜索结果时显示） -->
    <view v-else class="search-suggestions">
      <!-- 搜索历史 -->
      <view v-if="searchHistory.length > 0" class="history-section">
        <view class="section-header">
          <text class="section-title">搜索历史</text>
          <text class="clear-btn" @tap="clearHistory">清空</text>
        </view>
        <view class="keyword-list">
          <view
            v-for="(key, index) in searchHistory"
            :key="index"
            class="keyword-item"
            @tap="clickHotKeyword(key)"
          >
            {{ key }}
          </view>
        </view>
      </view>

      <!-- 热门搜索 -->
      <view v-if="hotKeywords.length > 0" class="hot-section">
        <view class="section-header">
          <text class="section-title">热门搜索</text>
        </view>
        <view class="keyword-list">
          <view
            v-for="(kw, index) in hotKeywords"
            :key="index"
            class="keyword-item"
            :class="{ hot: index < 3 }"
            @tap="clickHotKeyword(kw)"
          >
            {{ kw }}
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.search-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.search-header {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 20rpx;
  background-color: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-btn {
  font-size: 48rpx;
  color: #333;
}

.search-bar {
  flex: 1;
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 40rpx;
  padding: 16rpx 32rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.clear-btn {
  font-size: 28rpx;
  color: #999;
  margin-left: 20rpx;
}

.search-btn {
  font-size: 28rpx;
  color: #e93b3d;
  white-space: nowrap;
}

.search-results {
  padding-bottom: 40rpx;
}

.sort-bar {
  display: flex;
  background-color: #fff;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.sort-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5rpx;
  font-size: 28rpx;
  color: #666;

  &.active {
    color: #e93b3d;
  }
}

.sort-icon {
  font-size: 24rpx;
}

.product-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  padding: 0 20rpx;
}

.product-item {
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

.product-info {
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
  font-size: 32rpx;
  font-weight: bold;
  color: #e93b3d;
}

.product-sales {
  font-size: 24rpx;
  color: #999;
}

.load-status {
  text-align: center;
  padding: 40rpx 0;
  font-size: 28rpx;
  color: #999;
}

.search-suggestions {
  padding: 30rpx;
}

.history-section,
.hot-section {
  margin-bottom: 40rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.clear-btn {
  font-size: 28rpx;
  color: #999;
}

.keyword-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.keyword-item {
  padding: 16rpx 32rpx;
  background-color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  color: #333;

  &.hot {
    background: linear-gradient(135deg, #ff6b6b, #e93b3d);
    color: #fff;
  }
}
</style>
