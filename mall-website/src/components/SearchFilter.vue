<template>
  <div class="search-filter">
    <div class="filter-section">
      <h4 class="section-title">分类</h4>
      <el-tree
        :data="categoryTree"
        :props="{ label: 'name', children: 'children' }"
        node-key="id"
        @node-click="handleCategoryClick"
        highlight-current
      />
    </div>

    <div class="filter-section">
      <h4 class="section-title">品牌</h4>
      <el-checkbox-group v-model="selectedBrandIds" @change="handleFilterChange">
        <el-checkbox v-for="brand in brands" :key="brand.id" :label="brand.id">
          {{ brand.name }}
        </el-checkbox>
      </el-checkbox-group>
    </div>

    <div class="filter-section">
      <h4 class="section-title">价格区间</h4>
      <div class="price-range">
        <el-input-number
          v-model="minPrice"
          :min="0"
          :precision="2"
          placeholder="最低价"
          size="small"
          controls-position="right"
        />
        <span class="separator">-</span>
        <el-input-number
          v-model="maxPrice"
          :min="0"
          :precision="2"
          placeholder="最高价"
          size="small"
          controls-position="right"
        />
        <el-button type="primary" size="small" @click="handlePriceChange">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { getCategoryTree } from '@/api/product/category'
import { getBrandList } from '@/api/product/brand'
import type { Category } from '@/api/product/category'
import type { Brand } from '@/api/product/brand'

const emit = defineEmits<{
  filter: [params: {
    categoryId?: number
    brandIds?: number[]
    minPrice?: number
    maxPrice?: number
  }]
}>()

const categoryTree = ref<Category[]>([])
const brands = ref<Brand[]>([])
const selectedBrandIds = ref<number[]>([])
const minPrice = ref<number>()
const maxPrice = ref<number>()

const loadCategories = async () => {
  try {
    const res = await getCategoryTree()
    categoryTree.value = res.data || []
  } catch (error) {
    console.error('[SearchFilter] Load categories failed', error)
  }
}

const loadBrands = async () => {
  try {
    const res = await getBrandList({ page: 1, size: 100 })
    brands.value = res.data?.records || []
  } catch (error) {
    console.error('[SearchFilter] Load brands failed', error)
  }
}

const handleCategoryClick = (category: Category) => {
  emit('filter', {
    categoryId: category.id,
    brandIds: selectedBrandIds.value.length > 0 ? selectedBrandIds.value : undefined,
    minPrice: minPrice.value,
    maxPrice: maxPrice.value
  })
}

const handleFilterChange = () => {
  emit('filter', {
    brandIds: selectedBrandIds.value.length > 0 ? selectedBrandIds.value : undefined,
    minPrice: minPrice.value,
    maxPrice: maxPrice.value
  })
}

const handlePriceChange = () => {
  emit('filter', {
    brandIds: selectedBrandIds.value.length > 0 ? selectedBrandIds.value : undefined,
    minPrice: minPrice.value,
    maxPrice: maxPrice.value
  })
}

watch(() => [selectedBrandIds.value, minPrice.value, maxPrice.value], () => {
  // 可选：自动触发筛选变化
}, { deep: true })

loadCategories()
loadBrands()
</script>

<style scoped lang="scss">
.search-filter {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
}

.filter-section {
  margin-bottom: 30px;

  &:last-child {
    margin-bottom: 0;
  }
}

.section-title {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

:deep(.el-checkbox-group) {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.price-range {
  display: flex;
  align-items: center;
  gap: 10px;

  .separator {
    color: #999;
  }

  .el-input-number {
    width: 120px;
  }
}
</style>
