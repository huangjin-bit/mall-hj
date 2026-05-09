import { get, post } from '../request'
import type { PageResult } from '../request'

export interface Spu {
  id?: number
  spuName: string
  categoryId?: number
  brandId?: number
  weight?: number
  publishStatus?: number
  auditStatus?: number
  description?: string
  images?: string[]
  createTime?: string
  updateTime?: string
}

export interface SpuParams {
  key?: string
  categoryId?: number
  brandId?: number
  publishStatus?: number
  auditStatus?: number
}

// 获取 SPU 列表
export const getSpuList = (params?: SpuParams) => {
  return get<PageResult<Spu>>('/admin/product/spu/list', params)
}

// 获取 SPU 详情
export const getSpuDetail = (spuId: number) => {
  return get<Spu>(`/admin/product/spu/${spuId}`)
}

// 保存 SPU
export const saveSpu = (data: Spu) => {
  return post<void>('/admin/product/spu/save', data)
}

// 更新 SPU
export const updateSpu = (data: Spu) => {
  return post<void>('/admin/product/spu/update', data)
}

// 删除 SPU
export const deleteSpu = (ids: number[]) => {
  return post<void>('/admin/product/spu/delete', ids)
}

// 发布/下架 SPU
export const publishSpu = (spuId: number, status: number) => {
  return post<void>(`/admin/product/spu/${spuId}/publish`, { status })
}

// 审核 SPU
export const auditSpu = (spuId: number, status: number, reason?: string) => {
  return post<void>(`/admin/product/spu/${spuId}/audit`, { status, reason })
}
