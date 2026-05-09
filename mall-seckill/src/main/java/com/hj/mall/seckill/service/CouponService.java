package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.Coupon;

import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService {

    /**
     * 分页查询优惠券
     * @param page 分页参数
     * @param name 优惠券名称（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    IPage<Coupon> listPage(Page<Coupon> page, String name, Integer status);

    /**
     * 查询可领取的优惠券列表
     * @return 优惠券列表
     */
    List<Coupon> listAvailableCoupons();

    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    Coupon getById(Long id);

    /**
     * 保存优惠券
     * @param coupon 优惠券信息
     */
    void save(Coupon coupon);

    /**
     * 更新优惠券
     * @param coupon 优惠券信息
     */
    void updateById(Coupon coupon);

    /**
     * 批量删除优惠券
     * @param ids 优惠券ID列表
     */
    void removeBatch(List<Long> ids);

    /**
     * 领取优惠券
     * @param couponId 优惠券ID
     * @param memberId 会员ID
     */
    void claimCoupon(Long couponId, Long memberId);
}
