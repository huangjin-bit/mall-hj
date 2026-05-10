package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.RefundInfo;
import com.hj.mall.order.mapper.RefundInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refund-info")
@RequiredArgsConstructor
public class RefundInfoController {

    private final RefundInfoMapper refundInfoMapper;

    @GetMapping("/list")
    public Result<IPage<RefundInfo>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String orderSn) {
        Page<RefundInfo> p = new Page<>(page, size);
        LambdaQueryWrapper<RefundInfo> wrapper = new LambdaQueryWrapper<>();
        if (orderSn != null) {
            wrapper.eq(RefundInfo::getOrderSn, orderSn);
        }
        return Result.ok(refundInfoMapper.selectPage(p, wrapper));
    }

    @GetMapping("/{id}")
    public Result<RefundInfo> getById(@PathVariable Long id) {
        return Result.ok(refundInfoMapper.selectById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody RefundInfo refundInfo) {
        refundInfoMapper.insert(refundInfo);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody RefundInfo refundInfo) {
        refundInfoMapper.updateById(refundInfo);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        refundInfoMapper.deleteById(id);
        return Result.ok();
    }
}
