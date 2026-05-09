import { get } from './request'

export function getOperLogList(params: any) {
  return get('/sys/oper-log/list', params)
}
