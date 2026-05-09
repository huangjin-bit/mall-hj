package com.hj.mall.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员认证实体
 * 用于存储不同身份类型的认证凭据
 */
@Data
@TableName("ums_member_auth")
public class MemberAuth {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 身份类型：USERNAME-用户名密码, PHONE-手机号, WECHAT-微信, QQ-QQ等
     */
    private String identityType;

    /**
     * 标识符（用户名、手机号、第三方openid等）
     */
    private String identifier;

    /**
     * 凭据（密码、凭证等）
     */
    private String credential;

    /**
     * 是否已验证：0-未验证 1-已验证
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
