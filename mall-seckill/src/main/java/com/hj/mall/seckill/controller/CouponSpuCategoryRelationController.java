package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.CouponSpuCategoryRelation;
import com.hj.mall.seckill.service.CouponSpuCategoryRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券分类关联控制器
 */
@RestController
@RequestMapping("/coupon-spu-category-relation")
@RequiredArgsConstructor
public class CouponSpuCategoryRelationController {

    private final CouponSpuCategoryRelationService couponSpuCategoryRelationService;

    /**
     * 分页查询优惠券分类关联
     */
    @GetMapping("/list")
    public Result<IPage<CouponSpuCategoryRelation>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<CouponSpuCategoryRelation> result = couponSpuCategoryRelationService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询优惠券分类关联
     */
    @GetMapping("/{id}")
    public Result<CouponSpuCategoryRelation> getById(@PathVariable Long id) {
        CouponSpuCategoryRelation entity = couponSpuCategoryRelationService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存优惠券分类关联
     */
    @PostMapping
    public Result<Void> save(@RequestBody CouponSpuCategoryRelation entity) {
        couponSpuCategoryRelationService.save(entity);
        return Result.ok();
    }

    /**
     * 更新优惠券分类关联
     */
    @PutMapping
    public Result<Void> update(@RequestBody CouponSpuCategoryRelation entity) {
        couponSpuCategoryRelationService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除优惠券分类关联
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        couponSpuCategoryRelationService.removeBatch(ids);
        return Result.ok();
    }
}
