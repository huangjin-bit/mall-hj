<script setup>
import { ref, onMounted } from 'vue'
import { getCategoryTree } from '@/api/product'

// 分类数据
const categoryTree = ref([])
const selectedLevel1 = ref(null)
const selectedLevel2 = ref(null)

// 左侧一级分类列表
const level1Categories = ref([])

// 右侧二级和三级分类
const level2Categories = ref([])

onMounted(async () => {
  await loadCategories()
})

/**
 * 加载分类树
 */
async function loadCategories() {
  try {
    const tree = await getCategoryTree()
    categoryTree.value = tree || []

    // 提取一级分类
    level1Categories.value = tree || []

    // 默认选中第一个一级分类
    if (level1Categories.value.length > 0) {
      selectLevel1(level1Categories.value[0])
    }
  } catch (error) {
    console.error('[LoadCategories Error]', error)
  }
}

/**
 * 选择一级分类
 */
function selectLevel1(category) {
  selectedLevel1.value = category
  level2Categories.value = category.children || []
}

/**
 * 选择三级分类跳转搜索
 */
function goToSearch(categoryId, categoryName) {
  uni.navigateTo({
    url: `/pages/search/index?categoryId=${categoryId}&categoryName=${categoryName}`
  })
}
</script>

<template>
  <view class="category-page">
    <!-- 左侧一级分类 -->
    <scroll-view class="level1-sidebar" scroll-y>
      <view
        v-for="category in level1Categories"
        :key="category.id"
        class="level1-item"
        :class="{ active: selectedLevel1?.id === category.id }"
        @tap="selectLevel1(category)"
      >
        <text class="level1-name">{{ category.name }}</text>
      </view>
    </scroll-view>

    <!-- 右侧二级和三级分类 -->
    <scroll-view class="level2-content" scroll-y>
      <view v-if="selectedLevel1" class="category-content">
        <!-- 二级分类分组 -->
        <view
          v-for="level2 in level2Categories"
          :key="level2.id"
          class="level2-group"
        >
          <view class="level2-title">
            <text class="level2-name">{{ level2.name }}</text>
          </view>

          <!-- 三级分类网格 -->
          <view class="level3-grid">
            <view
              v-for="level3 in level2.children"
              :key="level3.id"
              class="level3-item"
              @tap="goToSearch(level3.id, level3.name)"
            >
              <image
                v-if="level3.icon"
                :src="level3.icon"
                class="level3-icon"
                mode="aspectFill"
              />
              <view v-else class="level3-icon-placeholder">
                <text class="placeholder-text">{{ level3.name?.charAt(0) || '?' }}</text>
              </view>
              <text class="level3-name">{{ level3.name }}</text>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-if="level2Categories.length === 0" class="empty-state">
          <text class="empty-text">暂无分类数据</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<style lang="scss" scoped>
.category-page {
  display: flex;
  height: 100vh;
  background-color: #f5f5f5;
}

.level1-sidebar {
  width: 200rpx;
  height: 100%;
  background-color: #f8f8f8;
}

.level1-item {
  padding: 30rpx 20rpx;
  text-align: center;
  border-bottom: 1px solid #eaeaea;
  position: relative;

  &.active {
    background-color: #fff;
    color: #e93b3d;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 6rpx;
      height: 40rpx;
      background-color: #e93b3d;
      border-radius: 0 4rpx 4rpx 0;
    }
  }
}

.level1-name {
  font-size: 28rpx;
  color: inherit;
}

.level2-content {
  flex: 1;
  height: 100%;
  background-color: #fff;
}

.category-content {
  padding: 20rpx;
}

.level2-group {
  margin-bottom: 40rpx;
}

.level2-title {
  padding: 20rpx 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20rpx;
}

.level2-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.level3-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
}

.level3-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 16rpx;
}

.level3-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 12rpx;
  background-color: #fff;
  margin-bottom: 12rpx;
}

.level3-icon-placeholder {
  width: 100rpx;
  height: 100rpx;
  border-radius: 12rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12rpx;
}

.placeholder-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.level3-name {
  font-size: 24rpx;
  color: #333;
  text-align: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>
