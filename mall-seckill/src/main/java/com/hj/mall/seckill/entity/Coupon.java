package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体
 */
@Data
@TableName("sms_coupon")
public class Coupon {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 优惠券类型：0->满减券；1->折扣券；2->立减券
     */
    private Integer couponType;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 金额（满减券、立减券使用）
     */
    private BigDecimal amount;

    /**
     * 最低消费金额（满减券使用）
     */
    private BigDecimal minPoint;

    /**
     * 折扣（折扣券使用，如85折传85）
     */
    private BigDecimal discount;

    /**
     * 最大优惠金额（折扣券使用）
     */
    private BigDecimal maxDiscountAmount;

    /**
     * 发行数量
     */
    private Integer totalCount;

    /**
     * 已领取数量
     */
    private Integer receivedCount;

    /**
     * 已使用数量
     */
    private Integer usedCount;

    /**
     * 每人限领数量
     */
    private Integer perLimit;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态：0->禁用；1->启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
