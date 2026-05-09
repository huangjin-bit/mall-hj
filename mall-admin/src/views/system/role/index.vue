<template>
  <div class="role-container">
    <el-card>
      <el-button type="primary" @click="handleAdd" v-hasPermi="['system:role:add']">新增</el-button>

      <el-table :data="tableData" border style="margin-top: 20px" v-loading="loading">
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleKey" label="角色权限字符串" width="200" />
        <el-table-column prop="sort" label="显示顺序" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['system:role:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['system:role:delete']">删除</el-button>
            <el-button type="success" link @click="handleAssignMenus(row)" v-hasPermi="['system:role:assignMenu']">分配菜单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限字符串" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入权限字符串" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="sort">
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

    <!-- Assign Menus Dialog -->
    <el-dialog v-model="menuDialogVisible" title="分配菜单" width="500px">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        :props="{ label: 'name', children: 'children' }"
        node-key="id"
        show-checkbox
        default-expand-all
      />
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveMenus">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { getRoleList, saveRole, updateRole, deleteRole, getRoleMenus, assignRoleMenus } from '@/api/sysRole'
import { getMenuTree } from '@/api/sysMenu'
import type { ElTree } from 'element-plus'

interface Role {
  id?: number
  roleName: string
  roleKey: string
  sort: number
  status: number
}

const tableData = ref<Role[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const menuDialogVisible = ref(false)
const formRef = ref<FormInstance>()
const menuTreeRef = ref<InstanceType<typeof ElTree>>()
const isEdit = ref(false)
const currentId = ref<number>()
const menuTree = ref<any[]>([])

const form = reactive<Role>({
  roleName: '',
  roleKey: '',
  sort: 0,
  status: 1
})

const rules: FormRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleKey: [{ required: true, message: '请输入权限字符串', trigger: 'blur' }]
}

onMounted(async () => {
  await Promise.all([loadData(), loadMenuTree()])
})

const loadData = async () => {
  loading.value = true
  try {
    const data = await getRoleList({})
    tableData.value = data || []
  } catch (error) {
    console.error('[Role] Failed to load data:', error)
  } finally {
    loading.value = false
  }
}

const loadMenuTree = async () => {
  try {
    const data = await getMenuTree()
    menuTree.value = data || []
  } catch (error) {
    console.error('[Role] Failed to load menu tree:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增角色'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: Role) => {
  dialogTitle.value = '编辑角色'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: Role) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id!)
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[Role] Delete failed:', error)
    }
  })
}

const handleAssignMenus = async (row: Role) => {
  currentId.value = row.id
  menuDialogVisible.value = true
  try {
    const menuIds = await getRoleMenus(row.id!)
    setTimeout(() => {
      menuTreeRef.value?.setCheckedKeys(menuIds || [])
    }, 100)
  } catch (error) {
    console.error('[Role] Failed to load role menus:', error)
  }
}

const handleSaveMenus = async () => {
  const checkedKeys = menuTreeRef.value?.getCheckedKeys() || []
  const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() || []
  const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys] as number[]
  try {
    await assignRoleMenus(currentId.value!, allCheckedKeys)
    ElMessage.success('菜单分配成功')
    menuDialogVisible.value = false
  } catch (error) {
    console.error('[Role] Assign menus failed:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateRole({ ...form, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveRole(form)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[Role] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    roleName: '',
    roleKey: '',
    sort: 0,
    status: 1
  })
}
</script>

<style scoped lang="scss">
.role-container {
  :deep(.el-tree) {
    max-height: 400px;
    overflow-y: auto;
  }
}
</style>
