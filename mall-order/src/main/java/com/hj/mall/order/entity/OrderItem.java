package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_order_item")
public class OrderItem {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long orderId;

    private String orderSn;

    private Long spuId;

    private Long skuId;

    private String spuName;

    private String skuName;

    private String skuImg;

    private BigDecimal skuPrice;

    private Integer skuQuantity;

    private BigDecimal skuTotalPrice;

    private BigDecimal promotionAmount;

    private BigDecimal couponAmount;

    private LocalDateTime createTime;

    // SPU图片
    private String spuPic;

    // SPU品牌
    private String spuBrand;

    // 分类ID
    private Long categoryId;

    // SKU属性值
    private String skuAttrsVals;

    // 积分抵扣金额
    private BigDecimal integrationAmount;

    // 实际支付金额
    private BigDecimal realAmount;

    // 赠送积分
    private Integer giftIntegration;

    // 赠送成长值
    private Integer giftGrowth;
}
