package com.hj.mall.auth.dto;

import lombok.Data;

/**
 * 登录请求 DTO
 * 支持用户名密码登录和手机验证码登录
 */
@Data
public class LoginDTO {

    /**
     * 用户名（用户名密码登录时使用）
     */
    private String username;

    /**
     * 密码（用户名密码登录时使用）
     */
    private String password;

    /**
     * 手机号（手机验证码登录时使用）
     */
    private String phone;

    /**
     * 短信验证码（手机验证码登录时使用）
     */
    private String smsCode;
}
