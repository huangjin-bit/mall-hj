package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_comment_replay")
public class CommentReply {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long commentId;

    private Long replyId;

    private Long memberId;

    private String memberNickname;

    private String memberAvatar;

    private String content;

    private LocalDateTime createTime;
}
