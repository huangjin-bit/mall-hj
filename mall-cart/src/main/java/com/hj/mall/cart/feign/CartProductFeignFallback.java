package com.hj.mall.cart.feign;

import com.hj.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 商品服务Feign降级处理
 */
@Slf4j
@Component
public class CartProductFeignFallback implements ProductFeignClient {

    @Override
    public Result<SkuInfoDTO> getSkuInfo(Long skuId) {
        log.error("[购物车] 商品服务调用失败，skuId={}", skuId);
        return Result.error("商品服务暂时不可用");
    }

    @Override
    public Result<List<String>> getSkuSaleAttrValues(Long skuId) {
        log.error("[购物车] 获取销售属性失败，skuId={}", skuId);
        return Result.success(Collections.emptyList());
    }
}
