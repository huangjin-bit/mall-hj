package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.Category;
import com.hj.mall.product.service.CategoryService;
import com.hj.mall.product.vo.CategoryTreeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Admin list (flat paginated)
    @GetMapping("/list")
    public Result<IPage<Category>> list(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        Page<Category> p = new Page<>(page, size);
        return Result.ok(categoryService.listPage(p));
    }

    // Admin info
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        return Result.ok(categoryService.getById(id));
    }

    // Admin save
    @PostMapping
    public Result<Void> save(@RequestBody Category category) {
        categoryService.save(category);
        return Result.ok();
    }

    // Admin update
    @PutMapping
    public Result<Void> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.ok();
    }

    // Admin delete
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        categoryService.removeBatch(ids);
        return Result.ok();
    }

    @GetMapping("/tree")
    public Result<List<CategoryTreeVO>> getCategoryTree() {
        return Result.ok(categoryService.getCategoryTree());
    }
}
