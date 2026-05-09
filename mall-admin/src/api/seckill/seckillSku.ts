import { get, post } from '../request'
import type { PageResult } from '../request'

export interface SeckillSku {
  id?: number
  sessionId?: number
  sessionName?: string
  skuId?: number
  skuName?: string
  skuImage?: string
  seckillPrice: number
  seckillStock: number
  seckillLimit: number
  sortOrder: number
  status: number // 1: 未开始, 2: 进行中, 3: 已结束
  createTime?: string
}

export interface SeckillSkuParams {
  key?: string
  sessionId?: number
  status?: number
}

// 获取秒杀商品列表
export const getSeckillSkuList = (params?: SeckillSkuParams) => {
  return get<PageResult<SeckillSku>>('/admin/seckill/sku/list', params)
}

// 根据 ID 获取秒杀商品
export const getSeckillSkuById = (id: number) => {
  return get<SeckillSku>(`/admin/seckill/sku/${id}`)
}

// 保存秒杀商品
export const saveSeckillSku = (data: SeckillSku) => {
  return post<void>('/admin/seckill/sku/save', data)
}

// 更新秒杀商品
export const updateSeckillSku = (data: SeckillSku) => {
  return post<void>('/admin/seckill/sku/update', data)
}

// 删除秒杀商品
export const deleteSeckillSku = (ids: number[]) => {
  return post<void>('/admin/seckill/sku/delete', ids)
}
