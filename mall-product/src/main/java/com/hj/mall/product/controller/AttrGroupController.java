package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.Attr;
import com.hj.mall.product.entity.AttrGroup;
import com.hj.mall.product.service.AttrGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性分组管理控制器
 * 处理属性分组的增删改查，以及分组与属性的关联关系
 */
@RestController
@RequestMapping("/attr-group")
@RequiredArgsConstructor
public class AttrGroupController {

    private final AttrGroupService attrGroupService;

    /**
     * 分页查询属性分组列表
     */
    @GetMapping("/list")
    public Result<IPage<AttrGroup>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long categoryId,
                                          @RequestParam(required = false) String key) {
        return Result.ok(attrGroupService.listPage(new Page<>(page, size), categoryId, key));
    }

    /**
     * 根据ID查询属性分组详情
     */
    @GetMapping("/{id}")
    public Result<AttrGroup> getById(@PathVariable Long id) {
        return Result.ok(attrGroupService.getById(id));
    }

    /**
     * 新增属性分组
     */
    @PostMapping
    public Result<Void> save(@RequestBody AttrGroup entity) {
        attrGroupService.save(entity);
        return Result.ok();
    }

    /**
     * 更新属性分组
     */
    @PutMapping
    public Result<Void> update(@RequestBody AttrGroup entity) {
        attrGroupService.updateById(entity);
        return Result.ok();
    }

    /**
     * 批量删除属性分组
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        attrGroupService.removeBatch(ids);
        return Result.ok();
    }

    /**
     * 查询指定属性分组下的所有属性
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public Result<List<Attr>> getGroupAttrs(@PathVariable Long attrGroupId) {
        return Result.ok(attrGroupService.getAttrRelation(attrGroupId));
    }

    /**
     * 查询不在指定属性分组中的属性列表（分页）
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public Result<IPage<Attr>> getAttrsNotInGroup(@PathVariable Long attrGroupId,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam(required = false) String key) {
        return Result.ok(attrGroupService.getNoAttrRelation(attrGroupId, new Page<>(page, size), key));
    }

    /**
     * 查询指定分类下的所有属性分组，并包含每个分组的属性信息
     */
    @GetMapping("/{categoryId}/withattr")
    public Result<List<AttrGroup>> listGroupsWithAttrs(@PathVariable Long categoryId) {
        return Result.ok(attrGroupService.listWithAttrs(categoryId));
    }
}
