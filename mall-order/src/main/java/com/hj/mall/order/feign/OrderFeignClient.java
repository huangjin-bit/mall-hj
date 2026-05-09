package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "mall-order",
        path = "/feign",
        fallbackFactory = OrderFeignFallback.class
)
public interface OrderFeignClient {

    @GetMapping("/order/{orderSn}")
    Result<Order> getOrderByOrderSn(@PathVariable("orderSn") String orderSn);

    @GetMapping("/order/items/{orderId}")
    Result<List<OrderItem>> listOrderItems(@PathVariable("orderId") Long orderId);
}
