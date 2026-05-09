import { get, post } from './request'

/**
 * 获取分类树
 * @returns {Promise} - 返回分类树数据
 */
export function getCategoryTree() {
  return get('/product/category/tree')
}

/**
 * 获取SPU详情
 * @param {Number} spuId - SPU ID
 * @returns {Promise} - 返回SPU详情
 */
export function getSpuDetail(spuId) {
  return get(`/product/spu/${spuId}`)
}

/**
 * 获取SPU列表
 * @param {Object} params - 查询参数 {keyword, categoryId, brandId, minPrice, maxPrice, sort, page, size}
 * @returns {Promise} - 返回SPU列表
 */
export function getSpuList(params) {
  return get('/product/spu/page', params)
}

/**
 * 获取SKU详情
 * @param {Number} skuId - SKU ID
 * @returns {Promise} - 返回SKU详情
 */
export function getSkuDetail(skuId) {
  return get(`/product/sku/${skuId}`)
}

/**
 * 获取商品库存
 * @param {Number} skuId - SKU ID
 * @returns {Promise} - 返回库存信息
 */
export function getSkuStock(skuId) {
  return get(`/product/sku/${skuId}/stock`)
}

/**
 * 获取推荐商品
 * @param {Object} params - 查询参数 {page, size}
 * @returns {Promise} - 返回推荐商品列表
 */
export function getRecommendedProducts(params) {
  return get('/product/spu/recommended', params)
}

/**
 * 获取热门商品
 * @param {Object} params - 查询参数 {page, size}
 * @returns {Promise} - 返回热门商品列表
 */
export function getHotProducts(params) {
  return get('/product/spu/hot', params)
}

/**
 * 获取新品商品
 * @param {Object} params - 查询参数 {page, size}
 * @returns {Promise} - 返回新品商品列表
 */
export function getNewProducts(params) {
  return get('/product/spu/new', params)
}
