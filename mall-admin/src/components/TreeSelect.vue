<template>
  <el-tree-select
    :model-value="modelValue"
    :data="categoryTree"
    :props="treeProps"
    :placeholder="placeholder"
    :render-after-expand="false"
    check-strictly
    node-key="id"
    @update:model-value="handleChange"
  />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCategoryTree } from '@/api/product/category'

interface Props {
  modelValue?: number
  placeholder?: string
}

interface Emits {
  (e: 'update:modelValue', value: number): void
}

defineProps<Props>()
const emit = defineEmits<Emits>()

const categoryTree = ref<any[]>([])
const treeProps = {
  label: 'name',
  children: 'children'
}

onMounted(async () => {
  try {
    const data = await getCategoryTree()
    categoryTree.value = data
  } catch (error) {
    console.error('[TreeSelect] Failed to load category tree:', error)
  }
})

const handleChange = (value: number) => {
  emit('update:modelValue', value)
}
</script>
