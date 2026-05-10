package com.hj.mall.ware.feign.fallback;

import com.hj.mall.common.result.Result;
import com.hj.mall.ware.feign.OrderFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 订单远程调用降级处理
 */
@Slf4j
@Component
public class OrderFeignFallback implements OrderFeignClient {

    @Override
    public Result<Object> getOrderStatus(String orderSn) {
        log.error("[OrderFeignFallback] 查询订单状态失败，orderSn={}，进入降级处理", orderSn);
        return Result.error("订单服务暂时不可用");
    }
}
