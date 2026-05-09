import { get, post, put } from './request'

/**
 * 获取订单列表
 * @param {Object} params - 查询参数 {status, page, size}
 * @returns {Promise} - 返回订单列表
 */
export function getOrderList(params) {
  return get('/member/order/page', params)
}

/**
 * 获取订单详情
 * @param {String} orderSn - 订单编号
 * @returns {Promise} - 返回订单详情
 */
export function getOrderDetail(orderSn) {
  return get(`/member/order/${orderSn}`)
}

/**
 * 创建订单
 * @param {Object} data - 订单数据
 * @returns {Promise} - 返回创建结果
 */
export function createOrder(data) {
  return post('/member/order', data)
}

/**
 * 取消订单
 * @param {String} orderSn - 订单编号
 * @returns {Promise} - 返回取消结果
 */
export function cancelOrder(orderSn) {
  return put(`/member/order/${orderSn}/cancel`)
}

/**
 * 确认收货
 * @param {String} orderSn - 订单编号
 * @returns {Promise} - 返回确认结果
 */
export function confirmReceive(orderSn) {
  return put(`/member/order/${orderSn}/receive`)
}

/**
 * 删除订单
 * @param {String} orderSn - 订单编号
 * @returns {Promise} - 返回删除结果
 */
export function deleteOrder(orderSn) {
  return put(`/member/order/${orderSn}/delete`)
}

/**
 * 获取订单数量统计
 * @returns {Promise} - 返回各状态订单数量
 */
export function getOrderCount() {
  return get('/member/order/count')
}

/**
 * 订单支付
 * @param {String} orderSn - 订单编号
 * @param {Object} data - 支付数据 {payType}
 * @returns {Promise} - 返回支付结果
 */
export function payOrder(orderSn, data) {
  return post(`/member/order/${orderSn}/pay`, data)
}

/**
 * 查询支付状态
 * @param {String} orderSn - 订单编号
 * @returns {Promise} - 返回支付状态
 */
export function getPayStatus(orderSn) {
  return get(`/member/order/${orderSn}/pay/status`)
}
