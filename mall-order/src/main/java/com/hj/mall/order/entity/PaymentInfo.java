package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_payment_info")
public class PaymentInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderSn;

    private String paySn;

    private Integer payType;

    private BigDecimal payAmount;

    private Integer status;

    private String callbackContent;

    private LocalDateTime callbackTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 订单ID
    private Long orderId;

    // 支付宝交易号
    private String alipayTradeNo;

    // 订单总金额
    private BigDecimal totalAmount;

    // 支付主题
    private String subject;

    // 支付状态
    private String paymentStatus;

    // 确认时间
    private LocalDateTime confirmTime;
}
