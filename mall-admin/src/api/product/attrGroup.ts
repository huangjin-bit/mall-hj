import { get, post } from '../request'
import type { PageResult } from '../request'

export interface AttrGroup {
  id?: number
  categoryId?: number
  groupName: string
  sort: number
  descript?: string
  icon?: string
  attrs?: Attr[]
}

export interface Attr {
  id?: number
  attrName: string
  attrType: number
  valueType: number
  valueSelect?: string
}

export interface AttrGroupParams {
  key?: string
  categoryId?: number
}

// 获取属性分组列表
export const getAttrGroupList = (params?: AttrGroupParams) => {
  return get<PageResult<AttrGroup>>('/admin/product/attrGroup/list', params)
}

// 根据 ID 获取属性分组
export const getAttrGroupById = (id: number) => {
  return get<AttrGroup>(`/admin/product/attrGroup/${id}`)
}

// 获取分组下的属性
export const getGroupAttrs = (groupId: number) => {
  return get<Attr[]>(`/admin/product/attrGroup/${groupId}/attrs`)
}

// 获取分组下未关联的属性
export const getGroupNoAttrs = (groupId: number, params?: { key?: string }) => {
  return get<PageResult<Attr>>(`/admin/product/attrGroup/${groupId}/noAttrs`, params)
}

// 获取分类及其所有分组和属性
export const getGroupsWithAttrs = (categoryId: number) => {
  return get<AttrGroup[]>(`/admin/product/attrGroup/category/${categoryId}`)
}

// 保存属性分组
export const saveAttrGroup = (data: AttrGroup) => {
  return post<void>('/admin/product/attrGroup/save', data)
}

// 更新属性分组
export const updateAttrGroup = (data: AttrGroup) => {
  return post<void>('/admin/product/attrGroup/update', data)
}

// 删除属性分组
export const deleteAttrGroup = (ids: number[]) => {
  return post<void>('/admin/product/attrGroup/delete', ids)
}

// 关联属性到分组
export const addAttrToGroup = (groupId: number, attrIds: number[]) => {
  return post<void>(`/admin/product/attrGroup/${groupId}/attrs/add`, attrIds)
}

// 从分组移除属性
export const removeAttrFromGroup = (groupId: number, attrIds: number[]) => {
  return post<void>(`/admin/product/attrGroup/${groupId}/attrs/remove`, attrIds)
}
