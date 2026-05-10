package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 库存服务降级处理
 */
@Slf4j
@Component
public class WareFeignFallback implements FallbackFactory<WareFeignClient> {

    @Override
    public WareFeignClient create(Throwable cause) {
        return new WareFeignClient() {
            @Override
            public Result<Map<Long, Integer>> getStockBySkuIds(List<Long> skuIds) {
                log.error("[WareFeignFallback] getStockBySkuIds 调用失败, skuIds={}, error={}",
                        skuIds, cause.getMessage());
                return Result.error("库存服务暂不可用");
            }

            @Override
            public Result<Boolean> lockStock(Map<String, Object> params) {
                log.error("[WareFeignFallback] lockStock 调用失败, params={}, error={}",
                        params, cause.getMessage());
                return Result.error("库存服务暂不可用");
            }

            @Override
            public Result<Void> deductStock(Long taskId) {
                log.error("[WareFeignFallback] deductStock 调用失败, taskId={}, error={}",
                        taskId, cause.getMessage());
                return Result.error("库存服务暂不可用");
            }

            @Override
            public Result<Void> releaseStock(Long taskId) {
                log.error("[WareFeignFallback] releaseStock 调用失败, taskId={}, error={}",
                        taskId, cause.getMessage());
                return Result.error("库存服务暂不可用");
            }
        };
    }
}
