package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品满减信息实体
 */
@Data
@TableName("sms_sku_full_reduction")
public class SkuFullReduction {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品SKU ID
     */
    private Long skuId;

    /**
     * 商品SPU ID
     */
    private Long spuId;

    /**
     * 满减条件（满多少）
     */
    private BigDecimal fullPrice;

    /**
     * 优惠金额（减多少）
     */
    private BigDecimal reducePrice;

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
