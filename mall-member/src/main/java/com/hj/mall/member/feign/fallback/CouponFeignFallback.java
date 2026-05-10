package com.hj.mall.member.feign.fallback;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.feign.CouponFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 优惠券远程调用降级处理
 */
@Slf4j
@Component
public class CouponFeignFallback implements CouponFeignClient {

    @Override
    public Result<Object> listCoupons(Integer page, Integer size, String name, Integer status) {
        log.error("[CouponFeignFallback] 分页查询优惠券失败，进入降级处理");
        return Result.error("优惠券服务暂时不可用");
    }

    @Override
    public Result<Object> listAvailableCoupons() {
        log.error("[CouponFeignFallback] 查询可领取优惠券失败，进入降级处理");
        return Result.error("优惠券服务暂时不可用");
    }

    @Override
    public Result<Object> getCouponById(Long id) {
        log.error("[CouponFeignFallback] 查询优惠券详情失败，couponId={}，进入降级处理", id);
        return Result.error("优惠券服务暂时不可用");
    }

    @Override
    public Result<Void> claimCoupon(Long couponId, Long memberId) {
        log.error("[CouponFeignFallback] 领取优惠券失败，couponId={}, memberId={}，进入降级处理", couponId, memberId);
        return Result.error("优惠券服务暂时不可用");
    }
}
