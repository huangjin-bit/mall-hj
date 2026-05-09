package com.hj.mall.order.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.OrderSetting;
import com.hj.mall.order.service.OrderSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-setting")
@RequiredArgsConstructor
public class OrderSettingController {

    private final OrderSettingService orderSettingService;

    @GetMapping
    public Result<OrderSetting> getSetting() {
        return Result.ok(orderSettingService.getSetting());
    }

    @PutMapping
    public Result<Void> update(@RequestBody OrderSetting setting) {
        orderSettingService.update(setting);
        return Result.ok();
    }
}
