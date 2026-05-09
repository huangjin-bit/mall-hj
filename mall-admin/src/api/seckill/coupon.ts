import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Coupon {
  id?: number
  couponType: number // 1: 满减券, 2: 折扣券, 3: 立减券
  couponName: string
  amount?: number
  discount?: number
  minAmount: number
  totalCount: number
  remainCount: number
  perLimit: number
  validType: number // 1: 固定期限, 2: 相对期限
  validDays?: number
  startTime?: string
  endTime?: string
  status: number // 1: 未开始, 2: 进行中, 3: 已结束
  createTime?: string
}

export interface CouponParams {
  key?: string
  couponType?: number
  status?: number
}

// 获取优惠券列表
export const getCouponList = (params?: CouponParams) => {
  return get<PageResult<Coupon>>('/admin/seckill/coupon/list', params)
}

// 根据 ID 获取优惠券
export const getCouponById = (id: number) => {
  return get<Coupon>(`/admin/seckill/coupon/${id}`)
}

// 保存优惠券
export const saveCoupon = (data: Coupon) => {
  return post<void>('/admin/seckill/coupon/save', data)
}

// 更新优惠券
export const updateCoupon = (data: Coupon) => {
  return post<void>('/admin/seckill/coupon/update', data)
}

// 删除优惠券
export const deleteCoupon = (ids: number[]) => {
  return post<void>('/admin/seckill/coupon/delete', ids)
}

// 获取可用的优惠券
export const getAvailableCoupons = () => {
  return get<Coupon[]>('/admin/seckill/coupon/available')
}
