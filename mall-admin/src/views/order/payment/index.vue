<template>
  <div class="payment-container">
    <el-card>
      <template #header>
        <span>支付记录</span>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="orderSn" label="订单号" width="180" />
        <el-table-column prop="payType" label="支付方式" width="100">
          <template #default="{ row }">
            {{ row.payType === 1 ? '支付宝' : row.payType === 2 ? '微信' : '银行卡' }}
          </template>
        </el-table-column>
        <el-table-column prop="payAmount" label="支付金额" width="100">
          <template #default="{ row }">
            ¥{{ row.payAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="status" />
          </template>
        </el-table-column>
        <el-table-column prop="transactionId" label="交易单号" />
        <el-table-column prop="payTime" label="支付时间" width="180" />
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
import { ref, onMounted } from 'vue'
import { getPaymentInfoList } from '@/api/order/paymentInfo'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

const tableData = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getPaymentInfoList({
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Payment] Failed to load data:', error)
  }
}
</script>
