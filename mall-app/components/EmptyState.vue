<script setup>
defineProps({
  message: {
    type: String,
    default: '暂无数据'
  },
  icon: {
    type: String,
    default: '📭'
  }
})
</script>

<template>
  <view class="empty-state">
    <text class="empty-icon">{{ icon }}</text>
    <text class="empty-message">{{ message }}</text>
  </view>
</template>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
  opacity: 0.5;
}

.empty-message {
  font-size: 28rpx;
  color: #999;
}
</style>
