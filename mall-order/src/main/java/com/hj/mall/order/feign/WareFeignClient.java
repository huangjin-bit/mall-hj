package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 库存远程调用客户端
 */
@FeignClient(
        name = "mall-ware",
        path = "/feign/ware",
        fallbackFactory = WareFeignFallback.class
)
public interface WareFeignClient {

    /**
     * 批量查询SKU库存
     */
    @PostMapping("/hasStock")
    Result<Map<Long, Integer>> getStockBySkuIds(@RequestBody List<Long> skuIds);

    /**
     * 锁定订单库存
     */
    @PostMapping("/lock/order")
    Result<Boolean> lockStock(@RequestBody Map<String, Object> params);

    /**
     * 扣减库存
     */
    @PostMapping("/deduct/{taskId}")
    Result<Void> deductStock(@PathVariable("taskId") Long taskId);

    /**
     * 释放库存
     */
    @PostMapping("/release/{taskId}")
    Result<Void> releaseStock(@PathVariable("taskId") Long taskId);
}
