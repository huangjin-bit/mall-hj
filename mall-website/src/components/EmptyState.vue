<template>
  <div class="empty-state">
    <el-icon :size="60" color="#999">
      <component :is="iconComponent" />
    </el-icon>
    <p class="empty-message">{{ message }}</p>
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Document, ShoppingCart, Box } from '@element-plus/icons-vue'

const props = withDefaults(
  defineProps<{
    message?: string
    icon?: 'document' | 'cart' | 'box'
  }>(),
  {
    message: '暂无数据',
    icon: 'box'
  }
)

const iconComponent = computed(() => {
  const icons = {
    document: Document,
    cart: ShoppingCart,
    box: Box
  }
  return icons[props.icon]
})
</script>

<style scoped lang="scss">
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #999;
}

.empty-message {
  margin: 20px 0 0 0;
  font-size: 16px;
}
</style>
