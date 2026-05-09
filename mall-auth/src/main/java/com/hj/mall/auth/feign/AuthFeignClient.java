package com.hj.mall.auth.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 认证服务 Feign 客户端
 * 供其他服务调用认证服务
 */
@FeignClient(
    name = "mall-auth",
    path = "/feign",
    fallbackFactory = AuthFeignFallback.class
)
public interface AuthFeignClient {

    /**
     * 验证 Token
     *
     * @param token JWT Token
     * @return Result<Long> 返回会员ID
     */
    @GetMapping("/validate")
    Result<Long> validateToken(@RequestParam("token") String token);
}
