<template>
  <el-tag :type="tagInfo.type">{{ tagInfo.label }}</el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  orderStatusMap,
  publishStatusMap,
  auditStatusMap,
  statusMap,
  sessionStatusMap,
  returnStatusMap,
  purchaseStatusMap
} from '@/utils'

interface Props {
  status: number
  type: 'order' | 'publish' | 'audit' | 'status' | 'session' | 'return' | 'purchase'
}

const props = defineProps<Props>()

const statusMaps: Record<string, Record<number, { label: string; type: string }>> = {
  order: orderStatusMap,
  publish: publishStatusMap,
  audit: auditStatusMap,
  status: statusMap,
  session: sessionStatusMap,
  return: returnStatusMap,
  purchase: purchaseStatusMap
}

const tagInfo = computed(() => {
  const map = statusMaps[props.type] || statusMap
  return map[props.status] || { label: '未知', type: 'info' }
})
</script>
