package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SeckillPromotion;
import com.hj.mall.seckill.service.SeckillPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀活动控制器
 */
@RestController
@RequestMapping("/seckill-promotion")
@RequiredArgsConstructor
public class SeckillPromotionController {

    private final SeckillPromotionService seckillPromotionService;

    /**
     * 分页查询秒杀活动
     */
    @GetMapping("/list")
    public Result<IPage<SeckillPromotion>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        IPage<SeckillPromotion> result = seckillPromotionService.listPage(new Page<>(page, size), title, status);
        return Result.ok(result);
    }

    /**
     * 根据ID查询秒杀活动
     */
    @GetMapping("/{id}")
    public Result<SeckillPromotion> getById(@PathVariable Long id) {
        SeckillPromotion entity = seckillPromotionService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存秒杀活动
     */
    @PostMapping
    public Result<Void> save(@RequestBody SeckillPromotion entity) {
        seckillPromotionService.save(entity);
        return Result.ok();
    }

    /**
     * 更新秒杀活动
     */
    @PutMapping
    public Result<Void> update(@RequestBody SeckillPromotion entity) {
        seckillPromotionService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除秒杀活动
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        seckillPromotionService.removeBatch(ids);
        return Result.ok();
    }
}
