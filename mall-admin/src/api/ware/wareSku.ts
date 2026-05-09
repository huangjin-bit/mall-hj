import { get, post } from '../request'
import type { PageResult } from '../request'

export interface WareSku {
  id?: number
  skuId?: number
  skuName?: string
  skuImage?: string
  wareId?: number
  wareName?: string
  stock: number
  lockedStock: number
  updateTime?: string
}

export interface WareSkuParams {
  key?: string
  wareId?: number
  skuId?: number
}

// 获取库存列表
export const getWareSkuList = (params?: WareSkuParams) => {
  return get<PageResult<WareSku>>('/admin/ware/wareSku/list', params)
}

// 根据 ID 获取库存
export const getWareSkuById = (id: number) => {
  return get<WareSku>(`/admin/ware/wareSku/${id}`)
}

// 保存库存
export const saveWareSku = (data: WareSku) => {
  return post<void>('/admin/ware/wareSku/save', data)
}

// 更新库存
export const updateWareSku = (data: WareSku) => {
  return post<void>('/admin/ware/wareSku/update', data)
}

// 删除库存
export const deleteWareSku = (ids: number[]) => {
  return post<void>('/admin/ware/wareSku/delete', ids)
}

// 调整库存
export const adjustStock = (id: number, quantity: number, type: number) => {
  return post<void>('/admin/ware/wareSku/adjust', { id, quantity, type })
}
