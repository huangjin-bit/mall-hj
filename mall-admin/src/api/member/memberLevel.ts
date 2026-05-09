import { get, post } from '../request'
import type { PageResult } from '../request'

export interface MemberLevel {
  id?: number
  name: string
  growthPoint: number
  defaultStatus: number
  discount: number
  priviledge?: string
  fullPriviledge?: string
  note?: string
}

export interface MemberLevelParams {
  key?: string
}

// 获取会员等级列表
export const getMemberLevelList = (params?: MemberLevelParams) => {
  return get<PageResult<MemberLevel>>('/admin/member/level/list', params)
}

// 根据 ID 获取会员等级
export const getMemberLevelById = (id: number) => {
  return get<MemberLevel>(`/admin/member/level/${id}`)
}

// 保存会员等级
export const saveMemberLevel = (data: MemberLevel) => {
  return post<void>('/admin/member/level/save', data)
}

// 更新会员等级
export const updateMemberLevel = (data: MemberLevel) => {
  return post<void>('/admin/member/level/update', data)
}

// 删除会员等级
export const deleteMemberLevel = (ids: number[]) => {
  return post<void>('/admin/member/level/delete', ids)
}
