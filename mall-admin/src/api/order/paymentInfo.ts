import { get, post } from '../request'
import type { PageResult } from '../request'

export interface PaymentInfo {
  id?: number
  orderId?: number
  orderSn?: string
  memberId?: number
  payType: number // 1: 支付宝, 2: 微信, 3: 银行卡
  payAmount: number
  status: number // 1: 待支付, 2: 已支付, 3: 支付失败
  payTime?: string
  transactionId?: string
  createTime?: string
}

export interface PaymentInfoParams {
  key?: string
  orderId?: number
  status?: number
}

// 获取支付记录列表
export const getPaymentInfoList = (params?: PaymentInfoParams) => {
  return get<PageResult<PaymentInfo>>('/admin/order/payment/list', params)
}

// 根据 ID 获取支付记录
export const getPaymentInfoById = (id: number) => {
  return get<PaymentInfo>(`/admin/order/payment/${id}`)
}
