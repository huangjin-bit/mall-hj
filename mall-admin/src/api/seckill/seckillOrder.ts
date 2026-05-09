import { get } from '../request'
import type { PageResult } from '../request'

export interface SeckillOrder {
  id?: number
  orderSn?: string
  sessionId?: number
  sessionName?: string
  skuId?: number
  skuName?: string
  memberId?: number
  username?: string
  seckillPrice: number
  quantity: number
  totalAmount: number
  status: number // 1: 待支付, 2: 已支付, 3: 已取消
  payTime?: string
  createTime?: string
}

export interface SeckillOrderParams {
  key?: string
  sessionId?: number
  status?: number
}

// 获取秒杀订单列表
export const getSeckillOrderList = (params?: SeckillOrderParams) => {
  return get<PageResult<SeckillOrder>>('/admin/seckill/order/list', params)
}

// 根据 ID 获取秒杀订单
export const getSeckillOrderById = (id: number) => {
  return get<SeckillOrder>(`/admin/seckill/order/${id}`)
}
