package com.hj.mall.member.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.feign.fallback.CouponFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 优惠券远程调用客户端
 * mall-hj 原调用 mall-coupon，现调整为调用 mall-seckill 中的优惠券功能
 */
@FeignClient(value = "mall-seckill", fallback = CouponFeignFallback.class)
public interface CouponFeignClient {

    /**
     * 分页查询优惠券
     */
    @GetMapping("/coupon/list")
    Result<Object> listCoupons(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status);

    /**
     * 查询可领取的优惠券列表
     */
    @GetMapping("/coupon/available")
    Result<Object> listAvailableCoupons();

    /**
     * 根据ID查询优惠券
     */
    @GetMapping("/coupon/{id}")
    Result<Object> getCouponById(@PathVariable("id") Long id);

    /**
     * 领取优惠券
     */
    @PostMapping("/coupon/claim/{couponId}")
    Result<Void> claimCoupon(
            @PathVariable("couponId") Long couponId,
            @RequestParam("memberId") Long memberId);
}
