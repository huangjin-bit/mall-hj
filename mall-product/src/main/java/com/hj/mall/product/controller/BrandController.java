package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.service.BrandService;
import com.hj.mall.product.vo.BrandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // Admin list (paginated with key search)
    @GetMapping("/list")
    public Result<IPage<Brand>> list(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String key) {
        Page<Brand> p = new Page<>(page, size);
        return Result.ok(brandService.listPage(p, key));
    }

    // Admin info
    @GetMapping("/{id}")
    public Result<Brand> getById(@PathVariable Long id) {
        return Result.ok(brandService.getById(id));
    }

    // Admin save
    @PostMapping
    public Result<Void> save(@RequestBody Brand brand) {
        brandService.save(brand);
        return Result.ok();
    }

    // Admin update
    @PutMapping
    public Result<Void> update(@RequestBody Brand brand) {
        brandService.updateById(brand);
        return Result.ok();
    }

    // Admin delete
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        brandService.removeBatch(ids);
        return Result.ok();
    }

    @GetMapping
    public Result<List<BrandVO>> listBrands(
            @RequestParam(required = false) Long categoryId) {
        return Result.ok(brandService.listBrands(categoryId));
    }
}
