package com.hj.mall.cart.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品服务Feign客户端 - 用于购物车获取商品信息
 */
@FeignClient(value = "mall-product", path = "/feign", fallback = CartProductFeignFallback.class)
public interface ProductFeignClient {

    /**
     * 获取SKU信息
     */
    @GetMapping("/sku/{skuId}")
    Result<SkuInfoDTO> getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 获取SKU销售属性值
     */
    @GetMapping("/sku/sale-attr/{skuId}")
    Result<java.util.List<String>> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);
}
