<template>
  <div class="spu-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="关键词">
              <el-input v-model="searchForm.key" placeholder="SPU名称" clearable />
            </el-form-item>
            <el-form-item label="发布状态">
              <el-select v-model="searchForm.publishStatus" placeholder="请选择" clearable>
                <el-option label="未发布" :value="0" />
                <el-option label="已发布" :value="1" />
                <el-option label="已下架" :value="2" />
              </el-select>
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
          <el-button type="primary" @click="handleAdd" v-hasPermi="['product:spu:add']">新增SPU</el-button>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="spuName" label="SPU名称" />
        <el-table-column prop="categoryName" label="分类" />
        <el-table-column prop="brandName" label="品牌" />
        <el-table-column prop="publishStatus" label="发布状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.publishStatus" type="publish" />
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.auditStatus" type="audit" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)" v-hasPermi="['product:spu:list']">详情</el-button>
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['product:spu:edit']">编辑</el-button>
            <el-button
              :type="row.publishStatus === 1 ? 'warning' : 'success'"
              link
              @click="handlePublish(row)"
              v-hasPermi="['product:spu:publish']"
            >
              {{ row.publishStatus === 1 ? '下架' : '发布' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['product:spu:delete']">删除</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSpuList, deleteSpu, publishSpu } from '@/api/product/spu'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

interface Spu {
  id?: number
  spuName: string
  categoryName?: string
  brandName?: string
  publishStatus?: number
  auditStatus?: number
  createTime?: string
}

const router = useRouter()
const tableData = ref<Spu[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)

const searchForm = reactive({
  key: '',
  publishStatus: undefined as number | undefined,
  auditStatus: undefined as number | undefined
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getSpuList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[SPU] Failed to load data:', error)
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.key = ''
  searchForm.publishStatus = undefined
  searchForm.auditStatus = undefined
  handleSearch()
}

const handleAdd = () => {
  router.push('/product/spu/add')
}

const handleView = (row: Spu) => {
  router.push(`/product/spu/detail/${row.id}`)
}

const handleEdit = (row: Spu) => {
  router.push(`/product/spu/edit/${row.id}`)
}

const handlePublish = (row: Spu) => {
  const status = row.publishStatus === 1 ? 2 : 1
  const action = status === 1 ? '发布' : '下架'

  ElMessageBox.confirm(`确定要${action}该SPU吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await publishSpu(row.id!, status)
      ElMessage.success(`${action}成功`)
      await loadData()
    } catch (error) {
      console.error('[SPU] Publish failed:', error)
    }
  })
}

const handleDelete = (row: Spu) => {
  ElMessageBox.confirm('确定要删除该SPU吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSpu([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[SPU] Delete failed:', error)
    }
  })
}
</script>

<style scoped lang="scss">
.spu-container {
  .el-table {
    margin-top: 20px;
  }
}
</style>
