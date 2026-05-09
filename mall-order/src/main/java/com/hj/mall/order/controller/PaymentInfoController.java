package com.hj.mall.order.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.PaymentInfo;
import com.hj.mall.order.service.PaymentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-info")
@RequiredArgsConstructor
public class PaymentInfoController {

    private final PaymentInfoService paymentInfoService;

    @GetMapping("/order/{orderSn}")
    public Result<PaymentInfo> getByOrderSn(@PathVariable String orderSn) {
        return Result.ok(paymentInfoService.getByOrderSn(orderSn));
    }

    @PostMapping
    public Result<Void> save(@RequestBody PaymentInfo info) {
        paymentInfoService.save(info);
        return Result.ok();
    }

    @PutMapping("/{orderSn}/status")
    public Result<Void> updateStatus(@PathVariable String orderSn,
                                      @RequestParam Integer status,
                                      @RequestParam(required = false) String callbackContent) {
        paymentInfoService.updateStatus(orderSn, status, callbackContent);
        return Result.ok();
    }
}
