package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("pms_sku_info")
public class SkuInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long spuId;

    private String skuName;

    private String skuDesc;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String skuImg;

    private BigDecimal weight;

    /** 0下架 1上架 */
    private Integer publishStatus;

    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
