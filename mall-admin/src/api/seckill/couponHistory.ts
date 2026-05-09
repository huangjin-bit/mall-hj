import { get, post } from '../request'
import type { PageResult } from '../request'

export interface CouponHistory {
  id?: number
  couponId?: number
  couponName?: string
  memberId?: number
  username?: string
  status: number // 1: 未使用, 2: 已使用, 3: 已过期
  useTime?: string
  orderId?: string
  receiveTime?: string
}

export interface CouponHistoryParams {
  key?: string
  couponId?: number
  status?: number
}

// 获取优惠券领取记录列表
export const getCouponHistoryList = (params?: CouponHistoryParams) => {
  return get<PageResult<CouponHistory>>('/admin/seckill/couponHistory/list', params)
}

// 删除领取记录
export const deleteCouponHistory = (ids: number[]) => {
  return post<void>('/admin/seckill/couponHistory/delete', ids)
}
