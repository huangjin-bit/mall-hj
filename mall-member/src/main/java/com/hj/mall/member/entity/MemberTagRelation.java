package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员标签关联实体
 */
@Data
@TableName("ums_member_tag_relation")
public class MemberTagRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 来源：system->系统；manual->手动；auto->自动
     */
    private String source;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
