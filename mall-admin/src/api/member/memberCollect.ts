import { get, post } from '../request'
import type { PageResult } from '../request'

export interface MemberCollect {
  id?: number
  memberId?: number
  username?: string
  skuId?: number
  skuName?: string
  skuImage?: string
  skuPrice?: number
  createTime?: string
}

export interface MemberCollectParams {
  key?: string
  memberId?: number
  skuId?: number
}

// 获取收藏记录列表
export const getMemberCollectList = (params?: MemberCollectParams) => {
  return get<PageResult<MemberCollect>>('/admin/member/collect/list', params)
}

// 删除收藏记录
export const deleteMemberCollect = (ids: number[]) => {
  return post<void>('/admin/member/collect/delete', ids)
}
