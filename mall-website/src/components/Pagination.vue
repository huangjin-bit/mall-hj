<template>
  <div class="pagination">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  total: number
  page?: number
  size?: number
}>()

const emit = defineEmits<{
  change: [page: number, size: number]
}>()

const currentPage = ref(props.page || 1)
const pageSize = ref(props.size || 10)

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
  emit('change', currentPage.value, size)
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  emit('change', page, pageSize.value)
}

watch(() => props.page, (newPage) => {
  if (newPage) {
    currentPage.value = newPage
  }
})

watch(() => props.size, (newSize) => {
  if (newSize) {
    pageSize.value = newSize
  }
})
</script>

<style scoped lang="scss">
.pagination {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}
</style>
