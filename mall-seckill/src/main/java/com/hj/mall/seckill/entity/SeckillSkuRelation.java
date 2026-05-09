package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀商品关联实体
 */
@Data
@TableName("sms_seckill_sku_relation")
public class SeckillSkuRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 秒杀场次ID
     */
    private Long sessionId;

    /**
     * 商品SKU ID
     */
    private Long skuId;

    /**
     * 商品SPU ID
     */
    private Long spuId;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 秒杀库存
     */
    private Integer seckillStock;

    /**
     * 每人限购数量
     */
    private Integer seckillLimit;

    /**
     * 排序
     */
    private Integer sort;

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
