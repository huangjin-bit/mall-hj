/**
 * 格式化日期时间
 * @param {Date|String|Number} date - 日期对象、日期字符串或时间戳
 * @param {String} format - 格式化模板，默认 'YYYY-MM-DD HH:mm:ss'
 * @returns {String} - 格式化后的日期字符串
 */
export function formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''

  const d = new Date(date)
  if (isNaN(d.getTime())) return ''

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const minute = String(d.getMinutes()).padStart(2, '0')
  const second = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hour)
    .replace('mm', minute)
    .replace('ss', second)
}

/**
 * 格式化价格
 * @param {Number} price - 价格数值
 * @param {Number} decimals - 小数位数，默认2位
 * @returns {String} - 格式化后的价格字符串
 */
export function formatPrice(price, decimals = 2) {
  if (typeof price !== 'number') {
    price = parseFloat(price) || 0
  }
  return price.toFixed(decimals)
}

/**
 * 格式化价格（带符号）
 * @param {Number} price - 价格数值
 * @returns {String} - 格式化后的价格字符串，带¥符号
 */
export function formatPriceWithSymbol(price) {
  return `¥${formatPrice(price)}`
}

/**
 * 格式化倒计时时间
 * @param {Number} seconds - 剩余秒数
 * @returns {String} - 格式化后的时间字符串 HH:mm:ss
 */
export function formatCountdown(seconds) {
  if (seconds <= 0) return '00:00:00'

  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60

  return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(secs).padStart(2, '0')}`
}

/**
 * 格式化数量（带单位）
 * @param {Number} count - 数量
 * @returns {String} - 格式化后的数量字符串
 */
export function formatCount(count) {
  if (count >= 10000) {
    return `${(count / 10000).toFixed(1)}万`
  } else if (count >= 1000) {
    return `${(count / 1000).toFixed(1)}千`
  }
  return String(count)
}

/**
 * 防抖函数
 * @param {Function} func - 要执行的函数
 * @param {Number} delay - 延迟时间（毫秒）
 * @returns {Function} - 防抖后的函数
 */
export function debounce(func, delay = 300) {
  let timer = null
  return function (...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      func.apply(this, args)
    }, delay)
  }
}

/**
 * 节流函数
 * @param {Function} func - 要执行的函数
 * @param {Number} delay - 延迟时间（毫秒）
 * @returns {Function} - 节流后的函数
 */
export function throttle(func, delay = 300) {
  let timer = null
  return function (...args) {
    if (timer) return
    timer = setTimeout(() => {
      func.apply(this, args)
      timer = null
    }, delay)
  }
}

/**
 * 深拷贝对象
 * @param {Any} obj - 要拷贝的对象
 * @returns {Any} - 拷贝后的新对象
 */
export function deepClone(obj) {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime())
  if (obj instanceof Array) return obj.map(item => deepClone(item))

  const clonedObj = {}
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      clonedObj[key] = deepClone(obj[key])
    }
  }
  return clonedObj
}

/**
 * 获取图片完整URL
 * @param {String} path - 图片路径
 * @returns {String} - 完整URL
 */
export function getImageUrl(path) {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) {
    return path
  }
  return `http://localhost:8080${path}`
}

/**
 * 验证手机号
 * @param {String} phone - 手机号
 * @returns {Boolean} - 是否有效
 */
export function validatePhone(phone) {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 验证邮箱
 * @param {String} email - 邮箱
 * @returns {Boolean} - 是否有效
 */
export function validateEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

/**
 * 验证密码强度（6-20位，包含字母和数字）
 * @param {String} password - 密码
 * @returns {Boolean} - 是否有效
 */
export function validatePassword(password) {
  return /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/.test(password)
}

export default {
  formatDate,
  formatPrice,
  formatPriceWithSymbol,
  formatCountdown,
  formatCount,
  debounce,
  throttle,
  deepClone,
  getImageUrl,
  validatePhone,
  validateEmail,
  validatePassword
}
