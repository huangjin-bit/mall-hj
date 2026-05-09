import { get } from '../request'
import type { PageResult } from '../request'

export interface GrowthRecord {
  id?: number
  memberId?: number
  username?: string
  growth: number
  type: number // 1: 增加, 2: 减少
  reason?: string
  createTime?: string
}

export interface GrowthRecordParams {
  key?: string
  memberId?: number
  type?: number
}

// 获取成长记录列表
export const getGrowthRecordList = (params?: GrowthRecordParams) => {
  return get<PageResult<GrowthRecord>>('/admin/member/growth/list', params)
}
