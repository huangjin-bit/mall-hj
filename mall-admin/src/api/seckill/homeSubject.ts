import { get, post } from '../request'
import type { PageResult } from '../request'

export interface HomeSubject {
  id?: number
  title: string
  imageUrl: string
  linkUrl?: string
  categoryId?: number
  sort: number
  status: number
  createTime?: string
}

export interface HomeSubjectParams {
  key?: string
  categoryId?: number
  status?: number
}

// 获取首页专题列表
export const getHomeSubjectList = (params?: HomeSubjectParams) => {
  return get<PageResult<HomeSubject>>('/admin/seckill/homeSubject/list', params)
}

// 根据 ID 获取专题
export const getHomeSubjectById = (id: number) => {
  return get<HomeSubject>(`/admin/seckill/homeSubject/${id}`)
}

// 保存专题
export const saveHomeSubject = (data: HomeSubject) => {
  return post<void>('/admin/seckill/homeSubject/save', data)
}

// 更新专题
export const updateHomeSubject = (data: HomeSubject) => {
  return post<void>('/admin/seckill/homeSubject/update', data)
}

// 删除专题
export const deleteHomeSubject = (ids: number[]) => {
  return post<void>('/admin/seckill/homeSubject/delete', ids)
}

// 获取公开的专题列表（供前端展示）
export const getPublicHomeSubjectList = () => {
  return get<HomeSubject[]>('/api/seckill/homeSubject/public')
}
