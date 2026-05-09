package com.hj.mall.auth.controller;

import com.hj.mall.auth.dto.SysLoginDTO;
import com.hj.mall.auth.service.SysUserService;
import com.hj.mall.auth.util.JwtUtil;
import com.hj.mall.auth.vo.AdminLoginVO;
import com.hj.mall.auth.vo.RouterVO;
import com.hj.mall.auth.vo.UserInfoVO;
import com.hj.mall.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 系统登录控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys")
@RequiredArgsConstructor
public class SysLoginController {

    private final SysUserService sysUserService;
    private final JwtUtil jwtUtil;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@Valid @RequestBody SysLoginDTO loginDTO, HttpServletRequest request) {
        String ip = getClientIp(request);

        log.info("[SysLoginController] 管理员登录请求, username={}, ip={}", loginDTO.getUsername(), ip);

        try {
            AdminLoginVO loginVO = sysUserService.login(loginDTO.getUsername(), loginDTO.getPassword(), ip);
            return Result.ok(loginVO);
        } catch (RuntimeException e) {
            log.error("[SysLoginController] 登录失败, username={}, error={}", loginDTO.getUsername(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public Result<Void> logout() {
        log.info("[SysLoginController] 退出登录");
        // TODO: 可以在这里将token加入黑名单
        return Result.ok();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user/info")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.getUserId(token);

        log.debug("[SysLoginController] 获取用户信息, userId={}", userId);

        try {
            UserInfoVO userInfo = sysUserService.getUserInfo(userId);
            return Result.ok(userInfo);
        } catch (RuntimeException e) {
            log.error("[SysLoginController] 获取用户信息失败, userId={}, error={}", userId, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 获取用户路由
     */
    @GetMapping("/user/routers")
    public Result<java.util.List<RouterVO>> getRouters(HttpServletRequest request) {
        String token = extractToken(request);
        Long userId = jwtUtil.getUserId(token);

        log.debug("[SysLoginController] 获取用户路由, userId={}", userId);

        try {
            java.util.List<RouterVO> routers = sysUserService.getRouters(userId);
            return Result.ok(routers);
        } catch (RuntimeException e) {
            log.error("[SysLoginController] 获取用户路由失败, userId={}, error={}", userId, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 从请求中提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多级代理的情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
