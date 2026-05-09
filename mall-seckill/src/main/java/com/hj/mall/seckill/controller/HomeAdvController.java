package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.HomeAdv;
import com.hj.mall.seckill.service.HomeAdvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页广告控制器
 */
@RestController
@RequestMapping("/home-adv")
@RequiredArgsConstructor
public class HomeAdvController {

    private final HomeAdvService homeAdvService;

    /**
     * 分页查询广告
     */
    @GetMapping("/list")
    public Result<IPage<HomeAdv>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer type) {
        IPage<HomeAdv> result = homeAdvService.listPage(new Page<>(page, size), name, type);
        return Result.ok(result);
    }

    /**
     * 查询当前启用的广告列表
     */
    @GetMapping("/active")
    public Result<List<HomeAdv>> listActiveAds(@RequestParam(required = false) Integer type) {
        List<HomeAdv> list = homeAdvService.listActiveAds(type);
        return Result.ok(list);
    }

    /**
     * 根据ID查询广告
     */
    @GetMapping("/{id}")
    public Result<HomeAdv> getById(@PathVariable Long id) {
        HomeAdv adv = homeAdvService.getById(id);
        return Result.ok(adv);
    }

    /**
     * 保存广告
     */
    @PostMapping
    public Result<Void> save(@RequestBody HomeAdv adv) {
        homeAdvService.save(adv);
        return Result.ok();
    }

    /**
     * 更新广告
     */
    @PutMapping
    public Result<Void> update(@RequestBody HomeAdv adv) {
        homeAdvService.updateById(adv);
        return Result.ok();
    }

    /**
     * 批量删除广告
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        homeAdvService.removeBatch(ids);
        return Result.ok();
    }
}
