import request, { ApiResult } from '../request'

// 会员实体
export interface Member {
  id: number
  username: string
  nickname?: string
  phone?: string
  gender?: number
  birthday?: string
  avatar?: string
  createTime?: string
}

// 获取会员信息
export const getMemberInfo = (id: number) => {
  return request.get<any, ApiResult<Member>>(`/member/member/${id}`)
}

// 更新会员信息
export const updateMember = (data: Member) => {
  return request.put<any, void>('/member/member', data)
}
