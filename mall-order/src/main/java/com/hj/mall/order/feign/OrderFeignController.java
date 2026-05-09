package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
public class OrderFeignController {

    private final OrderService orderService;

    @GetMapping("/order/{orderSn}")
    public Result<Order> getOrderByOrderSn(@PathVariable String orderSn) {
        return Result.ok(orderService.getByOrderSn(orderSn));
    }

    @GetMapping("/order/items/{orderId}")
    public Result<List<OrderItem>> listOrderItems(@PathVariable Long orderId) {
        return Result.ok(orderService.listOrderItems(orderId));
    }
}
