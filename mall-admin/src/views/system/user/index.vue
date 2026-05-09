<template>
  <div class="user-container">
    <el-card>
      <template #header>
        <el-form :inline="true" :model="queryForm">
          <el-form-item label="用户名">
            <el-input v-model="queryForm.username" placeholder="请输入用户名" clearable />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="queryForm.phone" placeholder="请输入手机号" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </template>

      <el-button type="primary" @click="handleAdd" v-hasPermi="['system:user:add']">新增</el-button>

      <el-table :data="tableData" border style="margin-top: 20px" v-loading="loading">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['system:user:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['system:user:delete']">删除</el-button>
            <el-button type="warning" link @click="handleResetPassword(row)" v-hasPermi="['system:user:resetPwd']">重置密码</el-button>
            <el-button type="success" link @click="handleAssignRoles(row)" v-hasPermi="['system:user:assignRole']">分配角色</el-button>
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

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="0">保密</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <el-tree-select
            v-model="form.deptId"
            :data="deptTree"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择部门"
            clearable
            check-strictly
          />
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-checkbox-group v-model="form.roleIds">
            <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- Assign Roles Dialog -->
    <el-dialog v-model="roleDialogVisible" title="分配角色" width="400px">
      <el-checkbox-group v-model="selectedRoleIds">
        <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">
          {{ role.roleName }}
        </el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRoles">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import { getUserList, saveUser, updateUser, deleteUser, resetPassword, assignRoles, getAllRoles } from '@/api/sysUser'
import { getDeptTree } from '@/api/sysDept'

interface User {
  id?: number
  username: string
  nickname: string
  email?: string
  phone?: string
  gender: number
  status: number
  deptId?: number
  password?: string
  roleIds?: number[]
}

const tableData = ref<User[]>([])
const loading = ref(false)
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const roleDialogVisible = ref(false)
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()
const selectedRoleIds = ref<number[]>([])
const currentUserId = ref<number>()
const deptTree = ref<any[]>([])
const allRoles = ref<any[]>([])

const queryForm = reactive({
  username: '',
  phone: '',
  status: undefined as number | undefined,
  page: 1,
  size: 10
})

const form = reactive<User>({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: 0,
  status: 1,
  deptId: undefined,
  password: '',
  roleIds: []
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}

onMounted(async () => {
  await Promise.all([loadData(), loadDeptTree(), loadAllRoles()])
})

const loadData = async () => {
  loading.value = true
  try {
    const result = await getUserList(queryForm)
    tableData.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('[User] Failed to load data:', error)
  } finally {
    loading.value = false
  }
}

const loadDeptTree = async () => {
  try {
    const data = await getDeptTree()
    deptTree.value = data || []
  } catch (error) {
    console.error('[User] Failed to load dept tree:', error)
  }
}

const loadAllRoles = async () => {
  try {
    const data = await getAllRoles()
    allRoles.value = data || []
  } catch (error) {
    console.error('[User] Failed to load roles:', error)
  }
}

const handleQuery = () => {
  queryForm.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(queryForm, {
    username: '',
    phone: '',
    status: undefined,
    page: 1,
    size: 10
  })
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: User) => {
  dialogTitle.value = '编辑用户'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: User) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id!)
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[User] Delete failed:', error)
    }
  })
}

const handleResetPassword = (row: User) => {
  ElMessageBox.prompt('请输入新密码', '重置密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '密码不能为空'
  }).then(async ({ value }) => {
    try {
      await resetPassword(row.id!, value)
      ElMessage.success('密码重置成功')
    } catch (error) {
      console.error('[User] Reset password failed:', error)
    }
  })
}

const handleAssignRoles = async (row: User) => {
  currentUserId.value = row.id
  selectedRoleIds.value = row.roleIds || []
  roleDialogVisible.value = true
}

const handleSaveRoles = async () => {
  try {
    await assignRoles(currentUserId.value!, selectedRoleIds.value)
    ElMessage.success('角色分配成功')
    roleDialogVisible.value = false
    await loadData()
  } catch (error) {
    console.error('[User] Assign roles failed:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateUser({ ...form, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveUser(form)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[User] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    username: '',
    nickname: '',
    email: '',
    phone: '',
    gender: 0,
    status: 1,
    deptId: undefined,
    password: '',
    roleIds: []
  })
}
</script>

<style scoped lang="scss">
.user-container {
  :deep(.el-pagination) {
    display: flex;
  }
}
</style>
