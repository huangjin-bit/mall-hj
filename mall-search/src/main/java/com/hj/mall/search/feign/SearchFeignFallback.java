package com.hj.mall.search.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.search.model.SkuEsModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SearchFeignFallback implements SearchFeignClient {

    @Override
    public Result<Void> productStatusUp(List<SkuEsModel> models) {
        log.error("[Feign降级] 商品上架调用失败，skuIds={}",
                models.stream().map(SkuEsModel::getSkuId).toList());
        return Result.error("搜索服务暂时不可用");
    }

    @Override
    public Result<Void> productStatusDown(List<Long> skuIds) {
        log.error("[Feign降级] 商品下架调用失败，skuIds={}", skuIds);
        return Result.error("搜索服务暂时不可用");
    }
}
