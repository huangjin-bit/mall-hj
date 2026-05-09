import { get, post } from '../request'

export interface OrderSetting {
  id?: number
  orderOverdueMinutes?: number
  confirmOverdueDays?: number
  finishOverdueDays?: number
  commentOverdueDays?: number
  returnOverdueDays?: number
}

// 获取订单设置
export const getOrderSetting = () => {
  return get<OrderSetting>('/admin/order/setting')
}

// 更新订单设置
export const updateOrderSetting = (data: OrderSetting) => {
  return post<void>('/admin/order/setting/update', data)
}
