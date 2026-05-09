package com.hj.mall.ware.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.ware.service.WareSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/feign/ware")
@RequiredArgsConstructor
public class WareFeignController {

    private final WareSkuService wareSkuService;

    @PostMapping("/hasStock")
    public Result<Map<Long, Integer>> getStockBySkuIds(@RequestBody List<Long> skuIds) {
        log.info("[Feign] 查询SKU库存，skuIds：{}", skuIds);
        return Result.ok(wareSkuService.getStockBySkuIds(skuIds));
    }

    @PostMapping("/lock/order")
    public Result<Boolean> lockStock(@RequestBody Map<String, Object> params) {
        Long orderId = Long.valueOf(params.get("orderId").toString());
        String orderSn = params.get("orderSn").toString();
        List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");

        log.info("[Feign] 锁定订单库存，订单号：{}, 商品数：{}", orderSn, items.size());
        boolean result = wareSkuService.lockStock(orderId, orderSn, items);
        return Result.ok(result);
    }

    @PostMapping("/deduct/{taskId}")
    public Result<Void> deductStock(@PathVariable Long taskId) {
        log.info("[Feign] 扣减库存，任务ID：{}", taskId);
        wareSkuService.deductStock(taskId);
        return Result.ok();
    }

    @PostMapping("/release/{taskId}")
    public Result<Void> releaseStock(@PathVariable Long taskId) {
        log.info("[Feign] 释放库存，任务ID：{}", taskId);
        wareSkuService.releaseStock(taskId);
        return Result.ok();
    }
}
