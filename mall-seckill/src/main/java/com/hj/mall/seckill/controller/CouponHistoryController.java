package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.CouponHistory;
import com.hj.mall.seckill.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券领取历史控制器
 */
@RestController
@RequestMapping("/coupon-history")
@RequiredArgsConstructor
public class CouponHistoryController {

    private final CouponHistoryService couponHistoryService;

    /**
     * 分页查询领取历史
     */
    @GetMapping("/list")
    public Result<IPage<CouponHistory>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Integer status) {
        IPage<CouponHistory> result = couponHistoryService.listPage(new Page<>(page, size), memberId, status);
        return Result.ok(result);
    }

    /**
     * 根据会员ID查询领取历史
     */
    @GetMapping("/member/{memberId}")
    public Result<List<CouponHistory>> listByMemberId(@PathVariable Long memberId) {
        List<CouponHistory> list = couponHistoryService.listByMemberId(memberId);
        return Result.ok(list);
    }

    /**
     * 根据ID查询领取历史
     */
    @GetMapping("/{id}")
    public Result<CouponHistory> getById(@PathVariable Long id) {
        CouponHistory history = couponHistoryService.getById(id);
        return Result.ok(history);
    }
}
