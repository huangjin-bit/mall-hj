package com.hj.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券选项VO — 确认订单页展示可用优惠券
 */
@Data
public class CouponOptionVo {

    private Long id;

    private String couponName;

    private BigDecimal amount;

    private BigDecimal minPoint;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
