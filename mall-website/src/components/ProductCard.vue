<template>
  <div class="product-card" @click="handleClick">
    <div class="product-image">
      <img :src="product.image || defaultImage" :alt="product.spuName" />
    </div>
    <div class="product-info">
      <h3 class="product-name">{{ product.spuName }}</h3>
      <div class="product-price">
        <span class="price-symbol">¥</span>
        <span class="price-value">{{ formatPrice(product.price) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import type { Sku } from '@/api/product/spu'

const props = defineProps<{
  product: Sku & { spuName: string; spuId: number }
}>()

const router = useRouter()
const defaultImage = 'https://via.placeholder.com/200x200?text=No+Image'

const formatPrice = (price: number) => {
  return price.toFixed(2)
}

const handleClick = () => {
  router.push({
    name: 'ProductDetail',
    params: { id: props.product.spuId }
  })
}
</script>

<style scoped lang="scss">
.product-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transform: translateY(-4px);
  }
}

.product-image {
  width: 100%;
  padding-top: 100%; // 1:1 比例
  position: relative;
  background-color: #f5f5f5;
  overflow: hidden;

  img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;
  }

  &:hover img {
    transform: scale(1.05);
  }
}

.product-info {
  padding: 12px;
}

.product-name {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
  height: 42px;
}

.product-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: 500;

  .price-symbol {
    font-size: 14px;
    margin-right: 2px;
  }
}
</style>
