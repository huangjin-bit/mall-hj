package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品阶梯价格实体
 */
@Data
@TableName("sms_sku_ladder")
public class SkuLadder {

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
     * 满件数（买几件）
     */
    private Integer fullCount;

    /**
     * 折扣（如85折传85）
     */
    private BigDecimal discount;

    /**
     * 折后价格
     */
    private BigDecimal price;

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
