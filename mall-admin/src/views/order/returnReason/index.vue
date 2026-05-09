<template>
  <div class="return-reason-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>退货原因</span>
          <el-button type="primary" @click="handleAdd" v-hasPermi="['order:return-reason:add']">新增原因</el-button>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="reason" label="原因" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['order:return-reason:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['order:return-reason:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="原因" prop="reason">
          <el-input v-model="form.reason" placeholder="请输入退货原因" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
import { getOrderReturnReasonList, saveOrderReturnReason, updateOrderReturnReason, deleteOrderReturnReason } from '@/api/order/orderReturnReason'
import StatusTag from '@/components/StatusTag.vue'

interface ReturnReason {
  id?: number
  reason: string
  sort: number
  status: number
}

const tableData = ref<ReturnReason[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const form = reactive<ReturnReason>({
  reason: '',
  sort: 0,
  status: 1
})

const rules: FormRules = {
  reason: [{ required: true, message: '请输入退货原因', trigger: 'blur' }]
}

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records } = await getOrderReturnReasonList()
    tableData.value = records
  } catch (error) {
    console.error('[ReturnReason] Failed to load data:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增退货原因'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: ReturnReason) => {
  dialogTitle.value = '编辑退货原因'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: ReturnReason) => {
  ElMessageBox.confirm('确定要删除该退货原因吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteOrderReturnReason([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[ReturnReason] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateOrderReturnReason({ ...form, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveOrderReturnReason(form)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[ReturnReason] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    reason: '',
    sort: 0,
    status: 1
  })
}
</script>
