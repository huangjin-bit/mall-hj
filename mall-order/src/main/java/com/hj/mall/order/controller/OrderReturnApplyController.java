package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.OrderReturnApply;
import com.hj.mall.order.service.OrderReturnApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-return")
@RequiredArgsConstructor
public class OrderReturnApplyController {

    private final OrderReturnApplyService returnApplyService;

    @GetMapping("/list")
    public Result<IPage<OrderReturnApply>> list(@RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestParam(required = false) Long memberId,
                                                 @RequestParam(required = false) Integer status) {
        Page<OrderReturnApply> p = new Page<>(page, size);
        return Result.ok(returnApplyService.listPage(p, memberId, status));
    }

    @GetMapping("/{id}")
    public Result<OrderReturnApply> getById(@PathVariable Long id) {
        return Result.ok(returnApplyService.listPage(new Page<>(1, 1), null, null).getRecords().stream()
                .filter(a -> a.getId().equals(id)).findFirst().orElse(null));
    }

    @PostMapping
    public Result<Void> save(@RequestBody OrderReturnApply apply) {
        returnApplyService.save(apply);
        return Result.ok();
    }

    @PutMapping("/{id}/handle")
    public Result<Void> handle(@PathVariable Long id,
                               @RequestParam Integer status,
                               @RequestParam(required = false) Long handleBy,
                               @RequestParam(required = false) String handleNote) {
        returnApplyService.handle(id, status, handleBy, handleNote);
        return Result.ok();
    }
}
