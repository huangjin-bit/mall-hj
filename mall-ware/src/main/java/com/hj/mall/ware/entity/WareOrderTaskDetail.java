package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("wms_ware_order_task_detail")
public class WareOrderTaskDetail {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long taskId;

    private Long wareId;

    private Long skuId;

    private String skuName;

    private Integer lockQuantity;

    private Integer taskDetailStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
