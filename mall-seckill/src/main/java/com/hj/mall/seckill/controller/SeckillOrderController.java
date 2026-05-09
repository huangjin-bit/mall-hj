package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SeckillOrder;
import com.hj.mall.seckill.service.SeckillOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀订单控制器
 */
@RestController
@RequestMapping("/seckill-order")
@RequiredArgsConstructor
public class SeckillOrderController {

    private final SeckillOrderService seckillOrderService;

    /**
     * 分页查询秒杀订单
     */
    @GetMapping("/list")
    public Result<IPage<SeckillOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long memberId) {
        IPage<SeckillOrder> result = seckillOrderService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }

    /**
     * 根据会员ID查询订单列表
     */
    @GetMapping("/member/{memberId}")
    public Result<List<SeckillOrder>> listByMemberId(@PathVariable Long memberId) {
        List<SeckillOrder> list = seckillOrderService.listByMemberId(memberId);
        return Result.ok(list);
    }

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Result<SeckillOrder> getById(@PathVariable Long id) {
        SeckillOrder order = seckillOrderService.getById(id);
        return Result.ok(order);
    }

    /**
     * 创建秒杀订单
     */
    @PostMapping
    public Result<Void> save(@RequestBody SeckillOrder order) {
        seckillOrderService.save(order);
        return Result.ok();
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        seckillOrderService.updateStatus(id, status);
        return Result.ok();
    }
}
