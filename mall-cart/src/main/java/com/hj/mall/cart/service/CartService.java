package com.hj.mall.cart.service;

import com.hj.mall.cart.model.CartItem;
import com.hj.mall.cart.vo.CartVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 获取购物车
     *
     * @param memberId 用户ID
     * @return 购物车视图对象
     */
    CartVO getCart(Long memberId);

    /**
     * 添加商品到购物车
     * 如果商品已存在，则增加数量
     *
     * @param memberId  用户ID
     * @param skuId     SKU ID
     * @param quantity  数量
     */
    void addToCart(Long memberId, Long skuId, Integer quantity);

    /**
     * 更新购物车商品数量
     *
     * @param memberId  用户ID
     * @param skuId     SKU ID
     * @param quantity  新数量
     */
    void updateQuantity(Long memberId, Long skuId, Integer quantity);

    /**
     * 选中/取消选中商品
     *
     * @param memberId  用户ID
     * @param skuId     SKU ID
     * @param checked   是否选中
     */
    void checkItem(Long memberId, Long skuId, Boolean checked);

    /**
     * 全选/取消全选
     *
     * @param memberId  用户ID
     * @param checked   是否选中
     */
    void checkAll(Long memberId, Boolean checked);

    /**
     * 删除购物车商品
     *
     * @param memberId  用户ID
     * @param skuId     SKU ID
     */
    void deleteItem(Long memberId, Long skuId);

    /**
     * 批量删除购物车商品
     *
     * @param memberId  用户ID
     * @param skuIds    SKU ID 列表
     */
    void deleteItems(Long memberId, List<Long> skuIds);

    /**
     * 获取选中的商品列表
     *
     * @param memberId  用户ID
     * @return 选中的商品列表
     */
    List<CartItem> getCheckedItems(Long memberId);

    /**
     * 清除选中的商品
     *
     * @param memberId  用户ID
     */
    void clearChecked(Long memberId);
}
