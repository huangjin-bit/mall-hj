package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("wms_stock_log")
public class StockLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long wareId;

    private Long skuId;

    private String changeType;

    private Integer changeAmount;

    private Integer beforeStock;

    private Integer afterStock;

    private Integer beforeLocked;

    private Integer afterLocked;

    private String orderSn;

    private Long purchaseId;

    private String remark;

    private LocalDateTime createTime;
}
