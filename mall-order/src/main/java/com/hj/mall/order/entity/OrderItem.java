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
}
