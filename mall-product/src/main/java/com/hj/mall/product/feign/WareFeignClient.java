package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 仓储服务Feign客户端
 */
@FeignClient(
        name = "mall-ware",
        fallbackFactory = WareFeignFallback.class
)
public interface WareFeignClient {

    /**
     * 查询SKU是否有库存
     */
    @PostMapping("/feign/ware-sku/hasStock")
    Result<Map<Long, Boolean>> hasStock(@RequestBody List<Long> skuIds);
}
