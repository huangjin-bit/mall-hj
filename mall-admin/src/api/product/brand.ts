import { get, post, put, del } from '../request'
import type { PageResult } from '../request'

export interface Brand {
  id?: number
  name: string
  logo?: string
  sort: number
  status: number
}

export interface BrandParams {
  key?: string
  status?: number
}

// 获取品牌列表
export const getBrandList = (params?: BrandParams) => {
  return get<PageResult<Brand>>('/admin/product/brand/list', params)
}

// 根据 ID 获取品牌
export const getBrandById = (id: number) => {
  return get<Brand>(`/admin/product/brand/${id}`)
}

// 根据分类获取品牌
export const getBrandsByCategory = (categoryId: number) => {
  return get<Brand[]>(`/admin/product/brand/category/${categoryId}`)
}

// 保存品牌
export const saveBrand = (data: Brand) => {
  return post<void>('/admin/product/brand/save', data)
}

// 更新品牌
export const updateBrand = (data: Brand) => {
  return post<void>('/admin/product/brand/update', data)
}

// 删除品牌
export const deleteBrand = (ids: number[]) => {
  return post<void>('/admin/product/brand/delete', ids)
}
