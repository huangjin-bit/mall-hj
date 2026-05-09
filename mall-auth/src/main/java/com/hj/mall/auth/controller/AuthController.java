package com.hj.mall.auth.controller;

import com.hj.mall.auth.dto.LoginDTO;
import com.hj.mall.auth.dto.TokenDTO;
import com.hj.mall.auth.service.AuthService;
import com.hj.mall.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 提供登录、注册、Token 验证等接口
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login/username")
    public Result<TokenDTO> loginByUsername(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        log.info("[AuthController] 用户名密码登录请求, username={}, ip={}", loginDTO.getUsername(), ip);

        return authService.loginByUsername(loginDTO.getUsername(), loginDTO.getPassword(), ip, userAgent);
    }

    /**
     * 手机验证码登录
     */
    @PostMapping("/login/phone")
    public Result<TokenDTO> loginByPhone(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        log.info("[AuthController] 手机验证码登录请求, phone={}, ip={}", loginDTO.getPhone(), ip);

        return authService.loginByPhone(loginDTO.getPhone(), loginDTO.getSmsCode(), ip, userAgent);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody com.hj.mall.auth.dto.RegisterDTO registerDTO) {
        log.info("[AuthController] 用户注册请求, username={}, phone={}", registerDTO.getUsername(), registerDTO.getPhone());

        return authService.register(registerDTO.getUsername(), registerDTO.getPhone(), registerDTO.getPassword());
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/send")
    public Result<Void> sendSmsCode(@RequestParam("phone") String phone) {
        log.info("[AuthController] 发送短信验证码请求, phone={}", phone);

        return authService.sendSmsCode(phone);
    }

    /**
     * 验证 Token
     */
    @GetMapping("/validate")
    public Result<Long> validateToken(@RequestParam("token") String token) {
        log.debug("[AuthController] 验证 Token 请求");

        return authService.validateToken(token);
    }

    /**
     * 获取客户端真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多级代理的情况，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
