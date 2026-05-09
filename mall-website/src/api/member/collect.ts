import request, { ApiResult, PageResult } from '../request'

// 收藏实体
export interface MemberCollect {
  id: number
  memberId: number
  spuId: number
  spuName: string
  spuImage?: string
  price: number
  createTime?: string
}

// 获取收藏列表
export const getCollectList = (params: { memberId: number; page?: number; size?: number }) => {
  return request.get<any, ApiResult<PageResult<MemberCollect>>>('/member/collect/list', { params })
}

// 添加收藏
export const addCollect = (data: { memberId: number; spuId: number }) => {
  return request.post<any, void>('/member/collect', data)
}

// 取消收藏
export const removeCollect = (memberId: number, spuId: number) => {
  return request.delete<any, void>(`/member/collect/${memberId}/${spuId}`)
}
