package com.hj.mall.member.service.impl;

import com.hj.mall.member.entity.Member;
import com.hj.mall.member.service.MemberFeignService;
import com.hj.mall.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员Feign服务实现（用于服务间调用）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberFeignServiceImpl implements MemberFeignService {

    private final MemberService memberService;

    @Override
    public Member getMemberById(Long memberId) {
        log.info("[MemberFeignService] Feign调用：根据ID查询会员, memberId={}", memberId);

        if (memberId == null) {
            return null;
        }

        try {
            return memberService.getById(memberId);
        } catch (Exception e) {
            log.error("[MemberFeignService] 查询会员失败, memberId={}", memberId, e);
            return null;
        }
    }

    @Override
    public Member getMemberByUsername(String username) {
        log.info("[MemberFeignService] Feign调用：根据用户名查询会员, username={}", username);

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        try {
            return memberService.getByUsername(username);
        } catch (Exception e) {
            log.error("[MemberFeignService] 查询会员失败, username={}", username, e);
            return null;
        }
    }
}
