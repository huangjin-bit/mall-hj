package com.hj.mall.product.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SpuImages;
import com.hj.mall.product.service.SpuImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SPU图片管理控制器
 * 处理SPU商品图片的批量管理操作
 */
@RestController
@RequestMapping("/spu-images")
@RequiredArgsConstructor
public class SpuImagesController {

    private final SpuImagesService spuImagesService;

    /**
     * 查询指定SPU的所有图片
     */
    @GetMapping("/list/{spuId}")
    public Result<List<SpuImages>> listBySpuId(@PathVariable Long spuId) {
        return Result.ok(spuImagesService.listBySpuId(spuId));
    }

    /**
     * 批量保存SPU图片
     * 接收包含spuId和图片URL的请求体，创建SpuImages对象
     */
    @PostMapping
    public Result<Void> saveBatch(@RequestBody SpuImagesDTO dto) {
        spuImagesService.saveBatch(dto.getSpuId(), dto.getImageUrls());
        return Result.ok();
    }

    /**
     * 删除指定SPU的所有图片
     */
    @DeleteMapping("/{spuId}")
    public Result<Void> removeBySpuId(@PathVariable Long spuId) {
        spuImagesService.removeBySpuId(spuId);
        return Result.ok();
    }

    /**
     * DTO用于接收批量保存图片的请求
     */
    public static class SpuImagesDTO {
        private Long spuId;
        private List<String> imageUrls;

        public Long getSpuId() {
            return spuId;
        }

        public void setSpuId(Long spuId) {
            this.spuId = spuId;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }
}
