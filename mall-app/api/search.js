import { get } from './request'

/**
 * 搜索商品
 * @param {Object} params - 搜索参数
 * @param {String} params.keyword - 搜索关键词
 * @param {Number} params.categoryId - 分类ID
 * @param {Number} params.brandId - 品牌ID
 * @param {Number} params.minPrice - 最低价格
 * @param {Number} params.maxPrice - 最高价格
 * @param {String} params.sort - 排序方式 (price_asc, price_desc, sales, new)
 * @param {Number} params.page - 页码
 * @param {Number} params.size - 每页数量
 * @returns {Promise} - 返回搜索结果
 */
export function search(params) {
  return get('/product/search', params)
}

/**
 * 获取搜索历史
 * @returns {Promise} - 返回搜索历史
 */
export function getSearchHistory() {
  return get('/product/search/history')
}

/**
 * 清除搜索历史
 * @returns {Promise} - 返回清除结果
 */
export function clearSearchHistory() {
  return get('/product/search/history/clear')
}

/**
 * 获取热门搜索关键词
 * @returns {Promise} - 返回热门搜索关键词
 */
export function getHotKeywords() {
  return get('/product/search/hot')
}
