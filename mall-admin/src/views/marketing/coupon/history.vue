<template>
  <div class="coupon-history-container">
    <el-card>
      <template #header>
        <span>优惠券领取记录</span>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="username" label="会员" />
        <el-table-column prop="couponName" label="优惠券" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'info' : 'warning'">
              {{ row.status === 1 ? '未使用' : row.status === 2 ? '已使用' : '已过期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderId" label="订单号" />
        <el-table-column prop="useTime" label="使用时间" width="180" />
        <el-table-column prop="receiveTime" label="领取时间" width="180" />
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
import { getCouponHistoryList } from '@/api/seckill/couponHistory'
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
    const { records, total: totalCount } = await getCouponHistoryList({
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[CouponHistory] Failed to load data:', error)
  }
}
</script>
