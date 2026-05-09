package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_order_return_apply")
public class OrderReturnApply {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderSn;

    private Long orderId;

    private Long skuId;

    private Long memberId;

    private Integer returnType;

    private String reason;

    private String description;

    private String proofImgs;

    private BigDecimal returnAmount;

    private Integer status;

    private Long handleBy;

    private String handleNote;

    private LocalDateTime handleTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
