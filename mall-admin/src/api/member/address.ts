import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Address {
  id?: number
  memberId?: number
  name: string
  phone: string
  province: string
  city: string
  district: string
  detailAddress: string
  postCode?: string
  isDefault: number
}

// 获取会员地址列表
export const getAddressList = (memberId: number) => {
  return get<Address[]>(`/admin/member/address/${memberId}`)
}

// 根据 ID 获取地址
export const getAddressById = (id: number) => {
  return get<Address>(`/admin/member/address/detail/${id}`)
}

// 保存地址
export const saveAddress = (data: Address) => {
  return post<void>('/admin/member/address/save', data)
}

// 更新地址
export const updateAddress = (data: Address) => {
  return post<void>('/admin/member/address/update', data)
}

// 删除地址
export const deleteAddress = (id: number) => {
  return post<void>(`/admin/member/address/delete/${id}`)
}

// 设置默认地址
export const setDefaultAddress = (id: number, memberId: number) => {
  return post<void>('/admin/member/address/default', { id, memberId })
}
