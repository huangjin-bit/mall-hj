package com.hj.mall.member.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单详情 VO（含对话消息列表）
 */
@Data
public class TicketDetailVO {

    private Long id;
    private Long memberId;
    private String memberName;
    private String title;
    private String content;
    private Integer ticketType;
    private String orderSn;
    private Long skuId;
    private Integer status;
    private String handler;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 对话消息列表 */
    private List<MessageVO> messages;

    @Data
    public static class MessageVO {
        private Long id;
        private Integer senderType;
        private String senderName;
        private String content;
        private String attachments;
        private LocalDateTime createTime;
    }
}
