package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.PaymentInfo;
import com.hj.mall.order.mapper.PaymentInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-info")
@RequiredArgsConstructor
public class PaymentInfoController {

    private final PaymentInfoMapper paymentInfoMapper;

    @GetMapping("/list")
    public Result<IPage<PaymentInfo>> list(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) String orderSn) {
        Page<PaymentInfo> p = new Page<>(page, size);
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        if (orderSn != null) {
            wrapper.eq(PaymentInfo::getOrderSn, orderSn);
        }
        return Result.ok(paymentInfoMapper.selectPage(p, wrapper));
    }

    @GetMapping("/{id}")
    public Result<PaymentInfo> getById(@PathVariable Long id) {
        return Result.ok(paymentInfoMapper.selectById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody PaymentInfo paymentInfo) {
        paymentInfoMapper.insert(paymentInfo);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody PaymentInfo paymentInfo) {
        paymentInfoMapper.updateById(paymentInfo);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        paymentInfoMapper.deleteById(id);
        return Result.ok();
    }
}
