package com.hj.mall.auth.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 认证服务 Feign 降级处理
 * 当调用失败时执行此降级逻辑
 */
@Slf4j
@Component
public class AuthFeignFallback implements AuthFeignClient {

    @Override
    public Result<Long> validateToken(String token) {
        log.error("[AuthFeignFallback] 验证 Token 调用失败, token={}", token);
        return Result.error(ResultCode.FEIGN_ERROR.getCode(), "认证服务暂时不可用");
    }
}
