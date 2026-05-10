package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SeckillSkuNotice;
import com.hj.mall.seckill.service.SeckillSkuNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀商品提醒控制器
 */
@RestController
@RequestMapping("/seckill-sku-notice")
@RequiredArgsConstructor
public class SeckillSkuNoticeController {

    private final SeckillSkuNoticeService seckillSkuNoticeService;

    /**
     * 分页查询秒杀商品提醒
     */
    @GetMapping("/list")
    public Result<IPage<SeckillSkuNotice>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<SeckillSkuNotice> result = seckillSkuNoticeService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询秒杀商品提醒
     */
    @GetMapping("/{id}")
    public Result<SeckillSkuNotice> getById(@PathVariable Long id) {
        SeckillSkuNotice entity = seckillSkuNoticeService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存秒杀商品提醒
     */
    @PostMapping
    public Result<Void> save(@RequestBody SeckillSkuNotice entity) {
        seckillSkuNoticeService.save(entity);
        return Result.ok();
    }

    /**
     * 更新秒杀商品提醒
     */
    @PutMapping
    public Result<Void> update(@RequestBody SeckillSkuNotice entity) {
        seckillSkuNoticeService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除秒杀商品提醒
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        seckillSkuNoticeService.removeBatch(ids);
        return Result.ok();
    }
}
