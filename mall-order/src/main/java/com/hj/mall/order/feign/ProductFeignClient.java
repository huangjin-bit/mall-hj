package com.hj.mall.order.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.order.dto.SkuInfoDto;
import com.hj.mall.order.dto.SpuInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品远程调用客户端
 */
@FeignClient(
        name = "mall-product",
        path = "/feign",
        fallbackFactory = ProductFeignFallback.class
)
public interface ProductFeignClient {

    /**
     * 查询SKU信息
     */
    @GetMapping("/sku/{skuId}")
    Result<SkuInfoDto> getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 查询SPU信息
     */
    @GetMapping("/spu/{spuId}")
    Result<SpuInfoDto> getSpuInfo(@PathVariable("spuId") Long spuId);
}
