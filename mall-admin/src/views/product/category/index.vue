<template>
  <div class="category-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>分类管理</span>
          <el-button type="primary" @click="handleAdd" v-hasPermi="['product:category:add']">新增分类</el-button>
        </div>
      </template>

      <el-table
        :data="tableData"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        border
      >
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="level" label="级别" width="80">
          <template #default="{ row }">
            {{ getLevelName(row.level) }}
          </template>
        </el-table-column>
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="{ row }">
            <el-image v-if="row.icon" :src="row.icon" style="width: 40px; height: 40px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['product:category:edit']">编辑</el-button>
            <el-button type="primary" link @click="handleAddChild(row)" v-hasPermi="['product:category:add']">新增子分类</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['product:category:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级分类" prop="parentId">
          <TreeSelect v-model="form.parentId" placeholder="请选择上级分类（不选则为顶级分类）" />
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="级别" prop="level">
          <el-input-number v-model="form.level" :min="1" :max="3" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <ImageUpload v-model="form.icon" />
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
import { getCategoryTree, saveCategory, updateCategory, deleteCategory } from '@/api/product/category'
import StatusTag from '@/components/StatusTag.vue'
import TreeSelect from '@/components/TreeSelect.vue'
import ImageUpload from '@/components/ImageUpload.vue'

interface Category {
  id?: number
  parentId?: number
  name: string
  level: number
  icon?: string
  sort: number
  status: number
  children?: Category[]
}

const tableData = ref<Category[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const form = reactive<Category>({
  name: '',
  level: 1,
  sort: 0,
  status: 1
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  level: [{ required: true, message: '请选择级别', trigger: 'change' }]
}

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const data = await getCategoryTree()
    tableData.value = data
  } catch (error) {
    console.error('[Category] Failed to load data:', error)
  }
}

const getLevelName = (level: number) => {
  const map: Record<number, string> = {
    1: '一级',
    2: '二级',
    3: '三级'
  }
  return map[level] || '未知'
}

const handleAdd = () => {
  dialogTitle.value = '新增分类'
  isEdit.value = false
  dialogVisible.value = true
}

const handleAddChild = (row: Category) => {
  dialogTitle.value = '新增子分类'
  isEdit.value = false
  form.parentId = row.id
  form.level = row.level + 1
  dialogVisible.value = true
}

const handleEdit = (row: Category) => {
  dialogTitle.value = '编辑分类'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: Category) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[Category] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateCategory({ ...form, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveCategory(form)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[Category] Submit failed:', error)
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
    level: 1,
    sort: 0,
    status: 1
  })
}
</script>

<style scoped lang="scss">
.category-container {
  .el-table {
    margin-top: 20px;
  }
}
</style>
