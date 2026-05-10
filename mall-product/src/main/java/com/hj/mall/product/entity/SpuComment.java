package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_spu_comment")
public class SpuComment {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long skuId;

    private Long spuId;

    private String spuName;

    private Long memberId;

    private String memberNickname;

    private String memberAvatar;

    private String skuAttributes;

    /** 星级 1-5 */
    private Integer star;

    private String content;

    /** 评论图片/视频 JSON */
    private String resources;

    /** 评论类型: 0=直接评论, 1=回复 */
    private Integer commentType;

    /** 显示状态: 0=隐藏, 1=显示 */
    private Integer showStatus;

    private Integer likesCount;

    private Integer replyCount;

    private LocalDateTime createTime;
}
