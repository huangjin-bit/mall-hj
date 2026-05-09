<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="关键词">
              <el-input v-model="searchForm.key" placeholder="订单号" clearable />
            </el-form-item>
            <el-form-item label="订单状态">
              <el-select v-model="searchForm.status" placeholder="请选择" clearable>
                <el-option label="待支付" :value="1" />
                <el-option label="待发货" :value="2" />
                <el-option label="已发货" :value="3" />
                <el-option label="已完成" :value="4" />
                <el-option label="已取消" :value="5" />
                <el-option label="售后中" :value="6" />
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
        <el-table-column prop="totalAmount" label="订单总额" width="100">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="payAmount" label="应付金额" width="100">
          <template #default="{ row }">
            ¥{{ row.payAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="order" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
import { getOrderList } from '@/api/order/order'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

interface Order {
  id?: number
  orderSn?: string
  username?: string
  totalAmount: number
  payAmount: number
  status: number
  createTime?: string
}

const router = useRouter()
const tableData = ref<Order[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const searchForm = reactive({
  key: '',
  status: undefined as number | undefined
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getOrderList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Order] Failed to load data:', error)
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.key = ''
  searchForm.status = undefined
  handleSearch()
}

const handleView = (row: Order) => {
  router.push(`/order/order/detail/${row.id}`)
}
</script>

<style scoped lang="scss">
.order-container {
  .el-table {
    margin-top: 20px;
  }
}
</style>
