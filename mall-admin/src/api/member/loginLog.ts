import { get } from '../request'
import type { PageResult } from '../request'

export interface LoginLog {
  id?: number
  memberId?: number
  username?: string
  loginIp?: string
  loginDevice?: string
  createTime?: string
}

export interface LoginLogParams {
  key?: string
  memberId?: number
}

// 获取登录日志列表
export const getLoginLogList = (params?: LoginLogParams) => {
  return get<PageResult<LoginLog>>('/admin/member/loginLog/list', params)
}
