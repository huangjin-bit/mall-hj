import { get, post, put, del } from './request'

/**
 * 获取购物车列表
 * @returns {Promise} - 返回购物车数据
 */
export function getCart() {
  return get('/member/cart')
}

/**
 * 添加商品到购物车
 * @param {Object} data - 购物车数据 {skuId, count}
 * @returns {Promise} - 返回添加结果
 */
export function addToCart(data) {
  return post('/member/cart/add', data)
}

/**
 * 更新购物车商品数量
 * @param {Object} data - 更新数据 {skuId, count}
 * @returns {Promise} - 返回更新结果
 */
export function updateCartItem(data) {
  return put('/member/cart/count', data)
}

/**
 * 选中/取消选中购物车商品
 * @param {Object} data - 选中数据 {skuId, checked}
 * @returns {Promise} - 返回更新结果
 */
export function checkCartItem(data) {
  return put('/member/cart/check', data)
}

/**
 * 全选/取消全选
 * @param {Object} data - 全选数据 {checked}
 * @returns {Promise} - 返回更新结果
 */
export function checkAllCart(data) {
  return put('/member/cart/check/all', data)
}

/**
 * 删除购物车商品
 * @param {Object} data - 删除数据 {skuIds}
 * @returns {Promise} - 返回删除结果
 */
export function deleteCartItems(data) {
  return del('/member/cart', data)
}

/**
 * 清空购物车
 * @returns {Promise} - 返回清空结果
 */
export function clearCart() {
  return del('/member/cart/clear')
}
