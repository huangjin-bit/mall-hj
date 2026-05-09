package com.hj.mall.member.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 会员Feign客户端
 */
@FeignClient(name = "mall-member", path = "/feign", fallbackFactory = MemberFeignFallback.class)
public interface MemberFeignClient {

    /**
     * 根据会员ID查询会员信息
     */
    @GetMapping("/member/{memberId}")
    Result<Member> getMemberById(@PathVariable("memberId") Long memberId);

    /**
     * 根据用户名查询会员信息
     */
    @GetMapping("/member/username/{username}")
    Result<Member> getMemberByUsername(@PathVariable("username") String username);
}
