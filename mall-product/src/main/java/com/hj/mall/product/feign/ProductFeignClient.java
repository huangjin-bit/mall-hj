package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.entity.SpuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "mall-product",
        path = "/feign",
        fallbackFactory = ProductFeignFallback.class
)
public interface ProductFeignClient {

    @GetMapping("/sku/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);

    @GetMapping("/spu/{spuId}")
    Result<SpuInfo> getSpuInfo(@PathVariable("spuId") Long spuId);

    @DeleteMapping("/cache/category")
    Result<Void> refreshCategoryCache();
}
