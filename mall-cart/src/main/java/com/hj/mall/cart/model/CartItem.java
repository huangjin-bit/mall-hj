package com.hj.mall.cart.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车商品项
 */
@Data
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * SKU 名称
     */
    private String skuName;

    /**
     * SPU 名称
     */
    private String spuName;

    /**
     * SKU 图片
     */
    private String skuImg;

    /**
     * SKU 价格
     */
    private BigDecimal price;

    /**
     * SKU 原价
     */
    private BigDecimal originalPrice;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 是否选中
     */
    private Boolean isChecked = true;

    /**
     * 销售属性值列表，如 ["颜色:黑色", "尺寸:XL"]
     */
    private List<String> saleAttrs;
}
