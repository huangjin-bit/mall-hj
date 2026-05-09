import request, { ApiResult, PageResult } from '../request'

// 退货申请实体
export interface OrderReturn {
  id: number
  orderId: number
  orderItemId: number
  reason: string
  description?: string
  images?: string[]
  status: number
  createTime?: string
}

// 创建退货申请
export const createReturnApply = (data: {
  orderId: number
  orderItemId: number
  reason: string
  description?: string
  images?: string[]
}) => {
  return request.post<any, void>('/order/return/apply', data)
}

// 获取退货列表
export const getReturnList = (params: { memberId: number; page?: number; size?: number }) => {
  return request.get<any, ApiResult<PageResult<OrderReturn>>>('/order/return/list', { params })
}
