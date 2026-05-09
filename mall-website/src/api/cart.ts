import request, { ApiResult } from './request'

// 购物车项实体
export interface CartItem {
  skuId: number
  spuId: number
  spuName: string
  skuName: string
  skuImage?: string
  price: number
  quantity: number
  checked: boolean
  saleAttrs?: { attrName: string; attrValue: string }[]
}

// 购物车 VO
export interface CartVO {
  items: CartItem[]
  totalCount: number
  totalPrice: number
}

// 获取购物车
export const getCart = () => {
  return request.get<any, ApiResult<CartVO>>('/cart/cart')
}

// 添加到购物车
export const addToCart = (skuId: number, quantity: number) => {
  return request.post<any, void>('/cart/cart/add', { skuId, quantity })
}

// 更新购物车项数量
export const updateCartItem = (skuId: number, quantity: number) => {
  return request.put<any, void>('/cart/cart/update', { skuId, quantity })
}

// 选中/取消选中购物车项
export const checkCartItem = (skuId: number, checked: boolean) => {
  return request.put<any, void>('/cart/cart/check', { skuId, checked })
}

// 全选/取消全选
export const checkAllCart = (checked: boolean) => {
  return request.put<any, void>('/cart/cart/check/all', { checked })
}

// 删除购物车项
export const deleteCartItems = (skuIds: number[]) => {
  return request.delete<any, void>('/cart/cart/delete', { data: skuIds })
}
