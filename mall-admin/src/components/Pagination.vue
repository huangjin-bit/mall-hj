<template>
  <el-pagination
    :current-page="page"
    :page-size="size"
    :total="total"
    :page-sizes="[10, 20, 50, 100]"
    layout="total, sizes, prev, pager, next, jumper"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  />
</template>

<script setup lang="ts">
interface Props {
  total: number
  page: number
  size: number
}

interface Emits {
  (e: 'update:page', page: number): void
  (e: 'update:size', size: number): void
}

defineProps<Props>()
const emit = defineEmits<Emits>()

const handleSizeChange = (val: number) => {
  emit('update:size', val)
  emit('update:page', 1) // 切换每页条数时重置到第一页
}

const handleCurrentChange = (val: number) => {
  emit('update:page', val)
}
</script>

<style scoped>
.el-pagination {
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
