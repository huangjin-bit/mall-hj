package com.hj.mall.auth.feign;

import com.hj.mall.auth.service.AuthService;
import com.hj.mall.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证服务 Feign 接口实现
 * 提供给其他微服务调用的内部接口
 */
@Slf4j
@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
public class AuthFeignController implements AuthFeignClient {

    private final AuthService authService;

    @Override
    @GetMapping("/validate")
    public Result<Long> validateToken(@RequestParam("token") String token) {
        log.debug("[AuthFeignController] Feign 调用验证 Token");
        return authService.validateToken(token);
    }
}
