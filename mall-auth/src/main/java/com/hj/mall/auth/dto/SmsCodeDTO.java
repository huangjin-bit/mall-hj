package com.hj.mall.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsCodeDTO {

    /**
     * 验证码（开发环境返回，生产环境不返回）
     */
    private String code;

    /**
     * 过期时间（秒）
     */
    private Integer expireSeconds;

    /**
     * 提示信息
     */
    private String message;
}
