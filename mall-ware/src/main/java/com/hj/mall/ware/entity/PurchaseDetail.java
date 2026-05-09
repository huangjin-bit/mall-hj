package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("wms_purchase_detail")
public class PurchaseDetail {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long purchaseId;

    private Long skuId;

    private Long spuId;

    private String skuName;

    private BigDecimal skuPrice;

    private Integer quantity;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
