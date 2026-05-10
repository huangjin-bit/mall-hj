package com.hj.mall.member.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建客服工单 VO
 */
@Data
public class TicketCreateVO {

    @NotBlank(message = "工单标题不能为空")
    private String title;

    @NotBlank(message = "工单内容不能为空")
    private String content;

    /**
     * 工单类型：1-商品咨询 2-订单问题 3-售后退换 4-账户问题 5-其他
     */
    @NotNull(message = "工单类型不能为空")
    private Integer ticketType;

    /** 关联订单号（可空） */
    private String orderSn;

    /** 关联SKU ID（可空） */
    private Long skuId;
}
