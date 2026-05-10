package com.hj.mall.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单传输对象 — 用于服务间数据传递和MQ消息
 */
@Data
public class OrderTo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderSn;
    private Integer status;
    private Long memberId;
}
