import { get } from '../request'
import type { PageResult } from '../request'

export interface IntegrationRecord {
  id?: number
  memberId?: number
  username?: string
  integration: number
  type: number // 1: 增加, 2: 减少
  reason?: string
  createTime?: string
}

export interface IntegrationRecordParams {
  key?: string
  memberId?: number
  type?: number
}

// 获取积分记录列表
export const getIntegrationRecordList = (params?: IntegrationRecordParams) => {
  return get<PageResult<IntegrationRecord>>('/admin/member/integration/list', params)
}
