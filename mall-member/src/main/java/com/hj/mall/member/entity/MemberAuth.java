package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员认证实体（支持多端登录）
 */
@Data
@TableName("ums_member_auth")
public class MemberAuth {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 认证类型：username、phone、email、wechat、qq等
     */
    private String identityType;

    /**
     * 认证标识（如手机号、邮箱、第三方openid）
     */
    private String identifier;

    /**
     * 认证凭证（加密后的密码）
     */
    private String credential;

    /**
     * 是否验证：0->未验证；1->已验证
     */
    private Integer verified;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
