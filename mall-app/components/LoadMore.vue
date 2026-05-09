<script setup>
defineProps({
  status: {
    type: String,
    default: 'loading',
    validator: (value) => ['loading', 'noMore', 'error'].includes(value)
  },
  message: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['retry'])

function handleRetry() {
  emit('retry')
}
</script>

<template>
  <view class="load-more">
    <view v-if="status === 'loading'" class="loading">
      <view class="loading-spinner"></view>
      <text class="loading-text">加载中...</text>
    </view>

    <view v-else-if="status === 'noMore'" class="no-more">
      <text class="no-more-text">{{ message || '没有更多了' }}</text>
    </view>

    <view v-else-if="status === 'error'" class="error" @tap="handleRetry">
      <text class="error-text">{{ message || '加载失败，点击重试' }}</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.load-more {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40rpx 0;
}

.loading {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.loading-spinner {
  width: 32rpx;
  height: 32rpx;
  border: 3rpx solid #f0f0f0;
  border-top-color: #e93b3d;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: 28rpx;
  color: #999;
}

.no-more {
  text-align: center;
}

.no-more-text {
  font-size: 24rpx;
  color: #999;
}

.error {
  text-align: center;
}

.error-text {
  font-size: 24rpx;
  color: #e93b3d;
}
</style>
