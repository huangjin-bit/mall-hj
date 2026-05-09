<template>
  <div class="return-apply-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="状态">
              <el-select v-model="searchForm.status" placeholder="请选择" clearable>
                <el-option label="待审核" :value="1" />
                <el-option label="审核通过" :value="2" />
                <el-option label="审核拒绝" :value="3" />
                <el-option label="退款中" :value="4" />
                <el-option label="已退款" :value="5" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="orderSn" label="订单号" width="180" />
        <el-table-column prop="username" label="会员" />
        <el-table-column prop="skuName" label="商品名称" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            {{ row.type === 1 ? '退款' : '退货退款' }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="return" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">详情</el-button>
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
import { getOrderReturnList } from '@/api/order/orderReturn'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

const router = useRouter()
const tableData = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const searchForm = reactive({
  status: undefined as number | undefined
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getOrderReturnList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[ReturnApply] Failed to load data:', error)
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.status = undefined
  handleSearch()
}

const handleView = (row: any) => {
  router.push(`/order/return-apply/${row.id}`)
}
</script>
