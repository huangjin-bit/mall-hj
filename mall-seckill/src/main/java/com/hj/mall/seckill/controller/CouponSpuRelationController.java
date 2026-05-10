package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.CouponSpuRelation;
import com.hj.mall.seckill.service.CouponSpuRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券商品关联控制器
 */
@RestController
@RequestMapping("/coupon-spu-relation")
@RequiredArgsConstructor
public class CouponSpuRelationController {

    private final CouponSpuRelationService couponSpuRelationService;

    /**
     * 分页查询优惠券商品关联
     */
    @GetMapping("/list")
    public Result<IPage<CouponSpuRelation>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<CouponSpuRelation> result = couponSpuRelationService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询优惠券商品关联
     */
    @GetMapping("/{id}")
    public Result<CouponSpuRelation> getById(@PathVariable Long id) {
        CouponSpuRelation entity = couponSpuRelationService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存优惠券商品关联
     */
    @PostMapping
    public Result<Void> save(@RequestBody CouponSpuRelation entity) {
        couponSpuRelationService.save(entity);
        return Result.ok();
    }

    /**
     * 更新优惠券商品关联
     */
    @PutMapping
    public Result<Void> update(@RequestBody CouponSpuRelation entity) {
        couponSpuRelationService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除优惠券商品关联
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        couponSpuRelationService.removeBatch(ids);
        return Result.ok();
    }
}
