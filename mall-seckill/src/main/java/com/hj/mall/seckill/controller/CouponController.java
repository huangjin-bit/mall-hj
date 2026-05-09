package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.Coupon;
import com.hj.mall.seckill.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券控制器
 */
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * 分页查询优惠券
     */
    @GetMapping("/list")
    public Result<IPage<Coupon>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        IPage<Coupon> result = couponService.listPage(new Page<>(page, size), name, status);
        return Result.ok(result);
    }

    /**
     * 查询可领取的优惠券列表
     */
    @GetMapping("/available")
    public Result<List<Coupon>> listAvailableCoupons() {
        List<Coupon> list = couponService.listAvailableCoupons();
        return Result.ok(list);
    }

    /**
     * 根据ID查询优惠券
     */
    @GetMapping("/{id}")
    public Result<Coupon> getById(@PathVariable Long id) {
        Coupon coupon = couponService.getById(id);
        return Result.ok(coupon);
    }

    /**
     * 保存优惠券
     */
    @PostMapping
    public Result<Void> save(@RequestBody Coupon coupon) {
        couponService.save(coupon);
        return Result.ok();
    }

    /**
     * 更新优惠券
     */
    @PutMapping
    public Result<Void> update(@RequestBody Coupon coupon) {
        couponService.updateById(coupon);
        return Result.ok();
    }

    /**
     * 批量删除优惠券
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        couponService.removeBatch(ids);
        return Result.ok();
    }

    /**
     * 领取优惠券
     */
    @PostMapping("/claim/{couponId}")
    public Result<Void> claimCoupon(
            @PathVariable Long couponId,
            @RequestParam Long memberId) {
        couponService.claimCoupon(couponId, memberId);
        return Result.ok();
    }
}
