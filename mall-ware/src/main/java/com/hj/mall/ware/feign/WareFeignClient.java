package com.hj.mall.ware.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = "mall-ware", fallback = WareFeignFallback.class)
public interface WareFeignClient {

    @PostMapping("/feign/ware/hasStock")
    Result<Map<Long, Integer>> getStockBySkuIds(@RequestBody List<Long> skuIds);

    @PostMapping("/feign/ware/lock/order")
    Result<Boolean> lockStock(@RequestBody Map<String, Object> params);

    @PostMapping("/feign/ware/deduct/{taskId}")
    Result<Void> deductStock(@PathVariable("taskId") Long taskId);

    @PostMapping("/feign/ware/release/{taskId}")
    Result<Void> releaseStock(@PathVariable("taskId") Long taskId);
}
