import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Sku {
  id?: number
  spuId?: number
  skuName: string
  price: number
  originalPrice?: number
  stock: number
  publishStatus?: number
  saleAttrs?: Record<string, string>
  images?: string[]
  createTime?: string
}

export interface SkuParams {
  key?: string
  spuId?: number
  publishStatus?: number
}

// 获取 SKU 列表
export const getSkuList = (params?: SkuParams) => {
  return get<PageResult<Sku>>('/admin/product/sku/list', params)
}

// 获取 SKU 详情
export const getSkuDetail = (skuId: number) => {
  return get<Sku>(`/admin/product/sku/${skuId}`)
}

// 保存 SKU
export const saveSku = (data: Sku) => {
  return post<void>('/admin/product/sku/save', data)
}

// 更新 SKU
export const updateSku = (data: Sku) => {
  return post<void>('/admin/product/sku/update', data)
}

// 删除 SKU
export const deleteSku = (ids: number[]) => {
  return post<void>('/admin/product/sku/delete', ids)
}

// 发布/下架 SKU
export const publishSku = (skuId: number, status: number) => {
  return post<void>(`/admin/product/sku/${skuId}/publish`, { status })
}
