import { get, post } from '../request'
import type { PageResult } from '../request'

export interface OrderReturn {
  id?: number
  orderId?: number
  orderSn?: string
  memberId?: number
  username?: string
  skuId?: number
  skuName?: string
  skuImage?: string
  type: number // 1: 退款, 2: 退货退款
  reason: string
  description?: string
  amount: number
  status: number // 1: 待审核, 2: 审核通过, 3: 审核拒绝, 4: 退款中, 5: 已退款
  auditTime?: string
  refundTime?: string
  createTime?: string
}

export interface OrderReturnParams {
  key?: string
  orderId?: number
  status?: number
}

// 获取退货申请列表
export const getOrderReturnList = (params?: OrderReturnParams) => {
  return get<PageResult<OrderReturn>>('/admin/order/return/list', params)
}

// 根据 ID 获取退货申请
export const getOrderReturnById = (id: number) => {
  return get<OrderReturn>(`/admin/order/return/${id}`)
}

// 审核退货申请
export const auditOrderReturn = (id: number, status: number, remark?: string) => {
  return post<void>('/admin/order/return/audit', { id, status, remark })
}

// 确认退款
export const confirmRefund = (id: number) => {
  return post<void>(`/admin/order/return/${id}/refund`)
}

// 删除退货申请
export const deleteOrderReturn = (ids: number[]) => {
  return post<void>('/admin/order/return/delete', ids)
}
