<script setup>
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'

onLaunch(() => {
  // 应用启动时检查登录状态
  const token = uni.getStorageSync('token')
  if (token) {
    console.log('[App] 用户已登录，token:', token)
  } else {
    console.log('[App] 用户未登录')
  }
})

onShow(() => {
  // 应用从后台进入前台
})

onHide(() => {
  // 应用从前台进入后台
})
</script>

<style lang="scss">
@import '@/uni.scss';

page {
  background-color: #f5f5f5;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
}

/* 全局通用样式 */
.container {
  padding: 20rpx;
}

.flex {
  display: flex;
}

.flex-row {
  flex-direction: row;
}

.flex-column {
  flex-direction: column;
}

.justify-center {
  justify-content: center;
}

.justify-between {
  justify-content: space-between;
}

.align-center {
  align-items: center;
}

.text-center {
  text-align: center;
}

.text-right {
  text-align: right;
}

.text-primary {
  color: #e93b3d;
}

.text-secondary {
  color: #999;
}

.text-price {
  color: #e93b3d;
  font-weight: bold;
  font-size: 32rpx;

  &::before {
    content: '¥';
    font-size: 24rpx;
    margin-right: 2rpx;
  }
}

.btn-primary {
  background-color: #e93b3d;
  color: #ffffff;
  border-radius: 40rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
  text-align: center;

  &:active {
    opacity: 0.8;
  }
}

.btn-secondary {
  background-color: #ffffff;
  color: #333333;
  border: 1px solid #e0e0e0;
  border-radius: 40rpx;
  padding: 20rpx 40rpx;
  font-size: 28rpx;
  text-align: center;
}

.card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.divider {
  height: 1px;
  background-color: #f0f0f0;
  margin: 20rpx 0;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  color: #999;

  .empty-icon {
    font-size: 100rpx;
    margin-bottom: 20rpx;
  }

  .empty-text {
    font-size: 28rpx;
  }
}
</style>
