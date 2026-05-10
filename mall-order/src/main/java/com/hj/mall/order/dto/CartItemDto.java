package com.hj.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车项DTO - 用于Feign远程调用
 * 避免直接依赖mall-cart模块的CartItem实体
 */
@Data
public class CartItemDto {

    /** 主键ID */
    private Long id;

    /** 商品ID */
    private Long skuId;

    /** 商品标题 */
    private String title;

    /** 商品图片 */
    private String image;

    /** 商品价格 */
    private BigDecimal price;

    /** 购买数量 */
    private Integer count;

    /** 是否选中 */
    private Boolean check;

    /** 用户ID */
    private Long memberId;

    /** SPU ID */
    private Long spuId;

    /** SKU属性组合字符串 */
    private String skuAttr;

    /** 品牌ID */
    private Long brandId;

    /** 分类ID */
    private Long categoryId;

    /** 创建时间 */
    private String createTime;
}