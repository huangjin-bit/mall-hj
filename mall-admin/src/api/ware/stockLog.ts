import { get } from '../request'
import type { PageResult } from '../request'

export interface StockLog {
  id?: number
  wareId?: number
  wareName?: string
  skuId?: number
  skuName?: string
  type: number // 1: 入库, 2: 出库, 3: 调整
  quantity: number
  beforeStock: number
  afterStock: number
  remark?: string
  createTime?: string
}

export interface StockLogParams {
  key?: string
  wareId?: number
  skuId?: number
  type?: number
}

// 获取库存日志列表
export const getStockLogList = (params?: StockLogParams) => {
  return get<PageResult<StockLog>>('/admin/ware/stockLog/list', params)
}
