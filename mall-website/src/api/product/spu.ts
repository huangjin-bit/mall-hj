import request, { ApiResult, PageResult } from '../request'

// SPU 实体
export interface Spu {
  id: number
  spuName: string
  spuDescription?: string
  categoryId: number
  brandId: number
  publishStatus?: number
  createTime?: string
  images?: string[]
  baseAttrs?: { attrName: string; attrValue: string }[]
  skus?: Sku[]
}

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

// 获取 SPU 详情
export const getSpuDetail = (spuId: number) => {
  return request.get<any, ApiResult<Spu>>(`/product/spu/${spuId}`)
}

// 获取 SPU 列表
export const getSpuList = (params: {
  keyword?: string
  categoryId?: number
  brandId?: number
  page?: number
  size?: number
}) => {
  return request.get<any, ApiResult<PageResult<Spu>>>('/product/spu/list', { params })
}
