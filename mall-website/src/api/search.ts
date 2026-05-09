import request, { PageResult } from './request'

// 搜索参数
export interface SearchParams {
  keyword?: string
  categoryId?: number
  brandId?: number
  minPrice?: number
  maxPrice?: number
  sort?: 'default' | 'price_asc' | 'price_desc' | 'sales' | 'newest'
  page?: number
  size?: number
}

// 搜索结果项
export interface SearchResultItem {
  id: number
  spuName: string
  price: number
  image?: string
  sales?: number
  createTime?: string
}

// 搜索
export const search = (params: SearchParams) => {
  return request.get<any, PageResult<SearchResultItem>>('/search/product', { params })
}
