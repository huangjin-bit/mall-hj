import { get } from '../request'
import type { PageResult } from '../request'

export interface OrderItem {
  id?: number
  orderId?: number
  orderSn?: string
  skuId?: number
  skuName?: string
  skuImage?: string
  skuPrice: number
  skuQuantity: number
  totalPrice: number
}

export interface OrderItemParams {
  key?: string
  orderId?: number
  orderSn?: string
}

// 获取订单项列表
export const getOrderItemList = (params?: OrderItemParams) => {
  return get<PageResult<OrderItem>>('/admin/order/item/list', params)
}

// 根据订单 ID 获取订单项
export const getItemsByOrderId = (orderId: number) => {
  return get<OrderItem[]>(`/admin/order/item/order/${orderId}`)
}
