<template>
  <div class="return-apply-detail-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>退货详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ returnData.orderSn }}</el-descriptions-item>
        <el-descriptions-item label="会员">{{ returnData.username }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ returnData.skuName }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          {{ returnData.type === 1 ? '退款' : '退货退款' }}
        </el-descriptions-item>
        <el-descriptions-item label="退款金额">¥{{ returnData.amount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <StatusTag :status="returnData.status" type="return" />
        </el-descriptions-item>
        <el-descriptions-item label="退货原因" :span="2">{{ returnData.reason }}</el-descriptions-item>
        <el-descriptions-item label="详细说明" :span="2">{{ returnData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ returnData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ returnData.auditTime || '-' }}</el-descriptions-item>
      </el-descriptions>

      <div class="mt-20" v-if="returnData.status === 1">
        <el-button type="primary" @click="handleAudit(2)">审核通过</el-button>
        <el-button type="danger" @click="handleAudit(3)">审核拒绝</el-button>
      </div>

      <div class="mt-20" v-if="returnData.status === 2">
        <el-button type="primary" @click="handleRefund">确认退款</el-button>
      </div>
    </el-card>

    <el-dialog v-model="auditDialogVisible" title="审核" width="500px">
      <el-form label-width="100px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="3">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="auditForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderReturnById, auditOrderReturn, confirmRefund } from '@/api/order/orderReturn'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const router = useRouter()
const returnData = ref<any>({})
const auditDialogVisible = ref(false)
const auditForm = reactive({
  status: 2,
  remark: ''
})

onMounted(async () => {
  try {
    returnData.value = await getOrderReturnById(Number(route.params.id))
  } catch (error) {
    console.error('[ReturnApply Detail] Failed to load data:', error)
  }
})

const handleAudit = (status: number) => {
  auditForm.status = status
  auditDialogVisible.value = true
}

const handleConfirmAudit = async () => {
  try {
    await auditOrderReturn(returnData.value.id, auditForm.status, auditForm.remark)
    ElMessage.success('审核成功')
    auditDialogVisible.value = false
    returnData.value = await getOrderReturnById(Number(route.params.id))
  } catch (error) {
    console.error('[ReturnApply Detail] Failed to audit:', error)
  }
}

const handleRefund = async () => {
  ElMessageBox.confirm('确定要确认退款吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await confirmRefund(returnData.value.id)
      ElMessage.success('退款成功')
      returnData.value = await getOrderReturnById(Number(route.params.id))
    } catch (error) {
      console.error('[ReturnApply Detail] Failed to refund:', error)
    }
  })
}
</script>
