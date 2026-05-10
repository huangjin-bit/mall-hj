package com.hj.mall.search.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.search.model.SkuEsModel;
import com.hj.mall.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/elastic")
@RequiredArgsConstructor
public class ElasticSaveController {

    private final SearchService searchService;

    @PostMapping("/product/statusUp")
    public Result<Void> productStatusUp(@RequestBody List<SkuEsModel> models) {
        log.info("[ES上架接口] 批量上架商品到ES，数量={}", models.size());
        searchService.saveBatch(models);
        return Result.success();
    }

    @PostMapping("/product/statusDown")
    public Result<Void> productStatusDown(@RequestBody List<Long> skuIds) {
        log.info("[ES下架接口] 批量下架商品，skuIds={}", skuIds);
        for (Long skuId : skuIds) {
            searchService.deleteBySkuId(skuId);
        }
        return Result.success();
    }
}
