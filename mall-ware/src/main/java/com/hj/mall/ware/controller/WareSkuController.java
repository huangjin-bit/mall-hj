package com.hj.mall.ware.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.WareSku;
import com.hj.mall.ware.service.WareSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ware-sku")
@RequiredArgsConstructor
public class WareSkuController {

    private final WareSkuService wareSkuService;

    @GetMapping("/list")
    public Result<IPage<WareSku>> list(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) Long wareId,
                                       @RequestParam(required = false) Long skuId) {
        Page<WareSku> p = new Page<>(page, size);
        return Result.ok(wareSkuService.listPage(p, wareId, skuId));
    }

    @GetMapping("/{id}")
    public Result<WareSku> getById(@PathVariable Long id) {
        return Result.ok(wareSkuService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody WareSku wareSku) {
        wareSkuService.save(wareSku);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody WareSku wareSku) {
        wareSkuService.updateById(wareSku);
        return Result.ok();
    }

    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        wareSkuService.removeBatch(ids);
        return Result.ok();
    }

    @PostMapping("/stock")
    public Result<Map<Long, Integer>> getStock(@RequestBody List<Long> skuIds) {
        return Result.ok(wareSkuService.getStockBySkuIds(skuIds));
    }
}
