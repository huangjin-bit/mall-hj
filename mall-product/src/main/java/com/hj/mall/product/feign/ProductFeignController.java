package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.entity.SpuInfo;
import com.hj.mall.product.service.CategoryService;
import com.hj.mall.product.service.SkuService;
import com.hj.mall.product.service.SpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
public class ProductFeignController {

    private final SkuService skuService;
    private final SpuService spuService;
    private final CategoryService categoryService;

    @GetMapping("/sku/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable Long skuId) {
        return Result.ok(skuService.getSkuInfo(skuId));
    }

    @GetMapping("/spu/{spuId}")
    public Result<SpuInfo> getSpuInfo(@PathVariable Long spuId) {
        return Result.ok(spuService.getSpuInfo(spuId));
    }

    @DeleteMapping("/cache/category")
    public Result<Void> refreshCategoryCache() {
        categoryService.refreshCategoryTreeCache();
        return Result.ok(null);
    }
}
