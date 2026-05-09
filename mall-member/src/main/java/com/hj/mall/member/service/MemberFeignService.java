package com.hj.mall.member.service;

import com.hj.mall.member.entity.Member;

/**
 * 会员Feign服务接口（用于服务间调用）
 */
public interface MemberFeignService {

    /**
     * 根据会员ID查询会员信息
     * @param memberId 会员ID
     * @return 会员信息
     */
    Member getMemberById(Long memberId);

    /**
     * 根据用户名查询会员信息
     * @param username 用户名
     * @return 会员信息
     */
    Member getMemberByUsername(String username);
}
