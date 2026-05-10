package com.hj.mall.seckill.feign.fallback;

import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 商品远程调用降级处理
 */
@Slf4j
@Component
public class ProductFeignFallback implements ProductFeignClient {

    @Override
    public Result<Object> getSkuInfo(Long skuId) {
        log.error("[ProductFeignFallback] 查询商品信息失败，skuId={}，进入降级处理", skuId);
        return Result.error("商品服务暂时不可用");
    }
}
