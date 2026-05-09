package com.hj.mall.auth.service;

/**
 * Token 管理服务接口
 * 负责 Token 的缓存、验证、失效等操作
 */
public interface TokenService {

    /**
     * 缓存 Token 到 Redis
     *
     * @param memberId  会员ID
     * @param token     JWT Token
     */
    void cacheToken(Long memberId, String token);

    /**
     * 移除 Token（登出时使用）
     *
     * @param memberId  会员ID
     */
    void removeToken(Long memberId);

    /**
     * 验证 Token 是否有效
     *
     * @param memberId  会员ID
     * @param token     JWT Token
     * @return true-有效, false-无效
     */
    boolean isTokenValid(Long memberId, String token);
}
