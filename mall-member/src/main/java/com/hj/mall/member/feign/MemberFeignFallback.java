package com.hj.mall.member.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.Member;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 会员Feign降级处理
 */
@Component
public class MemberFeignFallback implements FallbackFactory<MemberFeignClient> {

    @Override
    public MemberFeignClient create(Throwable cause) {
        return new MemberFeignClient() {
            @Override
            public Result<Member> getMemberById(Long memberId) {
                return Result.error(500, "会员服务不可用：" + cause.getMessage());
            }

            @Override
            public Result<Member> getMemberByUsername(String username) {
                return Result.error(500, "会员服务不可用：" + cause.getMessage());
            }
        };
    }
}
