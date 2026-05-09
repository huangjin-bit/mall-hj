<template>
  <div class="search-page">
    <div class="page-container">
      <div class="search-layout">
        <!-- 左侧筛选 -->
        <aside class="search-sidebar">
          <SearchFilter @filter="handleFilter" />
        </aside>

        <!-- 右侧搜索结果 -->
        <main class="search-main">
          <!-- 搜索关键词 -->
          <div class="search-header">
            <h2>搜索结果</h2>
            <p v-if="keyword" class="search-keyword">
              关键词：<strong>"{{ keyword }}"</strong>
            </p>
          </div>

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

          <!-- 搜索结果 -->
          <div v-if="results.length > 0" class="result-grid">
            <ProductCard
              v-for="item in results"
              :key="item.id"
              :product="{
                ...item,
                spuName: item.spuName,
                spuId: item.id,
                id: item.id
              }"
            />
          </div>
          <EmptyState v-else message="未找到相关商品" />

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
import { search } from '@/api/search'
import type { SearchParams, SearchResultItem } from '@/api/search'

const route = useRoute()

const results = ref<SearchResultItem[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const sortType = ref<SearchParams['sort']>('default')
const keyword = ref('')

// 筛选参数
const filterParams = ref<{
  categoryId?: number
  brandId?: number
  minPrice?: number
  maxPrice?: number
}>({})

// 执行搜索
const performSearch = async () => {
  try {
    const params: SearchParams = {
      keyword: keyword.value || undefined,
      categoryId: filterParams.value.categoryId,
      brandId: filterParams.value.brandId,
      minPrice: filterParams.value.minPrice,
      maxPrice: filterParams.value.maxPrice,
      sort: sortType.value,
      page: currentPage.value,
      size: pageSize.value
    }

    const res = await search(params)
    results.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('[Search] Search failed', error)
  }
}

// 处理筛选
const handleFilter = (params: {
  categoryId?: number
  brandIds?: number[]
  minPrice?: number
  maxPrice?: number
}) => {
  filterParams.value = {
    categoryId: params.categoryId,
    brandId: params.brandIds?.[0],
    minPrice: params.minPrice,
    maxPrice: params.maxPrice
  }
  currentPage.value = 1
  performSearch()
}

// 处理排序
const handleSortChange = () => {
  currentPage.value = 1
  performSearch()
}

// 处理分页
const handlePageChange = (page: number, size: number) => {
  currentPage.value = page
  pageSize.value = size
  performSearch()
}

onMounted(() => {
  // 从路由参数获取搜索条件
  if (route.query.keyword) {
    keyword.value = String(route.query.keyword)
  }

  if (route.query.categoryId) {
    filterParams.value.categoryId = Number(route.query.categoryId)
  }

  performSearch()
})
</script>

<style scoped lang="scss">
.search-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.search-layout {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: 20px;
}

.search-sidebar {
  position: sticky;
  top: 80px;
  height: fit-content;
}

.search-main {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
}

.search-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;

  h2 {
    margin: 0 0 10px 0;
    font-size: 24px;
    color: #333;
  }

  .search-keyword {
    margin: 0;
    font-size: 14px;
    color: #666;

    strong {
      color: #409eff;
    }
  }
}

.sort-bar {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.result-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}
</style>
