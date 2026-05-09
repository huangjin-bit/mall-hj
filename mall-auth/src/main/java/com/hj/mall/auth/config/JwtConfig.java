package com.hj.mall.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性
 * 从 application.yml 读取 jwt.* 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT 密钥（必须至少 256 位用于 HS256）
     */
    private String secret;

    /**
     * Token 过期时间（毫秒），默认 24 小时
     */
    private Long expiration;

    /**
     * Token 前缀，默认 "Bearer "
     */
    private String tokenPrefix;

    /**
     * HTTP 头名称，默认 Authorization
     */
    private String header;
}
