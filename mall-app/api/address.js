import { get, post, put, del } from './request'

/**
 * 获取地址列表
 * @returns {Promise} - 返回地址列表
 */
export function getAddressList() {
  return get('/member/address/list')
}

/**
 * 获取地址详情
 * @param {Number} addressId - 地址ID
 * @returns {Promise} - 返回地址详情
 */
export function getAddressDetail(addressId) {
  return get(`/member/address/${addressId}`)
}

/**
 * 新增地址
 * @param {Object} data - 地址数据
 * @returns {Promise} - 返回新增结果
 */
export function saveAddress(data) {
  return post('/member/address', data)
}

/**
 * 更新地址
 * @param {Object} data - 地址数据
 * @returns {Promise} - 返回更新结果
 */
export function updateAddress(data) {
  return put('/member/address', data)
}

/**
 * 删除地址
 * @param {Number} addressId - 地址ID
 * @returns {Promise} - 返回删除结果
 */
export function deleteAddress(addressId) {
  return del(`/member/address/${addressId}`)
}

/**
 * 设置默认地址
 * @param {Number} addressId - 地址ID
 * @returns {Promise} - 返回设置结果
 */
export function setDefaultAddress(addressId) {
  return put(`/member/address/${addressId}/default`)
}
