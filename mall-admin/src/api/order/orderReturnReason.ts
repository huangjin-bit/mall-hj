import { get, post } from '../request'
import type { PageResult } from '../request'

export interface OrderReturnReason {
  id?: number
  reason: string
  sort: number
  status: number
}

export interface OrderReturnReasonParams {
  key?: string
  status?: number
}

// 获取退货原因列表
export const getOrderReturnReasonList = (params?: OrderReturnReasonParams) => {
  return get<PageResult<OrderReturnReason>>('/admin/order/returnReason/list', params)
}

// 根据 ID 获取退货原因
export const getOrderReturnReasonById = (id: number) => {
  return get<OrderReturnReason>(`/admin/order/returnReason/${id}`)
}

// 保存退货原因
export const saveOrderReturnReason = (data: OrderReturnReason) => {
  return post<void>('/admin/order/returnReason/save', data)
}

// 更新退货原因
export const updateOrderReturnReason = (data: OrderReturnReason) => {
  return post<void>('/admin/order/returnReason/update', data)
}

// 删除退货原因
export const deleteOrderReturnReason = (ids: number[]) => {
  return post<void>('/admin/order/returnReason/delete', ids)
}
