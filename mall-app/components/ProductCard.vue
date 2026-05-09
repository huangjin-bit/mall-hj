<script setup>
import { computed } from 'vue'
import { getImageUrl } from '@/utils'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click'])

const productImage = computed(() => getImageUrl(props.product.image))
const productName = computed(() => props.product.name || props.product.spuName || '')
const productPrice = computed(() => props.product.price || props.product.salePrice || 0)

function handleClick() {
  emit('click', props.product)
}
</script>

<template>
  <view class="product-card" @tap="handleClick">
    <image :src="productImage" class="product-image" mode="aspectFill" />
    <view class="product-info">
      <text class="product-name">{{ productName }}</text>
      <view class="product-bottom">
        <text class="product-price">¥{{ productPrice }}</text>
        <text v-if="product.sales" class="product-sales">已售{{ product.sales }}</text>
      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
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
</style>
