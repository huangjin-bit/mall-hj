import request, { ApiResult } from '../request'

// 首页专题实体
export interface HomeSubject {
  id: number
  name: string
  categoryId?: number
  categoryIds?: string
  imageUrl?: string
  linkUrl?: string
  status: number
}

// 获取活跃专题
export const getActiveSubjects = () => {
  return request.get<any, ApiResult<HomeSubject[]>>('/seckill/homesubject/active')
}
