import request, { ApiResult, PageResult } from '../request'

// 订单状态枚举
export enum OrderStatus {
  PENDING_PAYMENT = 0,
  PENDING_SHIPMENT = 1,
  SHIPPED = 2,
  COMPLETED = 3,
  CANCELLED = 4
}

// 订单实体
export interface Order {
  id: number
  orderSn: string
  memberId: number
  totalAmount: number
  freightAmount: number
  discountAmount: number
  payAmount: number
  status: OrderStatus
  receiverName: string
  receiverPhone: string
  receiverAddress: string
  createTime?: string
  items?: OrderItem[]
}

// 订单项实体
export interface OrderItem {
  id: number
  orderId: number
  spuId: number
  spuName: string
  skuId: number
  skuName: string
  skuImage?: string
  price: number
  quantity: number
  saleAttrs?: string
}

// 创建订单参数
export interface CreateOrderParams {
  receiverAddressId: number
  items: { skuId: number; quantity: number }[]
  couponId?: number
}

// 获取订单列表
export const getOrderList = (params: {
  memberId: number
  status?: OrderStatus
  page?: number
  size?: number
}) => {
  return request.get<any, ApiResult<PageResult<Order>>>('/order/order/list', { params })
}

// 获取订单详情
export const getOrderDetail = (id: number) => {
  return request.get<any, ApiResult<Order>>(`/order/order/${id}`)
}

// 创建订单
export const createOrder = (data: CreateOrderParams) => {
  return request.post<any, string>('/order/order', data)
}

// 取消订单
export const cancelOrder = (id: number) => {
  request.put<any, void>(`/order/order/cancel/${id}`)
}

// 确认收货
export const confirmReceive = (id: number) => {
  return request.put<any, void>(`/order/order/receive/${id}`)
}
