package com.hj.mall.search.feign;

import com.hj.mall.common.dto.Result;
import com.hj.mall.search.model.SkuEsModel;
import com.hj.mall.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
public class SearchFeignController {

    private final SearchService searchService;

    @PostMapping("/product/statusUp")
    public Result<Void> productStatusUp(@RequestBody List<SkuEsModel> models) {
        log.info("[Feign接口] 商品上架，数量={}", models.size());
        searchService.saveBatch(models);
        return Result.success();
    }

    @PostMapping("/product/statusDown")
    public Result<Void> productStatusDown(@RequestBody List<Long> skuIds) {
        log.info("[Feign接口] 商品下架，skuIds={}", skuIds);
        for (Long skuId : skuIds) {
            searchService.deleteBySkuId(skuId);
        }
        return Result.success();
    }
}
