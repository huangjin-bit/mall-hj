import request, { ApiResult } from '../request'

// SKU 实体
export interface Sku {
  id: number
  skuName: string
  spuId: number
  price: number
  stock: number
  image?: string
  saleAttrs?: { attrName: string; attrValue: string }[]
}

// 获取 SKU 详情
export const getSkuDetail = (skuId: number) => {
  return request.get<any, ApiResult<Sku>>(`/product/sku/${skuId}`)
}
