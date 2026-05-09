import request, { ApiResult, PageResult } from '../request'

// 秒杀订单实体
export interface SeckillOrder {
  id: number
  orderSn: string
  memberId: number
  sessionId: number
  skuId: number
  spuName: string
  skuName: string
  skuImage?: string
  seckillPrice: number
  quantity: number
  totalAmount: number
  status: number
  createTime?: string
}

// 创建秒杀订单
export const createSeckillOrder = (data: { seckillSkuId: number; quantity: number; receiverAddressId: number }) => {
  return request.post<any, string>('/seckill/order/create', data)
}

// 获取秒杀订单列表
export const getMemberSeckillOrders = (params: { memberId: number; page?: number; size?: number }) => {
  return request.get<any, ApiResult<PageResult<SeckillOrder>>>('/seckill/order/member/list', { params })
}
