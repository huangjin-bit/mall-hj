package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/list")
    public Result<IPage<Order>> list(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String key,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) Long memberId) {
        Page<Order> p = new Page<>(page, size);
        return Result.ok(orderService.listPage(p, key, status, memberId));
    }

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return Result.ok(orderService.getById(id));
    }

    @GetMapping("/sn/{orderSn}")
    public Result<Order> getByOrderSn(@PathVariable String orderSn) {
        return Result.ok(orderService.getByOrderSn(orderSn));
    }

    @GetMapping("/{orderId}/items")
    public Result<List<OrderItem>> listOrderItems(@PathVariable Long orderId) {
        return Result.ok(orderService.listOrderItems(orderId));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Order order) {
        orderService.save(order);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                      @RequestParam Integer status,
                                      @RequestParam(required = false) String note,
                                      @RequestParam(required = false) Long operateBy) {
        orderService.updateStatus(id, status, note, operateBy);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        orderService.removeBatch(ids);
        return Result.ok();
    }
}
