package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SpuBounds;
import com.hj.mall.seckill.service.SpuBoundsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SPU积分成长控制器
 */
@RestController
@RequestMapping("/spu-bounds")
@RequiredArgsConstructor
public class SpuBoundsController {

    private final SpuBoundsService spuBoundsService;

    /**
     * 分页查询SPU积分成长
     */
    @GetMapping("/list")
    public Result<IPage<SpuBounds>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<SpuBounds> result = spuBoundsService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询SPU积分成长
     */
    @GetMapping("/{id}")
    public Result<SpuBounds> getById(@PathVariable Long id) {
        SpuBounds entity = spuBoundsService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存SPU积分成长
     */
    @PostMapping
    public Result<Void> save(@RequestBody SpuBounds entity) {
        spuBoundsService.save(entity);
        return Result.ok();
    }

    /**
     * 更新SPU积分成长
     */
    @PutMapping
    public Result<Void> update(@RequestBody SpuBounds entity) {
        spuBoundsService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除SPU积分成长
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        spuBoundsService.removeBatch(ids);
        return Result.ok();
    }
}
