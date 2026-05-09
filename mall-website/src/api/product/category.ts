import request, { ApiResult } from '../request'

// 分类实体
export interface Category {
  id: number
  name: string
  parentId: number
  level: number
  icon?: string
  children?: Category[]
}

// 获取分类树
export const getCategoryTree = () => {
  return request.get<any, ApiResult<Category[]>>('/product/category/tree')
}

// 获取分类列表
export const getCategoryList = (params: { name?: string; level?: number; parentId?: number }) => {
  return request.get<any, ApiResult<Category[]>>('/product/category/list', { params })
}
