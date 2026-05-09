package com.hj.mall.auth.service;

import com.hj.mall.auth.dto.TokenDTO;
import com.hj.mall.common.result.Result;

/**
 * 认证服务接口
 * 处理登录、注册、Token 验证等核心认证逻辑
 */
public interface AuthService {

    /**
     * 用户名密码登录
     *
     * @param username   用户名
     * @param password   密码
     * @param ip         客户端IP
     * @param userAgent  用户代理
     * @return Result<TokenDTO> 登录成功返回 Token 信息
     */
    Result<TokenDTO> loginByUsername(String username, String password, String ip, String userAgent);

    /**
     * 手机验证码登录
     *
     * @param phone      手机号
     * @param code       短信验证码
     * @param ip         客户端IP
     * @param userAgent  用户代理
     * @return Result<TokenDTO> 登录成功返回 Token 信息
     */
    Result<TokenDTO> loginByPhone(String phone, String code, String ip, String userAgent);

    /**
     * 用户注册
     *
     * @param username  用户名
     * @param phone     手机号
     * @param password  密码
     * @return Result<Void> 注册成功
     */
    Result<Void> register(String username, String phone, String password);

    /**
     * 发送短信验证码
     *
     * @param phone  手机号
     * @return Result<Void> 发送成功
     */
    Result<Void> sendSmsCode(String phone);

    /**
     * 验证 Token 并返回会员ID
     *
     * @param token  JWT Token
     * @return Result<Long> 返回会员ID
     */
    Result<Long> validateToken(String token);
}
