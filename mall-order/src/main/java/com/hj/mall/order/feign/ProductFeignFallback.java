package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.dto.SkuInfoDto;
import com.hj.mall.order.dto.SpuInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 商品服务降级处理
 */
@Slf4j
@Component
public class ProductFeignFallback implements FallbackFactory<ProductFeignClient> {

    @Override
    public ProductFeignClient create(Throwable cause) {
        return new ProductFeignClient() {
            @Override
            public Result<SkuInfoDto> getSkuInfo(Long skuId) {
                log.error("[ProductFeignFallback] getSkuInfo 调用失败, skuId={}, error={}",
                        skuId, cause.getMessage());
                return Result.error("商品服务暂不可用");
            }

            @Override
            public Result<SpuInfoDto> getSpuInfo(Long spuId) {
                log.error("[ProductFeignFallback] getSpuInfo 调用失败, spuId={}, error={}",
                        spuId, cause.getMessage());
                return Result.error("商品服务暂不可用");
            }
        };
    }
}
