package com.hj.mall.member.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 会员登录请求VO
 */
@Data
public class MemberLoginVO {

    @NotBlank(message = "账号不能为空")
    private String loginAccount;

    @NotBlank(message = "密码不能为空")
    private String password;
}
