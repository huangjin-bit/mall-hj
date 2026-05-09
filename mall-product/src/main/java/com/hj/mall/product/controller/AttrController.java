package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.Attr;
import com.hj.mall.product.service.AttrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性管理控制器
 * 处理商品基本属性和销售属性的增删改查操作
 */
@RestController
@RequestMapping("/attr")
@RequiredArgsConstructor
public class AttrController {

    private final AttrService attrService;

    /**
     * 分页查询属性列表
     */
    @GetMapping("/list")
    public Result<IPage<Attr>> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String key) {
        return Result.ok(attrService.listPage(new Page<>(page, size), key));
    }

    /**
     * 根据ID查询属性详情
     */
    @GetMapping("/{id}")
    public Result<Attr> getById(@PathVariable Long id) {
        return Result.ok(attrService.getDetail(id));
    }

    /**
     * 新增属性
     */
    @PostMapping
    public Result<Void> save(@RequestBody Attr entity) {
        attrService.save(entity);
        return Result.ok();
    }

    /**
     * 更新属性
     */
    @PutMapping
    public Result<Void> update(@RequestBody Attr entity) {
        attrService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除属性
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        attrService.removeBatch(ids);
        return Result.ok();
    }

    /**
     * 查询指定分类的基本属性列表（分页）
     * attrType=1 表示基本属性
     */
    @GetMapping("/base/list/{categoryId}")
    public Result<IPage<Attr>> listBaseAttrs(@PathVariable Long categoryId,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(attrService.listByTypeAndCategory(new Page<>(page, size), 1, categoryId));
    }

    /**
     * 查询指定分类的销售属性列表（分页）
     * attrType=2 表示销售属性
     */
    @GetMapping("/sale/list/{categoryId}")
    public Result<IPage<Attr>> listSaleAttrs(@PathVariable Long categoryId,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(attrService.listByTypeAndCategory(new Page<>(page, size), 2, categoryId));
    }
}
