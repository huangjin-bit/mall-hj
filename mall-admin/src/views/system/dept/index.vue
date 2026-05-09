<template>
  <div class="dept-container">
    <el-card>
      <el-button type="primary" @click="handleAdd" v-hasPermi="['system:dept:add']">新增</el-button>

      <el-table
        :data="tableData"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        border
        style="margin-top: 20px"
        v-loading="loading"
      >
        <el-table-column prop="name" label="部门名称" width="200" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['system:dept:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['system:dept:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="deptTreeOptions"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择上级部门"
            clearable
            check-strictly
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="显示排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="部门状态" prop="status">
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { getDeptTree, saveDept, updateDept, deleteDept } from '@/api/sysDept'

interface Dept {
  id?: number
  parentId?: number
  name: string
  sort: number
  status: number
  children?: Dept[]
}

const tableData = ref<Dept[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const form = reactive<Dept>({
  parentId: 0,
  name: '',
  sort: 0,
  status: 1
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }]
}

const deptTreeOptions = computed(() => {
  const buildTree = (depts: Dept[], parentId: number = 0): Dept[] => {
    return depts
      .filter(dept => dept.parentId === parentId)
      .map(dept => ({
        ...dept,
        children: buildTree(depts, dept.id!)
      }))
  }
  return buildTree(tableData.value)
})

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const data = await getDeptTree()
    tableData.value = data || []
  } catch (error) {
    console.error('[Dept] Failed to load data:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增部门'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: Dept) => {
  dialogTitle.value = '编辑部门'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: Dept) => {
  ElMessageBox.confirm('确定要删除该部门吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDept(row.id!)
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[Dept] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateDept({ ...form, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveDept(form)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[Dept] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    parentId: 0,
    name: '',
    sort: 0,
    status: 1
  })
}
</script>

<style scoped lang="scss">
.dept-container {
  :deep(.el-table) {
    margin-top: 20px;
  }
}
</style>
