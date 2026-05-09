package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券与分类关联实体
 */
@Data
@TableName("sms_coupon_spu_category_relation")
public class CouponSpuCategoryRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
