import request, { ApiResult } from '../request'

// 收货地址实体
export interface MemberAddress {
  id: number
  memberId: number
  receiverName: string
  receiverPhone: string
  province: string
  city: string
  district: string
  detailAddress: string
  isDefault: number
}

// 获取地址列表
export const getAddressList = (memberId: number) => {
  return request.get<any, ApiResult<MemberAddress[]>>(`/member/address/list/${memberId}`)
}

// 获取地址详情
export const getAddressDetail = (id: number) => {
  return request.get<any, ApiResult<MemberAddress>>(`/member/address/${id}`)
}

// 新增地址
export const addAddress = (data: MemberAddress) => {
  return request.post<any, void>('/member/address', data)
}

// 更新地址
export const updateAddress = (data: MemberAddress) => {
  return request.put<any, void>('/member/address', data)
}

// 删除地址
export const deleteAddress = (id: number) => {
  return request.delete<any, void>(`/member/address/${id}`)
}

// 设置默认地址
export const setDefaultAddress = (id: number) => {
  return request.put<any, void>(`/member/address/default/${id}`)
}
