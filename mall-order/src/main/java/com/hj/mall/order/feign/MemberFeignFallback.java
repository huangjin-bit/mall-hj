package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 会员服务降级处理
 */
@Slf4j
@Component
public class MemberFeignFallback implements FallbackFactory<MemberFeignClient> {

    @Override
    public MemberFeignClient create(Throwable cause) {
        return new MemberFeignClient() {
            @Override
            public Result<MemberDto> getMemberById(Long memberId) {
                log.error("[MemberFeignFallback] getMemberById 调用失败, memberId={}, error={}",
                        memberId, cause.getMessage());
                return Result.error("会员服务暂不可用");
            }

            @Override
            public Result<MemberDto> getMemberByUsername(String username) {
                log.error("[MemberFeignFallback] getMemberByUsername 调用失败, username={}, error={}",
                        username, cause.getMessage());
                return Result.error("会员服务暂不可用");
            }
        };
    }
}
