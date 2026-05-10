package com.hj.mall.order.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提交订单VO — 用户点击"提交订单"时传入的参数
 *
 * 注意：无需传商品信息，后端从购物车重新获取（防篡改）
 */
@Data
public class OrderSubmitVo {

    /** 收货地址id */
    @NotNull(message = "收货地址不能为空")
    private Long addrId;

    /** 支付方式 */
    private Integer payType;

    /** 防重令牌 */
    @NotBlank(message = "防重令牌不能为空")
    private String orderToken;

    /** 应付价格（用于验价，前端计算的总价） */
    @NotNull(message = "应付金额不能为空")
    private BigDecimal payPrice;

    /** 使用的优惠券ID */
    private Long couponId;

    /** 订单备注 */
    private String note;
}
