package com.hj.mall.auth.service.impl;

import com.hj.mall.auth.config.JwtConfig;
import com.hj.mall.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token 管理服务实现
 * 使用 Redis 缓存 Token，实现分布式会话管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtConfig jwtConfig;

    private static final String TOKEN_PREFIX = "auth:token:";

    @Override
    public void cacheToken(Long memberId, String token) {
        String key = TOKEN_PREFIX + memberId;
        long expireSeconds = jwtConfig.getExpiration() / 1000; // 转换为秒

        stringRedisTemplate.opsForValue().set(key, token, expireSeconds, TimeUnit.SECONDS);
        log.debug("[TokenServiceImpl] 缓存 Token 成功, memberId={}, expireSeconds={}", memberId, expireSeconds);
    }

    @Override
    public void removeToken(Long memberId) {
        String key = TOKEN_PREFIX + memberId;
        stringRedisTemplate.delete(key);
        log.info("[TokenServiceImpl] 移除 Token 成功, memberId={}", memberId);
    }

    @Override
    public boolean isTokenValid(Long memberId, String token) {
        String key = TOKEN_PREFIX + memberId;
        String cachedToken = stringRedisTemplate.opsForValue().get(key);

        boolean isValid = token.equals(cachedToken);
        if (!isValid) {
            log.warn("[TokenServiceImpl] Token 验证失败, memberId={}", memberId);
        }

        return isValid;
    }
}
