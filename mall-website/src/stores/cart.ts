import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  getCart,
  addToCart,
  updateCartItem,
  checkCartItem,
  checkAllCart,
  deleteCartItems
} from '@/api/cart'
import type { CartVO, CartItem } from '@/api/cart'

export const useCartStore = defineStore('cart', () => {
  // 状态
  const cartVO = ref<CartVO>({ items: [], totalCount: 0, totalPrice: 0 })

  // 计算属性
  const totalCount = computed(() => cartVO.value.totalCount)
  const checkedItems = computed(() => cartVO.value.items.filter(item => item.checked))
  const totalPrice = computed(() => cartVO.value.totalPrice)

  // 获取购物车
  const fetchCart = async () => {
    try {
      const res = await getCart()
      cartVO.value = res
    } catch (error) {
      console.error('[Cart Store] Fetch cart failed', error)
    }
  }

  // 添加到购物车
  const addToCartAction = async (skuId: number, quantity: number) => {
    try {
      await addToCart(skuId, quantity)
      await fetchCart()
      return true
    } catch (error) {
      console.error('[Cart Store] Add to cart failed', error)
      return false
    }
  }

  // 更新数量
  const updateQuantity = async (skuId: number, quantity: number) => {
    try {
      await updateCartItem(skuId, quantity)
      await fetchCart()
    } catch (error) {
      console.error('[Cart Store] Update quantity failed', error)
    }
  }

  // 选中/取消选中
  const checkItem = async (skuId: number, checked: boolean) => {
    try {
      await checkCartItem(skuId, checked)
      await fetchCart()
    } catch (error) {
      console.error('[Cart Store] Check item failed', error)
    }
  }

  // 全选/取消全选
  const checkAll = async (checked: boolean) => {
    try {
      await checkAllCart(checked)
      await fetchCart()
    } catch (error) {
      console.error('[Cart Store] Check all failed', error)
    }
  }

  // 删除商品
  const deleteItems = async (skuIds: number[]) => {
    try {
      await deleteCartItems(skuIds)
      await fetchCart()
    } catch (error) {
      console.error('[Cart Store] Delete items failed', error)
    }
  }

  return {
    cartVO,
    totalCount,
    checkedItems,
    totalPrice,
    fetchCart,
    addToCart: addToCartAction,
    updateQuantity,
    checkItem,
    checkAll,
    deleteItems
  }
})
