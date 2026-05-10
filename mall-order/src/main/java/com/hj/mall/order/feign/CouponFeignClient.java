package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 优惠券远程调用客户端
 * 注：优惠券功能已合并到秒杀服务
 */
@FeignClient(
        name = "mall-seckill",
        path = "/feign/seckill",
        fallbackFactory = CouponFeignFallback.class
)
public interface CouponFeignClient {

    /**
     * 获取会员可用优惠券
     * TODO: 秒杀服务需要实现此接口
     */
    @GetMapping("/coupon/member/{memberId}/available")
    Result<List<Map<String, Object>>> getMemberAvailableCoupons(@PathVariable("memberId") Long memberId);

    /**
     * 使用优惠券
     * TODO: 秒杀服务需要实现此接口
     */
    @GetMapping("/coupon/use")
    Result<Void> useCoupon(@PathVariable("memberId") Long memberId,
                           @PathVariable("couponId") Long couponId,
                           @PathVariable("orderId") Long orderId);
}
