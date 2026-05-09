package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品会员价格实体
 */
@Data
@TableName("sms_member_price")
public class MemberPrice {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品SKU ID
     */
    private Long skuId;

    /**
     * 会员等级ID
     */
    private Long memberLevelId;

    /**
     * 会员等级名称
     */
    private String memberLevelName;

    /**
     * 会员价格
     */
    private BigDecimal memberPrice;

    /**
     * 是否可叠加其他优惠：0->不可叠加；1->可叠加
     */
    private Integer addOther;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
