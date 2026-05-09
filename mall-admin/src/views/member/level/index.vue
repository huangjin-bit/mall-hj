<template>
  <div class="member-level-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>会员等级</span>
          <el-button type="primary" @click="handleAdd" v-hasPermi="['member:level:add']">新增等级</el-button>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="name" label="等级名称" />
        <el-table-column prop="growthPoint" label="所需成长值" />
        <el-table-column prop="discount" label="折扣率" width="100">
          <template #default="{ row }">
            {{ (row.discount * 10).toFixed(1) }}折
          </template>
        </el-table-column>
        <el-table-column prop="defaultStatus" label="是否默认" width="100">
          <template #default="{ row }">
            <el-tag :type="row.defaultStatus === 1 ? 'success' : 'info'">
              {{ row.defaultStatus === 1 ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priviledge" label="特权" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['member:level:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['member:level:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="等级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入等级名称" />
        </el-form-item>
        <el-form-item label="所需成长值" prop="growthPoint">
          <el-input-number v-model="form.growthPoint" :min="0" />
        </el-form-item>
        <el-form-item label="折扣率" prop="discount">
          <el-input-number v-model="form.discount" :min="0.1" :max="1" :step="0.1" :precision="1" />
        </el-form-item>
        <el-form-item label="是否默认" prop="defaultStatus">
          <el-radio-group v-model="form.defaultStatus">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="特权" prop="priviledge">
          <el-input v-model="form.priviledge" type="textarea" :rows="3" placeholder="请输入会员特权" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea" :rows="2" placeholder="请输入备注" />
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
import { getMemberLevelList, saveMemberLevel, updateMemberLevel, deleteMemberLevel } from '@/api/member/memberLevel'

interface MemberLevel {
  id?: number
  name: string
  growthPoint: number
  discount: number
  defaultStatus: number
  priviledge?: string
  note?: string
}

const tableData = ref<MemberLevel[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const form = reactive<MemberLevel>({
  name: '',
  growthPoint: 0,
  discount: 1.0,
  defaultStatus: 0
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入等级名称', trigger: 'blur' }],
  growthPoint: [{ required: true, message: '请输入所需成长值', trigger: 'blur' }],
  discount: [{ required: true, message: '请输入折扣率', trigger: 'blur' }]
}

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records } = await getMemberLevelList()
    tableData.value = records
  } catch (error) {
    console.error('[MemberLevel] Failed to load data:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增会员等级'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: MemberLevel) => {
  dialogTitle.value = '编辑会员等级'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: MemberLevel) => {
  ElMessageBox.confirm('确定要删除该会员等级吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMemberLevel([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[MemberLevel] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateMemberLevel({ ...form, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveMemberLevel(form)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[MemberLevel] Submit failed:', error)
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
    growthPoint: 0,
    discount: 1.0,
    defaultStatus: 0
  })
}
</script>
