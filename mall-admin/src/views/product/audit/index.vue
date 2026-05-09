<template>
  <div class="audit-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="关键词">
              <el-input v-model="searchForm.key" placeholder="SPU名称" clearable />
            </el-form-item>
            <el-form-item label="审核状态">
              <el-select v-model="searchForm.auditStatus" placeholder="请选择" clearable>
                <el-option label="未审核" :value="0" />
                <el-option label="审核通过" :value="1" />
                <el-option label="审核拒绝" :value="2" />
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
        <el-table-column prop="spuName" label="SPU名称" />
        <el-table-column prop="operateType" label="操作类型" />
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.auditStatus" type="audit" />
          </template>
        </el-table-column>
        <el-table-column prop="auditReason" label="审核原因" show-overflow-tooltip />
        <el-table-column prop="auditor" label="审核人" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
import { getAuditLogList } from '@/api/product/auditLog'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

interface AuditLog {
  spuName?: string
  operateType?: string
  auditStatus?: number
  auditReason?: string
  auditor?: string
  createTime?: string
}

const tableData = ref<AuditLog[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const searchForm = reactive({
  key: '',
  auditStatus: undefined as number | undefined
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getAuditLogList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Audit] Failed to load data:', error)
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.key = ''
  searchForm.auditStatus = undefined
  handleSearch()
}
</script>

<style scoped lang="scss">
.audit-container {
  .el-table {
    margin-top: 20px;
  }
}
</style>
