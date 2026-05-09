package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员标签实体
 */
@Data
@TableName("ums_member_tag")
public class MemberTag {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签类型：system->系统标签；custom->自定义标签
     */
    private String tagType;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 状态：0->禁用；1->启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
