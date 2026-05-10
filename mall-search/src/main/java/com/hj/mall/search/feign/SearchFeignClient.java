package com.hj.mall.search.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.search.model.SkuEsModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "mall-search", path = "/feign", fallbackFactory = SearchFeignFallback.class)
public interface SearchFeignClient {

    @PostMapping("/product/statusUp")
    Result<Void> productStatusUp(@RequestBody List<SkuEsModel> models);

    @PostMapping("/product/statusDown")
    Result<Void> productStatusDown(@RequestBody List<Long> skuIds);
}
