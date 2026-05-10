package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.OrderOperateHistory;
import com.hj.mall.order.mapper.OrderOperateHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-operate-history")
@RequiredArgsConstructor
public class OrderOperateHistoryController {

    private final OrderOperateHistoryMapper orderOperateHistoryMapper;

    @GetMapping("/list")
    public Result<IPage<OrderOperateHistory>> list(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) Long orderId) {
        Page<OrderOperateHistory> p = new Page<>(page, size);
        LambdaQueryWrapper<OrderOperateHistory> wrapper = new LambdaQueryWrapper<>();
        if (orderId != null) {
            wrapper.eq(OrderOperateHistory::getOrderId, orderId);
        }
        wrapper.orderByDesc(OrderOperateHistory::getCreateTime);
        return Result.ok(orderOperateHistoryMapper.selectPage(p, wrapper));
    }

    @GetMapping("/{id}")
    public Result<OrderOperateHistory> getById(@PathVariable Long id) {
        return Result.ok(orderOperateHistoryMapper.selectById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody OrderOperateHistory history) {
        orderOperateHistoryMapper.insert(history);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody OrderOperateHistory history) {
        orderOperateHistoryMapper.updateById(history);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        orderOperateHistoryMapper.deleteById(id);
        return Result.ok();
    }
}
