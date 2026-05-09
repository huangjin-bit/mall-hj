package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员收藏专题实体
 */
@Data
@TableName("ums_member_collect_subject")
public class MemberCollectSubject {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 专题ID
     */
    private Long subjectId;

    /**
     * 专题名称
     */
    private String subjectName;

    /**
     * 专题图片
     */
    private String subjectImg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
