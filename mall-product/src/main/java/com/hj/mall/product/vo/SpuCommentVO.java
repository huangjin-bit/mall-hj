package com.hj.mall.product.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SpuCommentVO {

    private Long id;
    private Long skuId;
    private Long spuId;
    private String spuName;
    private String memberNickname;
    private String memberAvatar;
    private Integer star;
    private String content;
    private String resources;
    private String skuAttributes;
    private Integer likesCount;
    private Integer replyCount;
    private Integer showStatus;
    private LocalDateTime createTime;

    /** 回复列表（详情时填充） */
    private List<CommentReplyVO> replies;
}
