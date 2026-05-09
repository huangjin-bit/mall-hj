import request, { ApiResult, PageResult } from '../request'

// 品牌实体
export interface Brand {
  id: number
  name: string
  logo?: string
  description?: string
}

// 获取品牌列表
export const getBrandList = (params: { name?: string; page?: number; size?: number }) => {
  return request.get<any, ApiResult<PageResult<Brand>>>('/product/brand/list', { params })
}

// 根据分类获取品牌
export const getBrandsByCategory = (categoryId: number) => {
  return request.get<any, ApiResult<Brand[]>>(`/product/brand/category/${categoryId}`)
}
