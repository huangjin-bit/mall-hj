package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.entity.SpuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductFeignFallback implements FallbackFactory<ProductFeignClient> {

    @Override
    public ProductFeignClient create(Throwable cause) {
        log.error("[ProductFeign] 调用失败: {}", cause.getMessage());
        return new ProductFeignClient() {
            @Override
            public Result<SkuInfo> getSkuInfo(Long skuId) {
                return Result.error(ResultCode.INTERNAL_ERROR, "商品服务不可用");
            }

            @Override
            public Result<SpuInfo> getSpuInfo(Long spuId) {
                return Result.error(ResultCode.INTERNAL_ERROR, "商品服务不可用");
            }

            @Override
            public Result<Void> refreshCategoryCache() {
                return Result.error(ResultCode.INTERNAL_ERROR, "商品服务不可用");
            }
        };
    }
}
