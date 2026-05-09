import request, { ApiResult, PageResult } from '../request'

// 优惠券实体
export interface Coupon {
  id: number
  couponName: string
  couponType: number
  amount: number
  minAmount: number
  startTime: string
  endTime: string
  totalCount: number
  remainCount: number
  description?: string
}

// 用户优惠券实体
export interface MemberCoupon {
  id: number
  memberId: number
  couponId: number
  couponName: string
  couponType: number
  amount: number
  minAmount: number
  status: number
  startTime: string
  endTime: string
}

// 获取可用优惠券
export const getAvailableCoupons = () => {
  return request.get<any, ApiResult<Coupon[]>>('/seckill/coupon/available')
}

// 领取优惠券
export const claimCoupon = (couponId: number) => {
  return request.post<any, void>(`/seckill/coupon/claim/${couponId}`)
}

// 获取用户优惠券
export const getMemberCoupons = (params: { memberId: number; status?: number; page?: number; size?: number }) => {
  return request.get<any, ApiResult<PageResult<MemberCoupon>>>('/seckill/coupon/member/list', { params })
}
