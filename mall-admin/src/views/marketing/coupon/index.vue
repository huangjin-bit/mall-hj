<template>
  <div class="coupon-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="关键词">
              <el-input v-model="searchForm.key" placeholder="优惠券名称" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
          <el-button type="primary" @click="handleAdd" v-hasPermi="['marketing:coupon:add']">新增优惠券</el-button>
        </div>
      </template>

      <el-table :data="tableData" border>
        <el-table-column prop="couponName" label="优惠券名称" />
        <el-table-column prop="couponType" label="类型" width="100">
          <template #default="{ row }">
            {{ row.couponType === 1 ? '满减券' : row.couponType === 2 ? '折扣券' : '立减券' }}
          </template>
        </el-table-column>
        <el-table-column label="优惠内容" width="150">
          <template #default="{ row }">
            <span v-if="row.couponType === 1">满{{ row.minAmount }}减{{ row.amount }}</span>
            <span v-else-if="row.couponType === 2">{{ (row.discount! * 10).toFixed(1) }}折</span>
            <span v-else>立减{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalCount" label="发行数量" width="100" />
        <el-table-column prop="remainCount" label="剩余数量" width="100" />
        <el-table-column prop="perLimit" label="每人限领" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <StatusTag :status="row.status" type="status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)" v-hasPermi="['marketing:coupon:edit']">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)" v-hasPermi="['marketing:coupon:delete']">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="优惠券名称" prop="couponName">
          <el-input v-model="form.couponName" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="优惠券类型" prop="couponType">
          <el-radio-group v-model="form.couponType">
            <el-radio :label="1">满减券</el-radio>
            <el-radio :label="2">折扣券</el-radio>
            <el-radio :label="3">立减券</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优惠金额" prop="amount" v-if="form.couponType !== 2">
          <el-input-number v-model="form.amount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="折扣率" prop="discount" v-if="form.couponType === 2">
          <el-input-number v-model="form.discount" :min="0.1" :max="1" :step="0.1" :precision="1" />
        </el-form-item>
        <el-form-item label="最低消费" prop="minAmount">
          <el-input-number v-model="form.minAmount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="发行数量" prop="totalCount">
          <el-input-number v-model="form.totalCount" :min="1" />
        </el-form-item>
        <el-form-item label="每人限领" prop="perLimit">
          <el-input-number v-model="form.perLimit" :min="1" />
        </el-form-item>
        <el-form-item label="有效期类型" prop="validType">
          <el-radio-group v-model="form.validType">
            <el-radio :label="1">固定期限</el-radio>
            <el-radio :label="2">相对期限</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="有效天数" prop="validDays" v-if="form.validType === 2">
          <el-input-number v-model="form.validDays" :min="1" />
          <span class="ml-10">领取后有效天数</span>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime" v-if="form.validType === 1">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime" v-if="form.validType === 1">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" />
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
import { getCouponList, saveCoupon, updateCoupon, deleteCoupon } from '@/api/seckill/coupon'
import StatusTag from '@/components/StatusTag.vue'
import Pagination from '@/components/Pagination.vue'

interface Coupon {
  id?: number
  couponName: string
  couponType: number
  amount?: number
  discount?: number
  minAmount: number
  totalCount: number
  remainCount?: number
  perLimit: number
  validType: number
  validDays?: number
  startTime?: string
  endTime?: string
  status?: number
}

const tableData = ref<Coupon[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const isEdit = ref(false)
const currentId = ref<number>()

const searchForm = reactive({
  key: ''
})

const form = reactive<Coupon>({
  couponName: '',
  couponType: 1,
  amount: 0,
  minAmount: 0,
  totalCount: 100,
  perLimit: 1,
  validType: 1
})

const rules: FormRules = {
  couponName: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
  couponType: [{ required: true, message: '请选择优惠券类型', trigger: 'change' }],
  minAmount: [{ required: true, message: '请输入最低消费', trigger: 'blur' }],
  totalCount: [{ required: true, message: '请输入发行数量', trigger: 'blur' }],
  perLimit: [{ required: true, message: '请输入每人限领数量', trigger: 'blur' }]
}

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    const { records, total: totalCount } = await getCouponList({
      ...searchForm,
      current: page.value,
      size: size.value
    })
    tableData.value = records
    total.value = totalCount
  } catch (error) {
    console.error('[Coupon] Failed to load data:', error)
  }
}

const handleSearch = () => {
  page.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.key = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增优惠券'
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row: Coupon) => {
  dialogTitle.value = '编辑优惠券'
  isEdit.value = true
  currentId.value = row.id
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = (row: Coupon) => {
  ElMessageBox.confirm('确定要删除该优惠券吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCoupon([row.id!])
      ElMessage.success('删除成功')
      await loadData()
    } catch (error) {
      console.error('[Coupon] Delete failed:', error)
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
          startTime: form.startTime ? new Date(form.startTime).toISOString() : undefined,
          endTime: form.endTime ? new Date(form.endTime).toISOString() : undefined
        }
        if (isEdit.value) {
          await updateCoupon({ ...data, id: currentId.value })
          ElMessage.success('更新成功')
        } else {
          await saveCoupon(data)
          ElMessage.success('保存成功')
        }
        dialogVisible.value = false
        await loadData()
      } catch (error) {
        console.error('[Coupon] Submit failed:', error)
      }
    }
  })
}

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    couponName: '',
    couponType: 1,
    amount: 0,
    minAmount: 0,
    totalCount: 100,
    perLimit: 1,
    validType: 1
  })
}
</script>
