package com.hj.mall.seckill.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.feign.fallback.ProductFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品远程调用客户端
 * 秒杀模块需要查询商品SKU信息
 */
@FeignClient(value = "mall-product", path = "/feign", fallback = ProductFeignFallback.class)
public interface ProductFeignClient {

    /**
     * 根据SKU ID查询商品信息
     * @param skuId SKU ID
     * @return 商品信息
     */
    @GetMapping("/sku/{skuId}")
    Result<Object> getSkuInfo(@PathVariable("skuId") Long skuId);
}
