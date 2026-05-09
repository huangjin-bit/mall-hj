import { get, post, del } from './request'

/**
 * 添加商品收藏
 * @param {Number} spuId - SPU ID
 * @returns {Promise} - 返回添加结果
 */
export function addCollect(spuId) {
  return post('/member/collect/add', { spuId })
}

/**
 * 取消收藏
 * @param {Number} spuId - SPU ID
 * @returns {Promise} - 返回取消结果
 */
export function removeCollect(spuId) {
  return del(`/member/collect/${spuId}`)
}

/**
 * 获取收藏列表
 * @param {Object} params - 查询参数 {page, size}
 * @returns {Promise} - 返回收藏列表
 */
export function getCollectList(params) {
  return get('/member/collect/list', params)
}

/**
 * 检查是否已收藏
 * @param {Number} spuId - SPU ID
 * @returns {Promise} - 返回收藏状态
 */
export function checkCollect(spuId) {
  return get(`/member/collect/check/${spuId}`)
}
