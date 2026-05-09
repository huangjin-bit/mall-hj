package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.service.SkuService;
import com.hj.mall.product.vo.SkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sku")
@RequiredArgsConstructor
public class SkuController {

    private final SkuService skuService;

    // Admin list (paginated with spuId filter)
    @GetMapping("/list")
    public Result<IPage<SkuInfo>> list(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Long spuId) {
        Page<SkuInfo> p = new Page<>(page, size);
        return Result.ok(skuService.listPage(p, spuId));
    }

    // Admin save
    @PostMapping
    public Result<Void> save(@RequestBody SkuInfo sku) {
        skuService.save(sku);
        return Result.ok();
    }

    // Admin update
    @PutMapping
    public Result<Void> update(@RequestBody SkuInfo sku) {
        skuService.updateById(sku);
        return Result.ok();
    }

    // Admin delete
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        skuService.removeBatch(ids);
        return Result.ok();
    }

    @GetMapping("/{skuId}")
    public Result<SkuVO> getSkuDetail(@PathVariable Long skuId) {
        return Result.ok(skuService.getSkuDetail(skuId));
    }
}
