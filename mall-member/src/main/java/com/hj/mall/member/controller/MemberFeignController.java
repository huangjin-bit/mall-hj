package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.Member;
import com.hj.mall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员Feign控制器 - 提供给其他服务调用的接口
 */
@RestController
@RequestMapping("/feign/member")
@RequiredArgsConstructor
public class MemberFeignController {

    private final MemberService memberService;

    /**
     * 根据会员ID查询会员信息
     */
    @GetMapping("/{memberId}")
    public Result<Member> getMemberById(@PathVariable Long memberId) {
        Member member = memberService.getById(memberId);
        return Result.ok(member);
    }

    /**
     * 根据用户名查询会员信息
     */
    @GetMapping("/username/{username}")
    public Result<Member> getMemberByUsername(@PathVariable String username) {
        Member member = memberService.getByUsername(username);
        return Result.ok(member);
    }
}
