<template>
  <div class="growth-container">
    <el-card>
      <template #header>
        <span>成长记录</span>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="growth" label="成长值变化" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.type === 1 ? 'green' : 'red' }">
              {{ row.type === 1 ? '+' : '-' }}{{ row.growth }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" show-overflow-tooltip />
        <el-table-column prop="createTime" label="时间" width="180" />
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
import { getGrowthRecordList } from '@/api/member/growthRecord'
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
    const { records, total: totalCount } = await getGrowthRecordList({
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Growth] Failed to load data:', error)
  }
}
</script>
