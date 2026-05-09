<template>
  <div class="sku-selector">
    <div v-for="attr in saleAttrs" :key="attr.attrName" class="attr-group">
      <div class="attr-name">{{ attr.attrName }}</div>
      <div class="attr-values">
        <el-button
          v-for="value in attr.attrValues"
          :key="value"
          :type="isAttrSelected(attr.attrName, value) ? 'primary' : 'default'"
          :disabled="!isAttrValueAvailable(attr.attrName, value)"
          size="small"
          @click="selectAttrValue(attr.attrName, value)"
        >
          {{ value }}
        </el-button>
      </div>
    </div>

    <div v-if="selectedSku" class="sku-info">
      <div class="info-item">
        <span class="label">价格：</span>
        <span class="price">¥{{ selectedSku.price.toFixed(2) }}</span>
      </div>
      <div class="info-item">
        <span class="label">库存：</span>
        <span class="stock">{{ selectedSku.stock }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { Sku } from '@/api/product/spu'

const props = defineProps<{
  skus: Sku[]
  saleAttrs: { attrName: string; attrValues: string[] }[]
}>()

const emit = defineEmits<{
  select: [skuId: number]
}>()

// 已选属性
const selectedAttrs = ref<Record<string, string>>({})

// 当前选中的 SKU
const selectedSku = computed(() => {
  if (Object.keys(selectedAttrs.value).length === 0) {
    return null
  }

  return props.skus.find(sku => {
    if (!sku.saleAttrs) return false

    const skuAttrs = Object.fromEntries(
      sku.saleAttrs.map(attr => [attr.attrName, attr.attrValue])
    )

    return Object.entries(selectedAttrs.value).every(([key, value]) => {
      return skuAttrs[key] === value
    })
  }) || null
})

// 判断属性值是否被选中
const isAttrSelected = (attrName: string, attrValue: string) => {
  return selectedAttrs.value[attrName] === attrValue
}

// 判断属性值是否可选
const isAttrValueAvailable = (attrName: string, attrValue: string) => {
  // 找到至少一个 SKU 包含这个属性值
  return props.skus.some(sku => {
    if (!sku.saleAttrs) return false

    const hasThisAttr = sku.saleAttrs.some(
      attr => attr.attrName === attrName && attr.attrValue === attrValue
    )

    if (!hasThisAttr) return false

    // 检查其他已选属性是否兼容
    const otherSelectedAttrs = Object.entries(selectedAttrs.value)
      .filter(([key]) => key !== attrName)

    if (otherSelectedAttrs.length === 0) return true

    const skuAttrs = Object.fromEntries(
      sku.saleAttrs.map(attr => [attr.attrName, attr.attrValue])
    )

    return otherSelectedAttrs.every(([key, value]) => {
      return skuAttrs[key] === value
    })
  })
}

// 选择属性值
const selectAttrValue = (attrName: string, attrValue: string) => {
  selectedAttrs.value = {
    ...selectedAttrs.value,
    [attrName]: attrValue
  }
}

// 监听选中的 SKU 变化
watch(selectedSku, (newSku) => {
  if (newSku) {
    emit('select', newSku.id)
  }
}, { immediate: true })
</script>

<style scoped lang="scss">
.sku-selector {
  padding: 20px 0;
}

.attr-group {
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.attr-name {
  font-size: 14px;
  color: #333;
  margin-bottom: 10px;
  font-weight: 500;
}

.attr-values {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.sku-info {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 30px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 14px;

  .label {
    color: #666;
    margin-right: 5px;
  }

  .price {
    color: #f56c6c;
    font-size: 24px;
    font-weight: 500;
  }

  .stock {
    color: #67c23a;
    font-size: 16px;
  }
}
</style>
