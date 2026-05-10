package com.hj.mall.product.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentReplyVO {

    private Long id;
    private Long commentId;
    private Long replyId;
    private String memberNickname;
    private String memberAvatar;
    private String content;
    private LocalDateTime createTime;
}
