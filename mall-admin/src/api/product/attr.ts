import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Attr {
  id?: number
  attrGroupId?: number
  attrName: string
  attrType: number // 1: 基本属性, 2: 销售属性
  valueType: number // 1: 手动输入, 2: 列表选择
  valueSelect?: string
  sort: number
  categoryId?: number
  values?: string[]
}

export interface AttrParams {
  key?: string
  categoryId?: number
  attrType?: number
  attrGroupId?: number
}

// 获取属性列表
export const getAttrList = (params?: AttrParams) => {
  return get<PageResult<Attr>>('/admin/product/attr/list', params)
}

// 根据 ID 获取属性
export const getAttrById = (id: number) => {
  return get<Attr>(`/admin/product/attr/${id}`)
}

// 获取分类的基本属性
export const getBaseAttrs = (categoryId: number, params?: AttrParams) => {
  return get<PageResult<Attr>>('/admin/product/attr/base', { categoryId, ...params })
}

// 获取分类的销售属性
export const getSaleAttrs = (categoryId: number, params?: AttrParams) => {
  return get<PageResult<Attr>>('/admin/product/attr/sale', { categoryId, ...params })
}

// 保存属性
export const saveAttr = (data: Attr) => {
  return post<void>('/admin/product/attr/save', data)
}

// 更新属性
export const updateAttr = (data: Attr) => {
  return post<void>('/admin/product/attr/update', data)
}

// 删除属性
export const deleteAttr = (ids: number[]) => {
  return post<void>('/admin/product/attr/delete', ids)
}
