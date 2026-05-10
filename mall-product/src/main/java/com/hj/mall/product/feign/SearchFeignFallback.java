package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 搜索服务Feign降级处理
 */
@Slf4j
@Component
public class SearchFeignFallback implements FallbackFactory<SearchFeignClient> {

    @Override
    public SearchFeignClient create(Throwable cause) {
        log.error("[SearchFeign] 调用失败: {}", cause.getMessage(), cause);
        return new SearchFeignClient() {
            @Override
            public Result<Void> productStatusUp(List<SearchFeignClient.SkuEsTO> skuEsModels) {
                log.warn("[SearchFeign] 商品上架失败，SKU数量: {}", skuEsModels != null ? skuEsModels.size() : 0);
                return Result.error(ResultCode.INTERNAL_ERROR);
            }
        };
    }
}
