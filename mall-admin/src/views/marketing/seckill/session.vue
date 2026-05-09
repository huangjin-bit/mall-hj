<template>
  <div class="seckill-session-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>秒杀场次</span>
          <el-button type="primary" @click="handleAdd" v-hasPermi="['marketing:seckill-session:add']">新增场次</el-button>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="name" label="场次名称" />
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="session" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['marketing:seckill-session:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['marketing:seckill-session:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="场次名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入场次名称" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { getSeckillSessionList, saveSeckillSession, updateSeckillSession, deleteSeckillSession } from '@/api/seckill/seckillSession'
import StatusTag from '@/components/StatusTag.vue'

interface SeckillSession {
  id?: number
  name: string
  startTime: string
  endTime: string
  sort: number
  status?: number
}

const tableData = ref<SeckillSession[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const form = reactive<SeckillSession>({
  name: '',
  startTime: '',
  endTime: '',
  sort: 0
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入场次名称', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
}

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records } = await getSeckillSessionList()
    tableData.value = records
  } catch (error) {
    console.error('[SeckillSession] Failed to load data:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增秒杀场次'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: SeckillSession) => {
  dialogTitle.value = '编辑秒杀场次'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: SeckillSession) => {
  ElMessageBox.confirm('确定要删除该秒杀场次吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSeckillSession([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[SeckillSession] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = {
          ...form,
          startTime: new Date(form.startTime).toISOString(),
          endTime: new Date(form.endTime).toISOString()
        }
        if (isEdit.value) {
          await updateSeckillSession({ ...data, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveSeckillSession(data)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[SeckillSession] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    name: '',
    startTime: '',
    endTime: '',
    sort: 0
  })
}
</script>
