package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("wms_ware_order_task")
public class WareOrderTask {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderSn;

    private Long orderId;

    private Long wareId;

    private Integer taskStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
