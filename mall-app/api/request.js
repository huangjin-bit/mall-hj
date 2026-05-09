const BASE_URL = 'http://localhost:8080/api'

/**
 * 统一请求封装
 * @param {Object} options - 请求配置
 * @returns {Promise} - 返回Promise
 */
function request(options) {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token')

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...options.header
      },
      success: (res) => {
        const { code, msg, data } = res.data

        if (code === 200) {
          resolve(data)
        } else if (code === 401) {
          // Token过期或未登录，清除本地存储并跳转登录页
          uni.removeStorageSync('token')
          uni.removeStorageSync('memberId')
          uni.removeStorageSync('username')
          uni.redirectTo({
            url: '/pages/auth/login'
          })
          reject(new Error(msg || '登录已过期，请重新登录'))
        } else {
          uni.showToast({
            title: msg || '请求失败',
            icon: 'none',
            duration: 2000
          })
          reject(new Error(msg || '请求失败'))
        }
      },
      fail: (err) => {
        console.error('[Request Error]', err)
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none',
          duration: 2000
        })
        reject(err)
      }
    })
  })
}

/**
 * GET请求
 */
export function get(url, data) {
  return request({ url, method: 'GET', data })
}

/**
 * POST请求
 */
export function post(url, data) {
  return request({ url, method: 'POST', data })
}

/**
 * PUT请求
 */
export function put(url, data) {
  return request({ url, method: 'PUT', data })
}

/**
 * DELETE请求
 */
export function del(url, data) {
  return request({ url, method: 'DELETE', data })
}

export default {
  get,
  post,
  put,
  del
}
