<template>
  <div class="member-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="关键词">
              <el-input v-model="searchForm.key" placeholder="用户名/手机号" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="levelName" label="会员等级" />
        <el-table-column prop="integration" label="积分" width="100" />
        <el-table-column prop="growth" label="成长值" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="status" />
          </template>
        </el-table-column>
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
import { getMemberList } from '@/api/member/member'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

interface Member {
  id?: number
  username?: string
  phone?: string
  nickname?: string
  levelName?: string
  integration: number
  growth: number
  status: number
}

const router = useRouter()
const tableData = ref<Member[]>([])
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
    const { records, total: totalCount } = await getMemberList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Member] Failed to load data:', error)
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

const handleView = (row: Member) => {
  router.push(`/member/member/detail/${row.id}`)
}
</script>

<style scoped lang="scss">
.member-container {
  .el-table {
    margin-top: 20px;
  }
}
</style>
