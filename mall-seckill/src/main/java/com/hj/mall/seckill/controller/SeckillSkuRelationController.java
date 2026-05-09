package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SeckillSkuRelation;
import com.hj.mall.seckill.service.SeckillSkuRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀商品控制器
 */
@RestController
@RequestMapping("/seckill-sku")
@RequiredArgsConstructor
public class SeckillSkuRelationController {

    private final SeckillSkuRelationService seckillSkuRelationService;

    /**
     * 分页查询秒杀商品
     */
    @GetMapping("/list")
    public Result<IPage<SeckillSkuRelation>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long sessionId) {
        IPage<SeckillSkuRelation> result = seckillSkuRelationService.listPage(new Page<>(page, size), sessionId);
        return Result.ok(result);
    }

    /**
     * 根据场次ID查询商品列表
     */
    @GetMapping("/session/{sessionId}")
    public Result<List<SeckillSkuRelation>> listBySessionId(@PathVariable Long sessionId) {
        List<SeckillSkuRelation> list = seckillSkuRelationService.listBySessionId(sessionId);
        return Result.ok(list);
    }

    /**
     * 根据ID查询商品
     */
    @GetMapping("/{id}")
    public Result<SeckillSkuRelation> getById(@PathVariable Long id) {
        SeckillSkuRelation relation = seckillSkuRelationService.getById(id);
        return Result.ok(relation);
    }

    /**
     * 保存商品
     */
    @PostMapping
    public Result<Void> save(@RequestBody SeckillSkuRelation relation) {
        seckillSkuRelationService.save(relation);
        return Result.ok();
    }

    /**
     * 更新商品
     */
    @PutMapping
    public Result<Void> update(@RequestBody SeckillSkuRelation relation) {
        seckillSkuRelationService.updateById(relation);
        return Result.ok();
    }

    /**
     * 批量删除商品
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        seckillSkuRelationService.removeBatch(ids);
        return Result.ok();
    }
}
