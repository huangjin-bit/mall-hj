package com.hj.mall.member.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 工单回复 VO
 */
@Data
public class TicketReplyVO {

    @NotBlank(message = "回复内容不能为空")
    private String content;

    /** 附件URL（JSON数组，可空） */
    private String attachments;
}
