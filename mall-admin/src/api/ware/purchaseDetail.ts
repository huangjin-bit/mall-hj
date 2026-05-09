import { get, post } from '../request'
import type { PageResult } from '../request'

export interface PurchaseDetail {
  id?: number
  purchaseId?: number
  purchaseSn?: string
  skuId?: number
  skuName?: string
  skuImage?: string
  quantity: number
  purchasePrice: number
  totalPrice: number
  status: number // 1: 未采购, 2: 已采购
  createTime?: string
}

export interface PurchaseDetailParams {
  key?: string
  purchaseId?: number
}

// 获取采购单明细列表
export const getPurchaseDetailList = (params?: PurchaseDetailParams) => {
  return get<PageResult<PurchaseDetail>>('/admin/ware/purchaseDetail/list', params)
}

// 根据 ID 获取采购单明细
export const getPurchaseDetailById = (id: number) => {
  return get<PurchaseDetail>(`/admin/ware/purchaseDetail/${id}`)
}

// 保存采购单明细
export const savePurchaseDetail = (data: PurchaseDetail) => {
  return post<void>('/admin/ware/purchaseDetail/save', data)
}

// 更新采购单明细
export const updatePurchaseDetail = (data: PurchaseDetail) => {
  return post<void>('/admin/ware/purchaseDetail/update', data)
}

// 删除采购单明细
export const deletePurchaseDetail = (ids: number[]) => {
  return post<void>('/admin/ware/purchaseDetail/delete', ids)
}

// 根据采购单 ID 获取明细列表
export const getDetailsByPurchaseId = (purchaseId: number) => {
  return get<PurchaseDetail[]>(`/admin/ware/purchaseDetail/purchase/${purchaseId}`)
}
