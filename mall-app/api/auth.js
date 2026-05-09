import { post } from './request'

/**
 * 用户名密码登录
 * @param {Object} data - 登录数据 {username, password}
 * @returns {Promise} - 返回登录结果
 */
export function loginByUsername(data) {
  return post('/member/auth/login', data)
}

/**
 * 手机号登录
 * @param {Object} data - 登录数据 {phone, smsCode}
 * @returns {Promise} - 返回登录结果
 */
export function loginByPhone(data) {
  return post('/member/auth/login/phone', data)
}

/**
 * 用户注册
 * @param {Object} data - 注册数据 {username, phone, password, smsCode}
 * @returns {Promise} - 返回注册结果
 */
export function register(data) {
  return post('/member/auth/register', data)
}

/**
 * 发送短信验证码
 * @param {Object} data - 发送验证码数据 {phone}
 * @returns {Promise} - 返回发送结果
 */
export function sendSmsCode(data) {
  return post('/member/sms/send', data)
}

/**
 * 退出登录
 * @returns {Promise} - 返回退出结果
 */
export function logout() {
  return post('/member/auth/logout')
}
