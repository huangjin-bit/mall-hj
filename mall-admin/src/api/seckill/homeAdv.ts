import { get, post } from '../request'
import type { PageResult } from '../request'

export interface HomeAdv {
  id?: number
  title: string
  imageUrl: string
  linkUrl?: string
  position: number // 1: 首页轮播, 2: 首页推荐
  sort: number
  status: number
  startTime?: string
  endTime?: string
  createTime?: string
}

export interface HomeAdvParams {
  key?: string
  position?: number
  status?: number
}

// 获取首页广告列表
export const getHomeAdvList = (params?: HomeAdvParams) => {
  return get<PageResult<HomeAdv>>('/admin/seckill/homeAdv/list', params)
}

// 根据 ID 获取广告
export const getHomeAdvById = (id: number) => {
  return get<HomeAdv>(`/admin/seckill/homeAdv/${id}`)
}

// 保存广告
export const saveHomeAdv = (data: HomeAdv) => {
  return post<void>('/admin/seckill/homeAdv/save', data)
}

// 更新广告
export const updateHomeAdv = (data: HomeAdv) => {
  return post<void>('/admin/seckill/homeAdv/update', data)
}

// 删除广告
export const deleteHomeAdv = (ids: number[]) => {
  return post<void>('/admin/seckill/homeAdv/delete', ids)
}

// 获取公开的广告列表（供前端展示）
export const getPublicHomeAdvList = (position: number) => {
  return get<HomeAdv[]>('/api/seckill/homeAdv/public', { position })
}
