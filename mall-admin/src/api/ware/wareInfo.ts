import { get, post } from '../request'
import type { PageResult } from '../request'

export interface WareInfo {
  id?: number
  name: string
  address: string
  areacode: string
  sort: number
  status: number
  createTime?: string
}

export interface WareInfoParams {
  key?: string
  status?: number
}

// 获取仓库列表
export const getWareInfoList = (params?: WareInfoParams) => {
  return get<PageResult<WareInfo>>('/admin/ware/wareInfo/list', params)
}

// 根据 ID 获取仓库
export const getWareInfoById = (id: number) => {
  return get<WareInfo>(`/admin/ware/wareInfo/${id}`)
}

// 保存仓库
export const saveWareInfo = (data: WareInfo) => {
  return post<void>('/admin/ware/wareInfo/save', data)
}

// 更新仓库
export const updateWareInfo = (data: WareInfo) => {
  return post<void>('/admin/ware/wareInfo/update', data)
}

// 删除仓库
export const deleteWareInfo = (ids: number[]) => {
  return post<void>('/admin/ware/wareInfo/delete', ids)
}
