<template>
  <div class="login-log-container">
    <el-card>
      <template #header>
        <span>登录日志</span>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="loginIp" label="登录IP" />
        <el-table-column prop="loginDevice" label="登录设备" />
        <el-table-column prop="createTime" label="登录时间" width="180" />
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
import { getLoginLogList } from '@/api/member/loginLog'
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
    const { records, total: totalCount } = await getLoginLogList({
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[LoginLog] Failed to load data:', error)
  }
}
</script>
