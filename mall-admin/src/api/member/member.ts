import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Member {
  id?: number
  username?: string
  phone?: string
  nickname?: string
  icon?: string
  levelId?: number
  levelName?: string
  integration: number
  growth: number
  status: number
  createTime?: string
}

export interface MemberParams {
  key?: string
  levelId?: number
  status?: number
}

// 获取会员列表
export const getMemberList = (params?: MemberParams) => {
  return get<PageResult<Member>>('/admin/member/member/list', params)
}

// 根据 ID 获取会员
export const getMemberById = (id: number) => {
  return get<Member>(`/admin/member/member/${id}`)
}

// 根据手机号获取会员
export const getMemberByPhone = (phone: string) => {
  return get<Member>('/admin/member/member/phone', { phone })
}

// 保存会员
export const saveMember = (data: Member) => {
  return post<void>('/admin/member/member/save', data)
}

// 更新会员
export const updateMember = (data: Member) => {
  return post<void>('/admin/member/member/update', data)
}

// 删除会员
export const deleteMember = (ids: number[]) => {
  return post<void>('/admin/member/member/delete', ids)
}
