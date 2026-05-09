<template>
  <div class="integration-container">
    <el-card>
      <template #header>
        <span>积分记录</span>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="integration" label="积分变化" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.type === 1 ? 'green' : 'red' }">
              {{ row.type === 1 ? '+' : '-' }}{{ row.integration }}
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
import { getIntegrationRecordList } from '@/api/member/integrationRecord'
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
    const { records, total: totalCount } = await getIntegrationRecordList({
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Integration] Failed to load data:', error)
  }
}
</script>
