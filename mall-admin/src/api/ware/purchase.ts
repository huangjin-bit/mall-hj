import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Purchase {
  id?: number
  purchaseSn?: string
  wareId?: number
  wareName?: string
  supplier?: string
  totalAmount: number
  status: number // 1: 未完成, 2: 已完成, 3: 已取消
  note?: string
  createTime?: string
  finishTime?: string
}

export interface PurchaseParams {
  key?: string
  wareId?: number
  status?: number
}

// 获取采购单列表
export const getPurchaseList = (params?: PurchaseParams) => {
  return get<PageResult<Purchase>>('/admin/ware/purchase/list', params)
}

// 根据 ID 获取采购单
export const getPurchaseById = (id: number) => {
  return get<Purchase>(`/admin/ware/purchase/${id}`)
}

// 保存采购单
export const savePurchase = (data: Purchase) => {
  return post<void>('/admin/ware/purchase/save', data)
}

// 更新采购单
export const updatePurchase = (data: Purchase) => {
  return post<void>('/admin/ware/purchase/update', data)
}

// 删除采购单
export const deletePurchase = (ids: number[]) => {
  return post<void>('/admin/ware/purchase/delete', ids)
}

// 完成采购单
export const finishPurchase = (id: number) => {
  return post<void>(`/admin/ware/purchase/${id}/finish`)
}

// 取消采购单
export const cancelPurchase = (id: number) => {
  return post<void>(`/admin/ware/purchase/${id}/cancel`)
}
