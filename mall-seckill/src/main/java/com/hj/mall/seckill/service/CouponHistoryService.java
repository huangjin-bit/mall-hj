package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.CouponHistory;

/**
 * 优惠券领取历史服务接口
 */
public interface CouponHistoryService {

    /**
     * 分页查询领取历史
     * @param page 分页参数
     * @param memberId 会员ID（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    IPage<CouponHistory> listPage(Page<CouponHistory> page, Long memberId, Integer status);

    /**
     * 根据会员ID查询领取历史
     * @param memberId 会员ID
     * @return 领取历史列表
     */
    java.util.List<CouponHistory> listByMemberId(Long memberId);

    /**
     * 根据ID查询领取历史
     * @param id 领取历史ID
     * @return 领取历史
     */
    CouponHistory getById(Long id);

    /**
     * 保存领取历史
     * @param history 领取历史
     */
    void save(CouponHistory history);

    /**
     * 统计会员领取某优惠券的数量
     * @param memberId 会员ID
     * @param couponId 优惠券ID
     * @return 领取数量
     */
    long countByMemberIdAndCouponId(Long memberId, Long couponId);
}
