import { get, post } from '../request'
import type { PageResult } from '../request'

export interface CustomerService {
  id?: number
  memberId?: number
  username?: string
  type: number // 1: 咨询, 2: 投诉, 3: 建议
  title: string
  content: string
  status: number // 1: 待处理, 2: 处理中, 3: 已完成
  handleResult?: string
  handler?: string
  handleTime?: string
  createTime?: string
}

export interface CustomerServiceMessage {
  id?: number
  serviceId?: number
  sender: string // member 或 admin
  content: string
  createTime?: string
}

export interface CustomerServiceParams {
  key?: string
  memberId?: number
  type?: number
  status?: number
}

// 获取工单列表
export const getCustomerServiceList = (params?: CustomerServiceParams) => {
  return get<PageResult<CustomerService>>('/admin/member/customerService/list', params)
}

// 根据 ID 获取工单详情
export const getCustomerServiceById = (id: number) => {
  return get<CustomerService>(`/admin/member/customerService/${id}`)
}

// 获取工单消息列表
export const getServiceMessages = (serviceId: number) => {
  return get<CustomerServiceMessage[]>(`/admin/member/customerService/${serviceId}/messages`)
}

// 保存工单
export const saveCustomerService = (data: CustomerService) => {
  return post<void>('/admin/member/customerService/save', data)
}

// 更新工单状态
export const updateCustomerServiceStatus = (id: number, status: number, handleResult?: string) => {
  return post<void>('/admin/member/customerService/updateStatus', { id, status, handleResult })
}

// 发送消息
export const sendServiceMessage = (serviceId: number, content: string) => {
  return post<void>('/admin/member/customerService/sendMessage', { serviceId, content, sender: 'admin' })
}

// 删除工单
export const deleteCustomerService = (ids: number[]) => {
  return post<void>('/admin/member/customerService/delete', ids)
}
