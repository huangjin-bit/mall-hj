package com.hj.mall.product.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SkuImages;
import com.hj.mall.product.service.SkuImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SKU图片管理控制器
 * 处理SKU商品图片的批量管理操作
 */
@RestController
@RequestMapping("/sku-images")
@RequiredArgsConstructor
public class SkuImagesController {

    private final SkuImagesService skuImagesService;

    /**
     * 查询指定SKU的所有图片
     */
    @GetMapping("/list/{skuId}")
    public Result<List<SkuImages>> listBySkuId(@PathVariable Long skuId) {
        return Result.ok(skuImagesService.listBySkuId(skuId));
    }

    /**
     * 批量保存SKU图片
     * 接收包含skuId和SkuImages对象的列表
     */
    @PostMapping
    public Result<Void> saveBatch(@RequestBody SkuImagesDTO dto) {
        skuImagesService.saveBatch(dto.getSkuId(), dto.getImages());
        return Result.ok();
    }

    /**
     * 删除指定SKU的所有图片
     */
    @DeleteMapping("/{skuId}")
    public Result<Void> removeBySkuId(@PathVariable Long skuId) {
        skuImagesService.removeBySkuId(skuId);
        return Result.ok();
    }

    /**
     * DTO用于接收批量保存图片的请求
     */
    public static class SkuImagesDTO {
        private Long skuId;
        private List<SkuImages> images;

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public List<SkuImages> getImages() {
            return images;
        }

        public void setImages(List<SkuImages> images) {
            this.images = images;
        }
    }
}
