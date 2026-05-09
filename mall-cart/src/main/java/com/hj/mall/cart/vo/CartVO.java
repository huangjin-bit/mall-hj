package com.hj.mall.cart.vo;

import com.hj.mall.cart.model.CartItem;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车视图对象
 */
@Data
public class CartVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车商品项列表
     */
    private List<CartItem> items;

    /**
     * 所有商品总数量
     */
    private Integer totalCount;

    /**
     * 选中商品总数量
     */
    private Integer checkedCount;

    /**
     * 选中商品总价
     */
    private BigDecimal totalPrice;

    /**
     * 选中商品原价总和
     */
    private BigDecimal totalOriginalPrice;
}
