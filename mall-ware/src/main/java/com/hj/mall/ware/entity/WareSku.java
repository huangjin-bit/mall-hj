package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("wms_ware_sku")
public class WareSku {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long wareId;

    private Long skuId;

    private Long spuId;

    private Integer stock;

    private Integer stockLocked;

    private Integer stockTotal;

    private Integer lowStock;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
