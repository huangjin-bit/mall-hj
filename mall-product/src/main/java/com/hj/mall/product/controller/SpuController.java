package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SpuInfo;
import com.hj.mall.product.service.SpuService;
import com.hj.mall.product.vo.SpuDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spu")
@RequiredArgsConstructor
public class SpuController {

    private final SpuService spuService;

    // Admin list (paginated with key + status filter)
    @GetMapping("/list")
    public Result<IPage<SpuInfo>> list(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) String key,
                                        @RequestParam(required = false) Integer status) {
        Page<SpuInfo> p = new Page<>(page, size);
        return Result.ok(spuService.listPage(p, key, status));
    }

    // Admin save
    @PostMapping
    public Result<Void> save(@RequestBody SpuInfo spu) {
        spuService.save(spu);
        return Result.ok();
    }

    // Admin update
    @PutMapping
    public Result<Void> update(@RequestBody SpuInfo spu) {
        spuService.updateById(spu);
        return Result.ok();
    }

    // Admin delete
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        spuService.removeBatch(ids);
        return Result.ok();
    }

    // Publish/unpublish
    @PostMapping("/{spuId}/publish")
    public Result<Void> publish(@PathVariable Long spuId, @RequestParam Integer status) {
        spuService.publish(spuId, status);
        return Result.ok();
    }

    @GetMapping("/{spuId}")
    public Result<SpuDetailVO> getSpuDetail(@PathVariable Long spuId) {
        return Result.ok(spuService.getSpuDetail(spuId));
    }
}
