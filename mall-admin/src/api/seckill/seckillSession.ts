import { get, post } from '../request'
import type { PageResult } from '../request'

export interface SeckillSession {
  id?: number
  name: string
  startTime: string
  endTime: string
  status: number // 1: 未开始, 2: 进行中, 3: 已结束
  sort: number
  createTime?: string
}

export interface SeckillSessionParams {
  key?: string
  status?: number
}

// 获取秒杀场次列表
export const getSeckillSessionList = (params?: SeckillSessionParams) => {
  return get<PageResult<SeckillSession>>('/admin/seckill/session/list', params)
}

// 根据 ID 获取秒杀场次
export const getSeckillSessionById = (id: number) => {
  return get<SeckillSession>(`/admin/seckill/session/${id}`)
}

// 保存秒杀场次
export const saveSeckillSession = (data: SeckillSession) => {
  return post<void>('/admin/seckill/session/save', data)
}

// 更新秒杀场次
export const updateSeckillSession = (data: SeckillSession) => {
  return post<void>('/admin/seckill/session/update', data)
}

// 删除秒杀场次
export const deleteSeckillSession = (ids: number[]) => {
  return post<void>('/admin/seckill/session/delete', ids)
}

// 获取当前活跃的秒杀场次
export const getActiveSessions = () => {
  return get<SeckillSession[]>('/admin/seckill/session/active')
}
