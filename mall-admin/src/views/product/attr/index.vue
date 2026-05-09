<template>
  <div class="attr-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="分类">
              <TreeSelect v-model="searchForm.categoryId" placeholder="请选择分类" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
            </el-form-item>
          </el-form>
          <el-button type="primary" @click="handleAdd" v-hasPermi="['product:attr:add']">新增属性</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="基本属性" name="base">
          <AttrTable
            :data="tableData"
            @edit="handleEdit"
            @delete="handleDelete"
          />
        </el-tab-pane>
        <el-tab-pane label="销售属性" name="sale">
          <AttrTable
            :data="tableData"
            @edit="handleEdit"
            @delete="handleDelete"
          />
        </el-tab-pane>
      </el-tabs>

      <Pagination
        v-model:page="page"
        v-model:size="size"
        :total="total"
        @update:page="loadData"
        @update:size="loadData"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="属性名称" prop="attrName">
          <el-input v-model="form.attrName" placeholder="请输入属性名称" />
        </el-form-item>
        <el-form-item label="属性类型" prop="attrType">
          <el-radio-group v-model="form.attrType">
            <el-radio :label="1">基本属性</el-radio>
            <el-radio :label="2">销售属性</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="值类型" prop="valueType">
          <el-radio-group v-model="form.valueType">
            <el-radio :label="1">手动输入</el-radio>
            <el-radio :label="2">列表选择</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="可选值" prop="valueSelect" v-if="form.valueType === 2">
          <el-input
            v-model="form.valueSelect"
            type="textarea"
            :rows="3"
            placeholder="多个值用逗号分隔，例如：红色,蓝色,绿色"
          />
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
import { getAttrList, saveAttr, updateAttr, deleteAttr } from '@/api/product/attr'
import TreeSelect from '@/components/TreeSelect.vue'
import Pagination from '@/components/Pagination.vue'

interface Attr {
  id?: number
  attrName: string
  attrType: number
  valueType: number
  valueSelect?: string
  sort: number
}

const tableData = ref<Attr[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()
const activeTab = ref('base')

const searchForm = reactive({
  categoryId: undefined as number | undefined
})

const form = reactive<Attr>({
  attrName: '',
  attrType: 1,
  valueType: 1,
  sort: 0
})

const rules: FormRules = {
  attrName: [{ required: true, message: '请输入属性名称', trigger: 'blur' }],
  attrType: [{ required: true, message: '请选择属性类型', trigger: 'change' }],
  valueType: [{ required: true, message: '请选择值类型', trigger: 'change' }]
}

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const attrType = activeTab.value === 'base' ? 1 : 2
    const { records, total: totalCount } = await getAttrList({
      ...searchForm,
      attrType,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Attr] Failed to load data:', error)
  }
}

const handleTabChange = () => {
  page.value = 1
  loadData()
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增属性'
  isEdit.value = false
  form.attrType = activeTab.value === 'base' ? 1 : 2
  dialogVisible.value = true
}

const handleEdit = (row: Attr) => {
  dialogTitle.value = '编辑属性'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: Attr) => {
  ElMessageBox.confirm('确定要删除该属性吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAttr([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[Attr] Delete failed:', error)
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...form, categoryId: searchForm.categoryId }
        if (isEdit.value) {
          await updateAttr({ ...data, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveAttr(data)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[Attr] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    attrName: '',
    attrType: 1,
    valueType: 1,
    sort: 0
  })
}
</script>

<script lang="ts">
// 属性表格组件
import { defineComponent } from 'vue'

const AttrTable = defineComponent({
  props: {
    data: Array
  },
  emits: ['edit', 'delete'],
  template: `
    <el-table :data="data" border>
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
      <el-table-column prop="valueSelect" label="可选值" show-overflow-tooltip />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="$emit('edit', row)" v-hasPermi="['product:attr:edit']">编辑</el-button>
          <el-button type="danger" link @click="$emit('delete', row)" v-hasPermi="['product:attr:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  `
})

export { AttrTable }
</script>

<style scoped lang="scss">
.attr-container {
  :deep(.el-tabs__content) {
    padding-top: 20px;
  }
}
</style>
