package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_refund_info")
public class RefundInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long orderReturnId;

    private String orderSn;

    private String refundSn;

    private BigDecimal refundAmount;

    private Integer refundStatus;

    private Integer refundChannel;

    private String refundContent;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 退款金额
    private BigDecimal refund;
}
