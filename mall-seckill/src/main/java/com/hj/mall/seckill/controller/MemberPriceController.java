package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.MemberPrice;
import com.hj.mall.seckill.service.MemberPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品会员价格控制器
 */
@RestController
@RequestMapping("/member-price")
@RequiredArgsConstructor
public class MemberPriceController {

    private final MemberPriceService memberPriceService;

    /**
     * 分页查询商品会员价格
     */
    @GetMapping("/list")
    public Result<IPage<MemberPrice>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<MemberPrice> result = memberPriceService.listPage(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询商品会员价格
     */
    @GetMapping("/{id}")
    public Result<MemberPrice> getById(@PathVariable Long id) {
        MemberPrice entity = memberPriceService.getById(id);
        return Result.ok(entity);
    }

    /**
     * 保存商品会员价格
     */
    @PostMapping
    public Result<Void> save(@RequestBody MemberPrice entity) {
        memberPriceService.save(entity);
        return Result.ok();
    }

    /**
     * 更新商品会员价格
     */
    @PutMapping
    public Result<Void> update(@RequestBody MemberPrice entity) {
        memberPriceService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除商品会员价格
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        memberPriceService.removeBatch(ids);
        return Result.ok();
    }
}
