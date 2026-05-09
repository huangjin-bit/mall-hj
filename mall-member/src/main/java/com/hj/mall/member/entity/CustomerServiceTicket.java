package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客服工单实体
 */
@Data
@TableName("ums_customer_service_ticket")
public class CustomerServiceTicket {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 工单标题
     */
    private String title;

    /**
     * 工单内容
     */
    private String content;

    /**
     * 工单类型：1->售前咨询；2->售后问题；3->投诉；4->建议
     */
    private Integer ticketType;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 商品SKU ID
     */
    private Long skuId;

    /**
     * 状态：0->待处理；1->处理中；2->已解决；3->已关闭
     */
    private Integer status;

    /**
     * 处理人
     */
    private String handler;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
