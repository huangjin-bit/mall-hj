<template>
  <div class="product-list-page">
    <div class="page-container">
      <div class="list-layout">
        <!-- 左侧筛选 -->
        <aside class="list-sidebar">
          <SearchFilter @filter="handleFilter" />
        </aside>

        <!-- 右侧商品列表 -->
        <main class="list-main">
          <!-- 排序选项 -->
          <div class="sort-bar">
            <el-radio-group v-model="sortType" @change="handleSortChange">
              <el-radio-button label="default">综合</el-radio-button>
              <el-radio-button label="price_asc">价格升序</el-radio-button>
              <el-radio-button label="price_desc">价格降序</el-radio-button>
              <el-radio-button label="sales">销量</el-radio-button>
              <el-radio-button label="newest">最新</el-radio-button>
            </el-radio-group>
          </div>

          <!-- 商品列表 -->
          <div v-if="products.length > 0" class="product-grid">
            <ProductCard
              v-for="product in products"
              :key="product.id"
              :product="{
                ...product,
                spuName: product.spuName,
                spuId: product.id,
                id: product.id
              }"
            />
          </div>
          <EmptyState v-else message="暂无商品" />

          <!-- 分页 -->
          <Pagination
            v-if="total > 0"
            :total="total"
            :page="currentPage"
            :size="pageSize"
            @change="handlePageChange"
          />
        </main>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import SearchFilter from '@/components/SearchFilter.vue'
import Pagination from '@/components/Pagination.vue'
import EmptyState from '@/components/EmptyState.vue'
import { getSpuList } from '@/api/product/spu'
import type { Spu } from '@/api/product/spu'

const route = useRoute()

const products = ref<Spu[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const sortType = ref<'default' | 'price_asc' | 'price_desc' | 'sales' | 'newest'>('default')

// 筛选参数
const filterParams = ref<{
  categoryId?: number
  brandIds?: number[]
  minPrice?: number
  maxPrice?: number
}>({})

// 加载商品列表
const loadProducts = async () => {
  try {
    const params: any = {
      page: currentPage.value,
      size: pageSize.value,
      categoryId: filterParams.value.categoryId
    }

    if (filterParams.value.brandIds && filterParams.value.brandIds.length > 0) {
      params.brandId = filterParams.value.brandIds[0] // 简化处理，只取第一个
    }

    if (filterParams.value.minPrice !== undefined) {
      params.minPrice = filterParams.value.minPrice
    }

    if (filterParams.value.maxPrice !== undefined) {
      params.maxPrice = filterParams.value.maxPrice
    }

    const res = await getSpuList(params)
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('[ProductList] Load products failed', error)
  }
}

// 处理筛选
const handleFilter = (params: {
  categoryId?: number
  brandIds?: number[]
  minPrice?: number
  maxPrice?: number
}) => {
  filterParams.value = params
  currentPage.value = 1
  loadProducts()
}

// 处理排序
const handleSortChange = () => {
  // 这里应该调用搜索 API 支持排序
  // 简化处理，重新加载
  loadProducts()
}

// 处理分页
const handlePageChange = (page: number, size: number) => {
  currentPage.value = page
  pageSize.value = size
  loadProducts()
}

onMounted(() => {
  // 从路由参数获取分类 ID
  if (route.query.categoryId) {
    filterParams.value.categoryId = Number(route.query.categoryId)
  }

  loadProducts()
})
</script>

<style scoped lang="scss">
.product-list-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.list-layout {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: 20px;
}

.list-sidebar {
  position: sticky;
  top: 80px;
  height: fit-content;
}

.list-main {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
}

.sort-bar {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}
</style>
