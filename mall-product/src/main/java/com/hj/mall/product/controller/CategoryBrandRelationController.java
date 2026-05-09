package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.entity.CategoryBrandRelation;
import com.hj.mall.product.service.CategoryBrandRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类品牌关联管理控制器
 * 处理分类与品牌的多对多关联关系
 */
@RestController
@RequestMapping("/category-brand-relation")
@RequiredArgsConstructor
public class CategoryBrandRelationController {

    private final CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 分页查询分类品牌关联列表
     */
    @GetMapping("/list")
    public Result<IPage<CategoryBrandRelation>> list(@RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size,
                                                      @RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) Long brandId) {
        return Result.ok(categoryBrandRelationService.listPage(new Page<>(page, size), categoryId, brandId));
    }

    /**
     * 根据ID查询分类品牌关联详情
     * 注意：该服务接口未提供getById方法，此为占位实现
     */
    @GetMapping("/{id}")
    public Result<CategoryBrandRelation> getById(@PathVariable Long id) {
        // 由于CategoryBrandRelationService未提供getById方法，需要根据实际业务需求补充
        // 暂时返回null，需要在service实现类中添加该方法
        return Result.ok(null);
    }

    /**
     * 新增分类品牌关联
     */
    @PostMapping
    public Result<Void> save(@RequestBody CategoryBrandRelation entity) {
        categoryBrandRelationService.save(entity);
        return Result.ok();
    }

    /**
     * 更新分类品牌关联
     */
    @PutMapping
    public Result<Void> update(@RequestBody CategoryBrandRelation entity) {
        categoryBrandRelationService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除分类品牌关联
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        categoryBrandRelationService.removeBatch(ids);
        return Result.ok();
    }

    /**
     * 查询指定分类下的所有品牌
     */
    @GetMapping("/brands/list")
    public Result<List<Brand>> listBrandsByCategory(@RequestParam Long categoryId) {
        return Result.ok(categoryBrandRelationService.listBrandsByCategory(categoryId));
    }
}
