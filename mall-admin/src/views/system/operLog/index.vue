<template>
  <div class="oper-log-container">
    <el-card>
      <template #header>
        <el-form :inline="true" :model="queryForm">
          <el-form-item label="模块标题">
            <el-input v-model="queryForm.title" placeholder="请输入模块标题" clearable />
          </el-form-item>
          <el-form-item label="操作人员">
            <el-input v-model="queryForm.operUser" placeholder="请输入操作人员" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
              <el-option label="成功" :value="0" />
              <el-option label="失败" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </template>

      <el-table :data="tableData" border style="margin-top: 20px" v-loading="loading">
        <el-table-column prop="title" label="模块标题" width="150" />
        <el-table-column prop="operType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.operType === 1">新增</el-tag>
            <el-tag v-else-if="row.operType === 2" type="success">修改</el-tag>
            <el-tag v-else-if="row.operType === 3" type="danger">删除</el-tag>
            <el-tag v-else-if="row.operType === 4" type="warning">查询</el-tag>
            <el-tag v-else type="info">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operUserName" label="操作人员" width="120" />
        <el-table-column prop="operIp" label="操作IP" width="140" />
        <el-table-column prop="requestUrl" label="请求URL" width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'success' : 'danger'">
              {{ row.status === 0 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="costTime" label="耗时" width="100">
          <template #default="{ row }">
            {{ row.costTime }}ms
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- Detail Dialog -->
    <el-dialog v-model="detailVisible" title="操作日志详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="模块标题">{{ currentLog?.title }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">
          <el-tag v-if="currentLog?.operType === 1">新增</el-tag>
          <el-tag v-else-if="currentLog?.operType === 2" type="success">修改</el-tag>
          <el-tag v-else-if="currentLog?.operType === 3" type="danger">删除</el-tag>
          <el-tag v-else-if="currentLog?.operType === 4" type="warning">查询</el-tag>
          <el-tag v-else type="info">其他</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作人员">{{ currentLog?.operUserName }}</el-descriptions-item>
        <el-descriptions-item label="操作IP">{{ currentLog?.operIp }}</el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">{{ currentLog?.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="请求方式">{{ currentLog?.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentLog?.status === 0 ? 'success' : 'danger'">
            {{ currentLog?.status === 0 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog?.costTime }}ms</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentLog?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <pre style="max-height: 200px; overflow: auto; margin: 0;">{{ currentLog?.operParam }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="返回结果" :span="2" v-if="currentLog?.jsonResult">
          <pre style="max-height: 200px; overflow: auto; margin: 0;">{{ currentLog?.jsonResult }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2" v-if="currentLog?.errorMsg">
          <pre style="max-height: 200px; overflow: auto; margin: 0; color: #f56c6c;">{{ currentLog?.errorMsg }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getOperLogList } from '@/api/sysOperLog'

interface OperLog {
  id?: number
  title: string
  operType: number
  operUserName: string
  operIp: string
  requestUrl: string
  requestMethod: string
  status: number
  costTime: number
  createTime: string
  operParam?: string
  jsonResult?: string
  errorMsg?: string
}

const tableData = ref<OperLog[]>([])
const loading = ref(false)
const total = ref(0)
const detailVisible = ref(false)
const currentLog = ref<OperLog>()

const queryForm = reactive({
  title: '',
  operUser: '',
  status: undefined as number | undefined,
  page: 1,
  size: 10
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const result = await getOperLogList(queryForm)
    tableData.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('[OperLog] Failed to load data:', error)
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryForm.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(queryForm, {
    title: '',
    operUser: '',
    status: undefined,
    page: 1,
    size: 10
  })
  loadData()
}

const handleDetail = (row: OperLog) => {
  currentLog.value = row
  detailVisible.value = true
}
</script>

<style scoped lang="scss">
.oper-log-container {
  :deep(.el-pagination) {
    display: flex;
  }
}
</style>
