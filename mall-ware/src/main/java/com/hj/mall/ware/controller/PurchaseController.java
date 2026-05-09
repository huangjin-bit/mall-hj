package com.hj.mall.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.Purchase;
import com.hj.mall.ware.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/list")
    public Result<IPage<Purchase>> list(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Long wareId,
                                        @RequestParam(required = false) Integer status) {
        Page<Purchase> p = new Page<>(page, size);
        return Result.ok(purchaseService.listPage(p, wareId, status));
    }

    @GetMapping("/{id}")
    public Result<Purchase> getById(@PathVariable Long id) {
        return Result.ok(purchaseService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Purchase purchase) {
        purchaseService.save(purchase);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Purchase purchase) {
        purchaseService.updateById(purchase);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        purchaseService.removeBatch(ids);
        return Result.ok();
    }
}
