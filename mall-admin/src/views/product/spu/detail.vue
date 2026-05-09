<template>
  <div class="spu-detail-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>SPU详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="SPU名称">{{ spuData.spuName }}</el-descriptions-item>
            <el-descriptions-item label="商品品牌">{{ spuData.brandName }}</el-descriptions-item>
            <el-descriptions-item label="商品分类">{{ spuData.categoryName }}</el-descriptions-item>
            <el-descriptions-item label="商品重量">{{ spuData.weight }}g</el-descriptions-item>
            <el-descriptions-item label="发布状态">
              <StatusTag :status="spuData.publishStatus" type="publish" />
            </el-descriptions-item>
            <el-descriptions-item label="审核状态">
              <StatusTag :status="spuData.auditStatus" type="audit" />
            </el-descriptions-item>
            <el-descriptions-item label="商品描述" :span="2">
              {{ spuData.description }}
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
        </el-tab-pane>

        <el-tab-pane label="SKU列表" name="sku">
          <el-table :data="spuData.skus || []" border>
            <el-table-column prop="skuName" label="SKU名称" />
            <el-table-column prop="price" label="价格" />
            <el-table-column prop="originalPrice" label="原价" />
            <el-table-column prop="stock" label="库存" />
            <el-table-column prop="publishStatus" label="状态" width="100">
              <template #default="{ row }">
                <StatusTag :status="row.publishStatus" type="publish" />
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="基本属性" name="attr">
          <el-table :data="spuData.baseAttrs || []" border>
            <el-table-column prop="attrName" label="属性名称" />
            <el-table-column prop="attrValue" label="属性值" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSpuDetail } from '@/api/product/spu'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const activeTab = ref('basic')
const spuData = ref<any>({})

onMounted(async () => {
  try {
    const data = await getSpuDetail(Number(route.params.id))
    spuData.value = data
  } catch (error) {
    console.error('[SPU Detail] Failed to load data:', error)
  }
})

const getImageList = () => {
  if (!spuData.value.images) return []
  if (typeof spuData.value.images === 'string') {
    return spuData.value.images.split(',').filter((i: string) => i)
  }
  return spuData.value.images
}
</script>

<style scoped lang="scss">
.spu-detail-container {
  .image-list {
    display: flex;
    flex-wrap: wrap;
  }
}
</style>
