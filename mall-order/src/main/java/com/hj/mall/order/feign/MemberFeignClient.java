package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.dto.MemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 会员远程调用客户端
 */
@FeignClient(
        name = "mall-member",
        path = "/feign/member",
        fallbackFactory = MemberFeignFallback.class
)
public interface MemberFeignClient {

    /**
     * 根据会员ID查询会员信息
     */
    @GetMapping("/{memberId}")
    Result<MemberDto> getMemberById(@PathVariable("memberId") Long memberId);

    /**
     * 根据用户名查询会员信息
     */
    @GetMapping("/username/{username}")
    Result<MemberDto> getMemberByUsername(@PathVariable("username") String username);
}
