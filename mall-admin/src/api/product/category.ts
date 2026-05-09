import { get, post, put, del } from '../request'
import type { PageResult } from '../request'

export interface Category {
  id?: number
  parentId?: number
  name: string
  level: number
  icon?: string
  sort: number
  status: number
  children?: Category[]
}

export interface CategoryParams {
  key?: string
  level?: number
  status?: number
}

// 获取分类列表
export const getCategoryList = (params?: CategoryParams) => {
  return get<PageResult<Category>>('/admin/product/category/list', params)
}

// 获取分类树
export const getCategoryTree = () => {
  return get<Category[]>('/admin/product/category/tree')
}

// 根据 ID 获取分类
export const getCategoryById = (id: number) => {
  return get<Category>(`/admin/product/category/${id}`)
}

// 保存分类
export const saveCategory = (data: Category) => {
  return post<void>('/admin/product/category/save', data)
}

// 更新分类
export const updateCategory = (data: Category) => {
  return post<void>('/admin/product/category/update', data)
}

// 删除分类
export const deleteCategory = (ids: number[]) => {
  return post<void>('/admin/product/category/delete', ids)
}
