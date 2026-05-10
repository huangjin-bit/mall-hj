package com.hj.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 确认订单页VO — 展示给用户确认的订单信息
 *
 * 京东下单流程：购物车勾选 → 确认订单页（地址、商品、优惠、运费、总价）→ 提交订单
 */
@Data
public class OrderConfirmVo {

    /** 收货地址列表 */
    private List<MemberAddressVo> addresses;

    /** 购物车中选中的商品项 */
    private List<OrderItemVo> items;

    /** 会员积分 */
    private Integer integration;

    /** 防重令牌（提交订单时校验，防止重复提交） */
    private String orderToken;

    /** 库存信息 Map<skuId, hasStock> */
    private Map<Long, Boolean> stocks;

    /** 当前用户可用优惠券 */
    private List<CouponOptionVo> coupons;

    /** 订单总额（自动计算） */
    public BigDecimal getTotal() {
        if (items == null) return BigDecimal.ZERO;
        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** 应付金额（暂不扣优惠） */
    public BigDecimal getPayPrice() {
        return getTotal();
    }
}
