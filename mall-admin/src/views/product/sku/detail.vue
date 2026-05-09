<template>
  <div class="sku-detail-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>SKU详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="SKU名称">{{ skuData.skuName }}</el-descriptions-item>
        <el-descriptions-item label="价格">¥{{ skuData.price }}</el-descriptions-item>
        <el-descriptions-item label="原价">¥{{ skuData.originalPrice }}</el-descriptions-item>
        <el-descriptions-item label="库存">{{ skuData.stock }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <StatusTag :status="skuData.publishStatus" type="publish" />
        </el-descriptions-item>
        <el-descriptions-item label="销售属性" :span="2">
          <el-tag
            v-for="(val, key) in skuData.saleAttrs"
            :key="key"
            class="mr-10"
          >
            {{ key }}: {{ val }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="商品图片" :span="2">
          <div class="image-list">
            <el-image
              v-for="(img, index) in getImageList()"
              :key="index"
              :src="img"
              style="width: 100px; height: 100px; margin-right: 10px"
              fit="cover"
              :preview-src-list="getImageList()"
              :initial-index="index"
            />
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSkuDetail } from '@/api/product/sku'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const skuData = ref<any>({})

onMounted(async () => {
  try {
    const data = await getSkuDetail(Number(route.params.id))
    skuData.value = data
  } catch (error) {
    console.error('[SKU Detail] Failed to load data:', error)
  }
})

const getImageList = () => {
  if (!skuData.value.images) return []
  if (typeof skuData.value.images === 'string') {
    return skuData.value.images.split(',').filter((i: string) => i)
  }
  return skuData.value.images
}
</script>

<style scoped lang="scss">
.sku-detail-container {
  .image-list {
    display: flex;
    flex-wrap: wrap;
  }
}
</style>
