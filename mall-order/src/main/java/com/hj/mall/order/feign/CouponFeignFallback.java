package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 优惠券服务降级处理
 */
@Slf4j
@Component
public class CouponFeignFallback implements FallbackFactory<CouponFeignClient> {

    @Override
    public CouponFeignClient create(Throwable cause) {
        return new CouponFeignClient() {
            @Override
            public Result<List<Map<String, Object>>> getMemberAvailableCoupons(Long memberId) {
                log.error("[CouponFeignFallback] getMemberAvailableCoupons 调用失败, memberId={}, error={}",
                        memberId, cause.getMessage());
                return Result.success(Collections.emptyList());
            }

            @Override
            public Result<Void> useCoupon(Long memberId, Long couponId, Long orderId) {
                log.error("[CouponFeignFallback] useCoupon 调用失败, memberId={}, couponId={}, orderId={}, error={}",
                        memberId, couponId, orderId, cause.getMessage());
                return Result.error("优惠券服务暂不可用");
            }
        };
    }
}
