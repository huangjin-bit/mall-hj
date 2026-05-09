import { get } from '../request'
import type { PageResult } from '../request'

export interface AuditLog {
  id?: number
  spuId?: number
  spuName?: string
  operateType?: string
  auditStatus?: number
  auditReason?: string
  auditor?: string
  createTime?: string
}

export interface AuditLogParams {
  key?: string
  spuId?: number
  auditStatus?: number
}

// 获取审核日志列表
export const getAuditLogList = (params?: AuditLogParams) => {
  return get<PageResult<AuditLog>>('/admin/product/auditLog/list', params)
}
