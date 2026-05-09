package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.Member;

import java.util.List;

/**
 * 会员服务接口
 */
public interface MemberService {

    /**
     * 分页查询会员列表
     * @param page 分页参数
     * @param key 搜索关键词（用户名/昵称/手机号）
     * @return 分页结果
     */
    IPage<Member> listPage(Page<Member> page, String key);

    /**
     * 根据ID查询会员
     * @param id 会员ID
     * @return 会员信息
     */
    Member getById(Long id);

    /**
     * 根据用户名查询会员
     * @param username 用户名
     * @return 会员信息
     */
    Member getByUsername(String username);

    /**
     * 根据手机号查询会员
     * @param phone 手机号
     * @return 会员信息
     */
    Member getByPhone(String phone);

    /**
     * 保存会员
     * @param member 会员信息
     */
    void save(Member member);

    /**
     * 更新会员信息
     * @param member 会员信息
     */
    void updateById(Member member);

    /**
     * 批量删除会员
     * @param ids 会员ID列表
     */
    void removeBatch(List<Long> ids);

    /**
     * 更新会员积分
     * @param memberId 会员ID
     * @param change 积分变化值（正数增加，负数减少）
     */
    void updateIntegration(Long memberId, int change);

    /**
     * 更新会员成长值
     * @param memberId 会员ID
     * @param change 成长值变化值（正数增加，负数减少）
     */
    void updateGrowth(Long memberId, int change);
}
