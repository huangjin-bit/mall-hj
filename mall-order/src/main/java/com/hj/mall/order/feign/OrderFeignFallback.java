package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class OrderFeignFallback implements FallbackFactory<OrderFeignClient> {

    @Override
    public OrderFeignClient create(Throwable cause) {
        return new OrderFeignClient() {
            @Override
            public Result<Order> getOrderByOrderSn(String orderSn) {
                log.error("[OrderFeignFallback] getOrderByOrderSn 调用失败, orderSn={}, error={}",
                        orderSn, cause.getMessage());
                return Result.error("订单服务暂不可用");
            }

            @Override
            public Result<List<OrderItem>> listOrderItems(Long orderId) {
                log.error("[OrderFeignFallback] listOrderItems 调用失败, orderId={}, error={}",
                        orderId, cause.getMessage());
                return Result.error("订单服务暂不可用", Collections.emptyList());
            }
        };
    }
}
