<template>
  <div class="sku-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="关键词">
              <el-input v-model="searchForm.key" placeholder="SKU名称" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="skuName" label="SKU名称" />
        <el-table-column prop="price" label="价格" width="100" />
        <el-table-column prop="originalPrice" label="原价" width="100" />
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="publishStatus" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.publishStatus" type="publish" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)" v-hasPermi="['product:sku:list']">详情</el-button>
            <el-button
              :type="row.publishStatus === 1 ? 'warning' : 'success'"
              link
              @click="handlePublish(row)"
              v-hasPermi="['product:sku:edit']"
            >
              {{ row.publishStatus === 1 ? '下架' : '发布' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        v-model:page="page"
        v-model:size="size"
        :total="total"
        @update:page="loadData"
        @update:size="loadData"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSkuList, publishSku } from '@/api/product/sku'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

interface Sku {
  id?: number
  skuName: string
  price: number
  originalPrice?: number
  stock: number
  publishStatus?: number
}

const router = useRouter()
const tableData = ref<Sku[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const searchForm = reactive({
  key: ''
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getSkuList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[SKU] Failed to load data:', error)
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.key = ''
  handleSearch()
}

const handleView = (row: Sku) => {
  router.push(`/product/sku/detail/${row.id}`)
}

const handlePublish = (row: Sku) => {
  const status = row.publishStatus === 1 ? 2 : 1
  const action = status === 1 ? '发布' : '下架'

  ElMessageBox.confirm(`确定要${action}该SKU吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await publishSku(row.id!, status)
      ElMessage.success(`${action}成功`)
      await loadData()
    } catch (error) {
      console.error('[SKU] Publish failed:', error)
    }
  })
}
</script>

<style scoped lang="scss">
.sku-container {
  .el-table {
    margin-top: 20px;
  }
}
</style>
