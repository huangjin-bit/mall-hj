package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀订单实体
 */
@Data
@TableName("sms_seckill_order")
public class SeckillOrder {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 秒杀场次ID
     */
    private Long sessionId;

    /**
     * 商品SKU ID
     */
    private Long skuId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 状态：0->无效；1->有效
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
