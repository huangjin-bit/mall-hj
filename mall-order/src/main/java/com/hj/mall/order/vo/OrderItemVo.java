package com.hj.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单项VO — 确认订单页中的每一个商品项
 */
@Data
public class OrderItemVo {

    private Long skuId;
    private String title;
    private String image;
    /** 销售属性组合，如 ["颜色:白色", "内存:128G"] */
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    /** 是否有库存 */
    private Boolean hasStock;
}
