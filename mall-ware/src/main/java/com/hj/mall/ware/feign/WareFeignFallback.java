package com.hj.mall.ware.feign;

import com.hj.mall.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class WareFeignFallback implements WareFeignClient {

    @Override
    public Result<Map<Long, Integer>> getStockBySkuIds(List<Long> skuIds) {
        return Result.error(5001, "仓储服务不可用");
    }

    @Override
    public Result<Boolean> lockStock(Map<String, Object> params) {
        return Result.error(5001, "仓储服务不可用");
    }

    @Override
    public Result<Void> deductStock(Long taskId) {
        return Result.error(5001, "仓储服务不可用");
    }

    @Override
    public Result<Void> releaseStock(Long taskId) {
        return Result.error(5001, "仓储服务不可用");
    }
}
