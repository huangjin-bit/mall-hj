import { get, post } from './request'

/**
 * 获取当前活跃的秒杀场次
 * @returns {Promise} - 返回秒杀场次列表
 */
export function getActiveSessions() {
  return get('/seckill/session/active')
}

/**
 * 获取秒杀场次的商品列表
 * @param {Number} sessionId - 场次ID
 * @returns {Promise} - 返回秒杀商品列表
 */
export function getSessionSkus(sessionId) {
  return get(`/seckill/session/${sessionId}/skus`)
}

/**
 * 获取秒杀商品详情
 * @param {Number} skuId - SKU ID
 * @returns {Promise} - 返回秒杀商品详情
 */
export function getSeckillSkuDetail(skuId) {
  return get(`/seckill/sku/${skuId}`)
}

/**
 * 创建秒杀订单
 * @param {Object} data - 订单数据 {skuId, count, addressId}
 * @returns {Promise} - 返回创建结果
 */
export function createSeckillOrder(data) {
  return post('/seckill/order/create', data)
}

/**
 * 获取可用优惠券
 * @param {Object} params - 查询参数 {spuId, skuId, price}
 * @returns {Promise} - 返回可用优惠券列表
 */
export function getAvailableCoupons(params) {
  return get('/member/coupon/available', params)
}

/**
 * 领取优惠券
 * @param {Number} couponId - 优惠券ID
 * @returns {Promise} - 返回领取结果
 */
export function claimCoupon(couponId) {
  return post(`/member/coupon/${couponId}/claim`)
}

/**
 * 获取我的优惠券列表
 * @param {Object} params - 查询参数 {status, page, size}
 * @returns {Promise} - 返回优惠券列表
 */
export function getMyCoupons(params) {
  return get('/member/coupon/list', params)
}

/**
 * 获取秒杀时间配置
 * @returns {Promise} - 返回秒杀时间段配置
 */
export function getSeckillTimeConfig() {
  return get('/seckill/config/time')
}
