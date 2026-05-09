import request, { ApiResult } from '../request'

// 首页广告实体
export interface HomeAdv {
  id: number
  name: string
  type: number
  imageUrl: string
  linkUrl?: string
  position: number
  status: number
  startTime?: string
  endTime?: string
}

// 获取当前活跃广告
export const getActiveAdvs = (type?: number) => {
  return request.get<any, ApiResult<HomeAdv[]>>('/seckill/homeadv/active', {
    params: { type }
  })
}
