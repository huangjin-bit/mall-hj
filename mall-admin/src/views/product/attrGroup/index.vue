<template>
  <div class="attr-group-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card>
          <template #header>
            <span>分类列表</span>
          </template>
          <el-tree
            :data="categoryTree"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            highlight-current
            @node-click="handleCategoryClick"
          />
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <template #header>
            <div class="flex-between">
              <span>属性分组</span>
              <el-button type="primary" @click="handleAdd" :disabled="!currentCategoryId" v-hasPermi="['product:attr-group:add']">新增分组</el-button>
            </div>
          </template>

          <el-table :data="tableData" border>
            <el-table-column prop="groupName" label="分组名称" />
            <el-table-column prop="sort" label="排序" width="80" />
            <el-table-column prop="descript" label="描述" show-overflow-tooltip />
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['product:attr-group:edit']">编辑</el-button>
                <el-button type="primary" link @click="handleManageAttrs(row)" v-hasPermi="['product:attr-group:list']">管理属性</el-button>
                <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['product:attr-group:delete']">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入分组名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="描述" prop="descript">
          <el-input v-model="form.descript" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <ImageUpload v-model="form.icon" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="attrDialogVisible"
      title="管理属性"
      width="800px"
      @close="loadData"
    >
      <div class="mb-20">
        <el-button type="primary" @click="handleAddAttr">添加属性</el-button>
      </div>
      <el-table :data="groupAttrs" border>
        <el-table-column prop="attrName" label="属性名称" />
        <el-table-column prop="attrType" label="属性类型" width="100">
          <template #default="{ row }">
            {{ row.attrType === 1 ? '基本属性' : '销售属性' }}
          </template>
        </el-table-column>
        <el-table-column prop="valueType" label="值类型" width="100">
          <template #default="{ row }">
            {{ row.valueType === 1 ? '手动输入' : '列表选择' }}
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleRemoveAttr(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="attrDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="addAttrDialogVisible"
      title="添加属性"
      width="600px"
      @close="loadNoAttrs"
    >
      <el-table
        :data="noAttrs"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="attrName" label="属性名称" />
        <el-table-column prop="attrType" label="属性类型" width="100">
          <template #default="{ row }">
            {{ row.attrType === 1 ? '基本属性' : '销售属性' }}
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="addAttrDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAddAttr">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance, FormRules } from 'element-plus'
import {
  getCategoryTree,
  getAttrGroupList,
  saveAttrGroup,
  updateAttrGroup,
  deleteAttrGroup,
  getGroupAttrs,
  getGroupNoAttrs,
  addAttrToGroup,
  removeAttrFromGroup
} from '@/api/product/attrGroup'
import ImageUpload from '@/components/ImageUpload.vue'

interface AttrGroup {
  id?: number
  categoryId?: number
  groupName: string
  sort: number
  descript?: string
  icon?: string
}

interface Attr {
  id?: number
  attrName: string
  attrType: number
  valueType: number
  sort: number
}

const categoryTree = ref<any[]>([])
const tableData = ref<AttrGroup[]>([])
const currentCategoryId = ref<number>()
const currentGroupId = ref<number>()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const attrDialogVisible = ref(false)
const addAttrDialogVisible = ref(false)
const groupAttrs = ref<Attr[]>([])
const noAttrs = ref<Attr[]>([])
const selectedAttrs = ref<Attr[]>([])

const form = reactive<AttrGroup>({
  groupName: '',
  sort: 0
})

const rules: FormRules = {
  groupName: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
}

onMounted(async () => {
  await loadCategoryTree()
})

const loadCategoryTree = async () => {
  try {
    const data = await getCategoryTree()
    categoryTree.value = data
  } catch (error) {
    console.error('[AttrGroup] Failed to load category tree:', error)
  }
}

const handleCategoryClick = (data: any) => {
  currentCategoryId.value = data.id
  loadData()
}

const loadData = async () => {
  if (!currentCategoryId.value) return

  try {
    const { records } = await getAttrGroupList({ categoryId: currentCategoryId.value })
    tableData.value = records
  } catch (error) {
    console.error('[AttrGroup] Failed to load data:', error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增分组'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: AttrGroup) => {
  dialogTitle.value = '编辑分组'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: AttrGroup) => {
  ElMessageBox.confirm('确定要删除该分组吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAttrGroup([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[AttrGroup] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...form, categoryId: currentCategoryId.value }
        if (isEdit.value) {
          await updateAttrGroup({ ...data, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveAttrGroup(data)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[AttrGroup] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    groupName: '',
    sort: 0
  })
}

const handleManageAttrs = async (row: AttrGroup) => {
  currentGroupId.value = row.id
  attrDialogVisible.value = true
  try {
    groupAttrs.value = await getGroupAttrs(row.id!)
  } catch (error) {
    console.error('[AttrGroup] Failed to load attrs:', error)
  }
}

const handleAddAttr = async () => {
  addAttrDialogVisible.value = true
  try {
    const { records } = await getGroupNoAttrs(currentGroupId.value!)
    noAttrs.value = records
  } catch (error) {
    console.error('[AttrGroup] Failed to load no attrs:', error)
  }
}

const loadNoAttrs = async () => {
  if (currentGroupId.value) {
    try {
      const { records } = await getGroupNoAttrs(currentGroupId.value)
      noAttrs.value = records
    } catch (error) {
      console.error('[AttrGroup] Failed to load no attrs:', error)
    }
  }
}

const handleSelectionChange = (val: Attr[]) => {
  selectedAttrs.value = val
}

const handleConfirmAddAttr = async () => {
  if (selectedAttrs.value.length === 0) {
    ElMessage.warning('请选择要添加的属性')
    return
  }

  try {
    await addAttrToGroup(currentGroupId.value!, selectedAttrs.value.map(item => item.id!))
    ElMessage.success('添加成功')
    addAttrDialogVisible.value = false
    groupAttrs.value = await getGroupAttrs(currentGroupId.value!)
  } catch (error) {
    console.error('[AttrGroup] Failed to add attrs:', error)
  }
}

const handleRemoveAttr = async (row: Attr) => {
  try {
    await removeAttrFromGroup(currentGroupId.value!, [row.id!])
    ElMessage.success('移除成功')
    groupAttrs.value = await getGroupAttrs(currentGroupId.value!)
  } catch (error) {
    console.error('[AttrGroup] Failed to remove attr:', error)
  }
}
</script>

<style scoped lang="scss">
.attr-group-container {
  :deep(.el-card__body) {
    padding: 20px;
  }

  .el-tree {
    max-height: 600px;
    overflow-y: auto;
  }
}
</style>
