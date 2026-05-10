package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SkuLadder;
import com.hj.mall.seckill.service.SkuLadderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品阶梯价格控制器
 */
@RestController
@RequestMapping("/sku-ladder")
@RequiredArgsConstructor
public class SkuLadderController {

    private final SkuLadderService skuLadderService;

    /**
     * 分页查询商品阶梯价格
     */
    @GetMapping("/list")
    public Result<IPage<SkuLadder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<SkuLadder> result = skuLadderService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询商品阶梯价格
     */
    @GetMapping("/{id}")
    public Result<SkuLadder> getById(@PathVariable Long id) {
        SkuLadder entity = skuLadderService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存商品阶梯价格
     */
    @PostMapping
    public Result<Void> save(@RequestBody SkuLadder entity) {
        skuLadderService.save(entity);
        return Result.ok();
    }

    /**
     * 更新商品阶梯价格
     */
    @PutMapping
    public Result<Void> update(@RequestBody SkuLadder entity) {
        skuLadderService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除商品阶梯价格
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        skuLadderService.removeBatch(ids);
        return Result.ok();
    }
}
