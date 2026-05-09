import request, { ApiResult } from '../request'

// 秒杀商品实体
export interface SeckillSku {
  id: number
  sessionId: number
  skuId: number
  spuId: number
  spuName: string
  skuName: string
  skuImage?: string
  originalPrice: number
  seckillPrice: number
  seckillStock: number
  seckillSales: number
  limitPerPerson: number
  saleAttrs?: { attrName: string; attrValue: string }[]
}

// 获取场次商品
export const getSessionSkus = (sessionId: number) => {
  return request.get<any, ApiResult<SeckillSku[]>>(`/seckill/sku/session/${sessionId}`)
}

// 获取秒杀商品详情
export const getSeckillSkuDetail = (sessionId: number, skuId: number) => {
  return request.get<any, ApiResult<SeckillSku>>(`/seckill/sku/detail/${sessionId}/${skuId}`)
}
