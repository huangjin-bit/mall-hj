import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Order {
  id?: number
  orderSn?: string
  memberId?: number
  username?: string
  totalAmount: number
  payAmount: number
  status: number
  deliveryType?: number
  receiverName?: string
  receiverPhone?: string
  receiverAddress?: string
  note?: string
  createTime?: string
  payTime?: string
  deliveryTime?: string
  finishTime?: string
}

export interface OrderParams {
  key?: string
  status?: number
  memberId?: number
}

// 获取订单列表
export const getOrderList = (params?: OrderParams) => {
  return get<PageResult<Order>>('/admin/order/order/list', params)
}

// 根据 ID 获取订单详情
export const getOrderById = (id: number) => {
  return get<Order>(`/admin/order/order/${id}`)
}

// 根据订单号获取订单
export const getOrderBySn = (orderSn: string) => {
  return get<Order>('/admin/order/order/sn', { orderSn })
}

// 更新订单状态
export const updateOrderStatus = (id: number, status: number) => {
  return post<void>('/admin/order/order/updateStatus', { id, status })
}

// 发货
export const deliverOrder = (id: number, deliveryCompany: string, deliverySn: string) => {
  return post<void>('/admin/order/order/deliver', { id, deliveryCompany, deliverySn })
}

// 取消订单
export const cancelOrder = (id: number, reason: string) => {
  return post<void>('/admin/order/order/cancel', { id, reason })
}

// 删除订单
export const deleteOrder = (ids: number[]) => {
  return post<void>('/admin/order/order/delete', ids)
}
