package com.hj.mall.order.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-item")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/list/{orderId}")
    public Result<List<OrderItem>> listByOrderId(@PathVariable Long orderId) {
        return Result.ok(orderItemService.listByOrderId(orderId));
    }

    @PostMapping("/batch")
    public Result<Void> saveBatch(@RequestBody List<OrderItem> items) {
        orderItemService.saveBatch(items);
        return Result.ok();
    }
}
