package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券与商品关联实体
 */
@Data
@TableName("sms_coupon_spu_relation")
public class CouponSpuRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 商品SPU ID
     */
    private Long spuId;

    /**
     * 商品SPU名称
     */
    private String spuName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
