package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.HomeSubjectSpu;
import com.hj.mall.seckill.service.HomeSubjectSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页专题商品关联控制器
 */
@RestController
@RequestMapping("/home-subject-spu")
@RequiredArgsConstructor
public class HomeSubjectSpuController {

    private final HomeSubjectSpuService homeSubjectSpuService;

    /**
     * 分页查询首页专题商品关联
     */
    @GetMapping("/list")
    public Result<IPage<HomeSubjectSpu>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<HomeSubjectSpu> result = homeSubjectSpuService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询首页专题商品关联
     */
    @GetMapping("/{id}")
    public Result<HomeSubjectSpu> getById(@PathVariable Long id) {
        HomeSubjectSpu entity = homeSubjectSpuService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存首页专题商品关联
     */
    @PostMapping
    public Result<Void> save(@RequestBody HomeSubjectSpu entity) {
        homeSubjectSpuService.save(entity);
        return Result.ok();
    }

    /**
     * 更新首页专题商品关联
     */
    @PutMapping
    public Result<Void> update(@RequestBody HomeSubjectSpu entity) {
        homeSubjectSpuService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除首页专题商品关联
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        homeSubjectSpuService.removeBatch(ids);
        return Result.ok();
    }
}
