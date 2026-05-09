package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_sku_sale_attr_value")
public class SkuSaleAttrValue {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long skuId;

    private Long spuId;

    private Long attrId;

    private String attrName;

    private String attrValue;

    private Integer sort;

    private LocalDateTime createTime;
}
