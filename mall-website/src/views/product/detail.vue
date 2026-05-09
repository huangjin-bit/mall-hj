<template>
  <div class="product-detail-page">
    <div class="page-container">
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="10" animated />
      </div>

      <div v-else-if="product" class="product-detail">
        <!-- 商品基本信息 -->
        <div class="product-main">
          <!-- 商品图片 -->
          <div class="product-gallery">
            <el-carousel height="400px" indicator-position="inside">
              <el-carousel-item v-for="(image, index) in productImages" :key="index">
                <img :src="image" :alt="product.spuName" class="product-image" />
              </el-carousel-item>
            </el-carousel>
          </div>

          <!-- 商品信息 -->
          <div class="product-info">
            <h1 class="product-name">{{ product.spuName }}</h1>
            <p v-if="product.spuDescription" class="product-description">
              {{ product.spuDescription }}
            </p>

            <!-- SKU 选择 -->
            <div v-if="product.skus && product.skus.length > 0" class="sku-section">
              <SkuSelector
                :skus="product.skus"
                :sale-attrs="saleAttrs"
                @select="handleSkuSelect"
              />
            </div>

            <!-- 操作按钮 -->
            <div class="action-buttons">
              <el-button type="primary" size="large" @click="handleAddToCart" :disabled="!selectedSkuId">
                加入购物车
              </el-button>
              <el-button size="large" @click="handleBuyNow" :disabled="!selectedSkuId">
                立即购买
              </el-button>
            </div>
          </div>
        </div>

        <!-- 商品属性 -->
        <div v-if="product.baseAttrs && product.baseAttrs.length > 0" class="product-attrs">
          <h2 class="section-title">商品属性</h2>
          <el-table :data="product.baseAttrs" border>
            <el-table-column prop="attrName" label="属性名称" width="180" />
            <el-table-column prop="attrValue" label="属性值" />
          </el-table>
        </div>

        <!-- 商品详情（此处可扩展富文本内容） -->
        <div class="product-details">
          <h2 class="section-title">商品详情</h2>
          <div class="detail-content">
            <p>商品详情内容...</p>
          </div>
        </div>
      </div>

      <EmptyState v-else message="商品不存在" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SkuSelector from '@/components/SkuSelector.vue'
import EmptyState from '@/components/EmptyState.vue'
import { getSpuDetail } from '@/api/product/spu'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { ElMessage } from 'element-plus'
import type { Spu, Sku } from '@/api/product/spu'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const product = ref<Spu | null>(null)
const loading = ref(true)
const selectedSkuId = ref<number>()

// 商品图片列表
const productImages = computed(() => {
  if (!product.value) return []
  return product.value.images || []
})

// 销售属性（从 SKU 中提取）
const saleAttrs = computed(() => {
  if (!product.value?.skus) return []

  const attrMap = new Map<string, Set<string>>()

  product.value.skus.forEach(sku => {
    if (sku.saleAttrs) {
      sku.saleAttrs.forEach(attr => {
        if (!attrMap.has(attr.attrName)) {
          attrMap.set(attr.attrName, new Set())
        }
        attrMap.get(attr.attrName)!.add(attr.attrValue)
      })
    }
  })

  return Array.from(attrMap.entries()).map(([attrName, attrValues]) => ({
    attrName,
    attrValues: Array.from(attrValues)
  }))
})

// 加载商品详情
const loadProductDetail = async () => {
  loading.value = true
  try {
    const spuId = Number(route.params.id)
    const res = await getSpuDetail(spuId)
    product.value = res.data || null
  } catch (error) {
    console.error('[ProductDetail] Load product failed', error)
  } finally {
    loading.value = false
  }
}

// 处理 SKU 选择
const handleSkuSelect = (skuId: number) => {
  selectedSkuId.value = skuId
}

// 加入购物车
const handleAddToCart = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push({
      name: 'Login',
      query: { redirect: route.fullPath }
    })
    return
  }

  if (!selectedSkuId.value) {
    ElMessage.warning('请选择商品规格')
    return
  }

  try {
    await cartStore.addToCart(selectedSkuId.value, 1)
    ElMessage.success('已加入购物车')
  } catch (error) {
    console.error('[ProductDetail] Add to cart failed', error)
  }
}

// 立即购买
const handleBuyNow = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push({
      name: 'Login',
      query: { redirect: route.fullPath }
    })
    return
  }

  if (!selectedSkuId.value) {
    ElMessage.warning('请选择商品规格')
    return
  }

  // 跳转到订单确认页，携带商品信息
  router.push({
    name: 'OrderConfirm',
    query: {
      skuId: selectedSkuId.value,
      quantity: 1
    }
  })
}

onMounted(() => {
  loadProductDetail()
})
</script>

<style scoped lang="scss">
.product-detail-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.loading-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 30px;
}

.product-detail {
  .product-main {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 40px;
    margin-bottom: 40px;
  }

  .product-gallery {
    background-color: #fff;
    border-radius: 8px;
    overflow: hidden;

    .product-image {
      width: 100%;
      height: 400px;
      object-fit: cover;
    }
  }

  .product-info {
    .product-name {
      margin: 0 0 15px 0;
      font-size: 28px;
      color: #333;
      font-weight: 500;
    }

    .product-description {
      color: #666;
      line-height: 1.6;
      margin-bottom: 30px;
    }

    .sku-section {
      margin-bottom: 30px;
    }

    .action-buttons {
      display: flex;
      gap: 15px;
    }
  }

  .product-attrs,
  .product-details {
    background-color: #fff;
    border-radius: 8px;
    padding: 30px;
    margin-bottom: 20px;
  }

  .section-title {
    margin: 0 0 20px 0;
    font-size: 20px;
    color: #333;
    font-weight: 500;
  }

  .detail-content {
    color: #666;
    line-height: 1.8;
  }
}
</style>
