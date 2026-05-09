<template>
  <div class="cart-page">
    <div class="page-container">
      <h1 class="page-title">购物车</h1>

      <div v-if="cartStore.cartVO.items.length > 0">
        <!-- 购物车表格 -->
        <el-table :data="cartStore.cartVO.items" border style="margin-bottom: 20px">
          <el-table-column width="55" align="center">
            <template #default="{ row }">
              <el-checkbox v-model="row.checked" @change="handleCheckChange(row)" />
            </template>
          </el-table-column>
          <el-table-column label="商品信息" width="400">
            <template #default="{ row }">
              <div class="item-info">
                <img :src="row.skuImage || defaultImage" class="item-image" />
                <div class="item-details">
                  <div class="item-name">{{ row.spuName }}</div>
                  <div v-if="row.saleAttrs" class="item-attrs">
                    {{ formatSaleAttrs(row.saleAttrs) }}
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="单价" align="center" width="120">
            <template #default="{ row }">
              <span class="price">¥{{ row.price.toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" align="center" width="180">
            <template #default="{ row }">
              <el-input-number
                v-model="row.quantity"
                :min="1"
                :max="99"
                size="small"
                @change="handleQuantityChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="小计" align="center" width="120">
            <template #default="{ row }">
              <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="100">
            <template #default="{ row }">
              <el-button
                type="danger"
                link
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 底部操作栏 -->
        <div class="cart-footer">
          <div class="footer-left">
            <el-checkbox v-model="allChecked" @change="handleCheckAll">
              全选
            </el-checkbox>
            <el-button type="danger" link @click="handleDeleteChecked" :disabled="checkedCount === 0">
              删除选中
            </el-button>
          </div>
          <div class="footer-right">
            <div class="selected-info">
              已选 <span class="count">{{ checkedCount }}</span> 件商品
            </div>
            <div class="total-info">
              合计：<span class="total">¥{{ cartStore.totalPrice.toFixed(2) }}</span>
            </div>
            <el-button type="primary" size="large" @click="handleCheckout" :disabled="checkedCount === 0">
              去结算
            </el-button>
          </div>
        </div>
      </div>

      <EmptyState v-else message="购物车为空" icon="cart">
        <el-button type="primary" @click="router.push('/')">去逛逛</el-button>
      </EmptyState>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import EmptyState from '@/components/EmptyState.vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()

const defaultImage = 'https://via.placeholder.com/80x80?text=No+Image'

// 全选状态
const allChecked = computed(() => {
  return cartStore.cartVO.items.length > 0 &&
    cartStore.cartVO.items.every(item => item.checked)
})

// 已选商品数量
const checkedCount = computed(() => {
  return cartStore.checkedItems.length
})

// 格式化销售属性
const formatSaleAttrs = (saleAttrs: { attrName: string; attrValue: string }[]) => {
  return saleAttrs.map(attr => `${attr.attrName}: ${attr.attrValue}`).join('; ')
}

// 处理单个商品选中状态变化
const handleCheckChange = (row: any) => {
  cartStore.checkItem(row.skuId, row.checked)
}

// 处理全选
const handleCheckAll = (checked: boolean) => {
  cartStore.checkAll(checked)
}

// 处理数量变化
const handleQuantityChange = (row: any) => {
  cartStore.updateQuantity(row.skuId, row.quantity)
}

// 删除单个商品
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
      type: 'warning'
    })
    await cartStore.deleteItems([row.skuId])
    ElMessage.success('删除成功')
  } catch (error) {
    // 用户取消删除
  }
}

// 删除选中的商品
const handleDeleteChecked = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${checkedCount.value} 件商品吗？`, '提示', {
      type: 'warning'
    })
    const skuIds = cartStore.checkedItems.map(item => item.skuId)
    await cartStore.deleteItems(skuIds)
    ElMessage.success('删除成功')
  } catch (error) {
    // 用户取消删除
  }
}

// 去结算
const handleCheckout = () => {
  const skuIds = cartStore.checkedItems.map(item => item.skuId)
  router.push({
    name: 'OrderConfirm',
    query: {
      skuIds: skuIds.join(',')
    }
  })
}

// 初始化加载购物车
cartStore.fetchCart()
</script>

<style scoped lang="scss">
.cart-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

.item-info {
  display: flex;
  gap: 15px;
  padding: 10px 0;

  .item-image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 4px;
  }

  .item-details {
    flex: 1;

    .item-name {
      font-size: 14px;
      color: #333;
      margin-bottom: 5px;
      line-height: 1.4;
      max-height: 40px;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .item-attrs {
      font-size: 12px;
      color: #999;
    }
  }
}

.price,
.subtotal {
  color: #f56c6c;
  font-weight: 500;
}

.cart-footer {
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 20px;

  .selected-info {
    font-size: 14px;
    color: #666;

    .count {
      color: #f56c6c;
      font-weight: 500;
    }
  }

  .total-info {
    font-size: 14px;
    color: #666;

    .total {
      color: #f56c6c;
      font-size: 20px;
      font-weight: 500;
    }
  }
}
</style>
