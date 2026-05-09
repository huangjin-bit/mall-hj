import { defineStore } from 'pinia'
import {
  getCart,
  addToCart as addToCartApi,
  updateCartItem as updateCartItemApi,
  checkCartItem as checkCartItemApi,
  checkAllCart as checkAllCartApi,
  deleteCartItems as deleteCartItemsApi
} from '@/api/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    cartVO: null,
    totalCount: 0
  }),

  getters: {
    // 购物车商品列表
    cartItems: (state) => state.cartVO?.cartItems || [],
    // 选中的商品
    checkedItems: (state) => state.cartVO?.cartItems?.filter(item => item.checked) || [],
    // 选中商品总数量
    checkedCount: (state) => state.cartVO?.cartItems?.filter(item => item.checked).reduce((sum, item) => sum + item.count, 0) || 0,
    // 选中商品总价格
    totalPrice: (state) => state.cartVO?.cartItems?.filter(item => item.checked).reduce((sum, item) => sum + (item.salePrice * item.count), 0) || 0,
    // 是否全选
    isAllChecked: (state) => {
      const items = state.cartVO?.cartItems || []
      return items.length > 0 && items.every(item => item.checked)
    }
  },

  actions: {
    /**
     * 获取购物车列表
     */
    async fetchCart() {
      try {
        const res = await getCart()
        this.cartVO = res
        this.totalCount = res.totalCount || 0
      } catch (error) {
        console.error('[FetchCart Error]', error)
        throw error
      }
    },

    /**
     * 添加商品到购物车
     */
    async addToCart(skuId, count = 1) {
      try {
        uni.showLoading({
          title: '添加中...'
        })

        await addToCartApi({ skuId, count })
        await this.fetchCart()

        uni.hideLoading()
        uni.showToast({
          title: '已添加到购物车',
          icon: 'success'
        })
      } catch (error) {
        uni.hideLoading()
        console.error('[AddToCart Error]', error)
        throw error
      }
    },

    /**
     * 更新商品数量
     */
    async updateQuantity(skuId, count) {
      try {
        await updateCartItemApi({ skuId, count })
        await this.fetchCart()
      } catch (error) {
        console.error('[UpdateQuantity Error]', error)
        throw error
      }
    },

    /**
     * 选中/取消选中商品
     */
    async checkItem(skuId, checked) {
      try {
        await checkCartItemApi({ skuId, checked })
        await this.fetchCart()
      } catch (error) {
        console.error('[CheckItem Error]', error)
        throw error
      }
    },

    /**
     * 全选/取消全选
     */
    async checkAll(checked) {
      try {
        await checkAllCartApi({ checked })
        await this.fetchCart()
      } catch (error) {
        console.error('[CheckAll Error]', error)
        throw error
      }
    },

    /**
     * 删除选中商品
     */
    async deleteCheckedItems() {
      const skuIds = this.checkedItems.map(item => item.skuId)

      if (skuIds.length === 0) {
        uni.showToast({
          title: '请选择要删除的商品',
          icon: 'none'
        })
        return
      }

      try {
        await uni.showModal({
          title: '提示',
          content: `确定删除这${skuIds.length}件商品吗？`
        })

        await deleteCartItemsApi({ skuIds })
        await this.fetchCart()

        uni.showToast({
          title: '删除成功',
          icon: 'success'
        })
      } catch (error) {
        console.error('[DeleteCartItems Error]', error)
        throw error
      }
    },

    /**
     * 清空购物车
     */
    clearCart() {
      this.cartVO = null
      this.totalCount = 0
    }
  }
})
