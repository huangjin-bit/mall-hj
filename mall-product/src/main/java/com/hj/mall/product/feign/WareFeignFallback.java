package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 仓储服务Feign降级处理
 */
@Slf4j
@Component
public class WareFeignFallback implements FallbackFactory<WareFeignClient> {

    @Override
    public WareFeignClient create(Throwable cause) {
        log.error("[WareFeign] 调用失败: {}", cause.getMessage(), cause);
        return new WareFeignClient() {
            @Override
            public Result<Map<Long, Boolean>> hasStock(List<Long> skuIds) {
                log.warn("[WareFeign] 查询库存失败，SKU数量: {}", skuIds != null ? skuIds.size() : 0);
                // 返回所有SKU都有库存的默认值，避免影响业务流程
                return Result.ok(Collections.emptyMap());
            }
        };
    }
}
