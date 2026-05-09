import { get, put } from './request'

/**
 * 获取会员信息
 * @returns {Promise} - 返回会员信息
 */
export function getMemberInfo() {
  return get('/member/info')
}

/**
 * 更新会员信息
 * @param {Object} data - 会员数据
 * @returns {Promise} - 返回更新结果
 */
export function updateMember(data) {
  return put('/member/info', data)
}

/**
 * 更新会员头像
 * @param {String} avatarUrl - 头像URL
 * @returns {Promise} - 返回更新结果
 */
export function updateAvatar(avatarUrl) {
  return put('/member/info/avatar', { avatarUrl })
}

/**
 * 修改密码
 * @param {Object} data - 密码数据 {oldPassword, newPassword}
 * @returns {Promise} - 返回修改结果
 */
export function changePassword(data) {
  return put('/member/password', data)
}

/**
 * 重置密码
 * @param {Object} data - 重置密码数据 {phone, smsCode, newPassword}
 * @returns {Promise} - 返回重置结果
 */
export function resetPassword(data) {
  return put('/member/password/reset', data)
}
