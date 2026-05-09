package com.hj.mall.auth.util;

import com.hj.mall.auth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 * 负责生成、解析、验证 JWT Token
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private SecretKey signingKey;

    /**
     * 初始化签名密钥
     */
    @PostConstruct
    public void init() {
        this.signingKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(
            jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 生成 JWT Token
     *
     * @param memberId  会员ID
     * @param username  用户名
     * @return JWT Token 字符串
     */
    public String generateToken(Long memberId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());

        String token = Jwts.builder()
            .subject(username)
            .claim("memberId", memberId)
            .claim("username", username)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(signingKey)
            .compact();

        log.debug("[JwtUtil] 生成 Token 成功, memberId={}, username={}", memberId, username);
        return token;
    }

    /**
     * 解析 JWT Token
     *
     * @param token JWT Token
     * @return Claims 对象，包含所有声明
     */
    public Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            log.debug("[JwtUtil] 解析 Token 成功, username={}", claims.getSubject());
            return claims;
        } catch (SignatureException e) {
            log.error("[JwtUtil] Token 签名验证失败: {}", e.getMessage());
            throw new RuntimeException("Token 签名无效", e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("[JwtUtil] Token 已过期: {}", e.getMessage());
            throw new RuntimeException("Token 已过期", e);
        } catch (MalformedJwtException e) {
            log.error("[JwtUtil] Token 格式错误: {}", e.getMessage());
            throw new RuntimeException("Token 格式错误", e);
        } catch (Exception e) {
            log.error("[JwtUtil] Token 解析失败: {}", e.getMessage(), e);
            throw new RuntimeException("Token 解析失败", e);
        }
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token
     * @return true-有效, false-无效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.warn("[JwtUtil] Token 验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从 Token 中获取会员ID
     *
     * @param token JWT Token
     * @return 会员ID
     */
    public Long getMemberId(String token) {
        Claims claims = parseToken(token);
        return claims.get("memberId", Long.class);
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }
}
