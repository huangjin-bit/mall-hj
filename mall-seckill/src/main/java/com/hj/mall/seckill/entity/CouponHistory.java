package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券领取历史实体
 */
@Data
@TableName("sms_coupon_history")
public class CouponHistory {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 优惠券类型：0->满减券；1->折扣券；2->立减券
     */
    private Integer couponType;

    /**
     * 优惠券金额
     */
    private BigDecimal amount;

    /**
     * 状态：0->未使用；1->已使用；2->已过期
     */
    private Integer status;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
