import request, { ApiResult } from '../request'

// 秒杀场次实体
export interface SeckillSession {
  id: number
  name: string
  startTime: string
  endTime: string
  status: number
}

// 获取当前活跃场次
export const getActiveSessions = () => {
  return request.get<any, ApiResult<SeckillSession[]>>('/seckill/session/active')
}

// 获取场次详情
export const getSessionDetail = (id: number) => {
  return request.get<any, ApiResult<SeckillSession>>(`/seckill/session/${id}`)
}
