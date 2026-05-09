package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.OrderReturnReason;
import com.hj.mall.order.service.OrderReturnReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-return-reason")
@RequiredArgsConstructor
public class OrderReturnReasonController {

    private final OrderReturnReasonService returnReasonService;

    @GetMapping("/list")
    public Result<IPage<OrderReturnReason>> list(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Page<OrderReturnReason> p = new Page<>(page, size);
        return Result.ok(returnReasonService.listPage(p));
    }

    @GetMapping("/{id}")
    public Result<OrderReturnReason> getById(@PathVariable Long id) {
        return Result.ok(returnReasonService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody OrderReturnReason reason) {
        returnReasonService.save(reason);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody OrderReturnReason reason) {
        returnReasonService.updateById(reason);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        returnReasonService.removeBatch(ids);
        return Result.ok();
    }
}
