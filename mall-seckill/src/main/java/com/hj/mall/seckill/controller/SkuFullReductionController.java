package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SkuFullReduction;
import com.hj.mall.seckill.service.SkuFullReductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品满减信息控制器
 */
@RestController
@RequestMapping("/sku-full-reduction")
@RequiredArgsConstructor
public class SkuFullReductionController {

    private final SkuFullReductionService skuFullReductionService;

    /**
     * 分页查询商品满减信息
     */
    @GetMapping("/list")
    public Result<IPage<SkuFullReduction>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<SkuFullReduction> result = skuFullReductionService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询商品满减信息
     */
    @GetMapping("/{id}")
    public Result<SkuFullReduction> getById(@PathVariable Long id) {
        SkuFullReduction entity = skuFullReductionService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存商品满减信息
     */
    @PostMapping
    public Result<Void> save(@RequestBody SkuFullReduction entity) {
        skuFullReductionService.save(entity);
        return Result.ok();
    }

    /**
     * 更新商品满减信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody SkuFullReduction entity) {
        skuFullReductionService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除商品满减信息
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        skuFullReductionService.removeBatch(ids);
        return Result.ok();
    }
}
