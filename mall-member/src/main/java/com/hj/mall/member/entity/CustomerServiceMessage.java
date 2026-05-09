package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客服消息实体
 */
@Data
@TableName("ums_customer_service_message")
public class CustomerServiceMessage {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工单ID
     */
    private Long ticketId;

    /**
     * 发送者类型：0->会员；1->客服
     */
    private Integer senderType;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 附件（JSON格式存储）
     */
    private String attachments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
