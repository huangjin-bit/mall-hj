package com.hj.mall.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token 响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    /**
     * JWT Token
     */
    private String token;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 用户名
     */
    private String username;
}
