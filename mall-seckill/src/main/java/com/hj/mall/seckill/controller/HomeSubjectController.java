package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.HomeSubject;
import com.hj.mall.seckill.service.HomeSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页专题控制器
 */
@RestController
@RequestMapping("/home-subject")
@RequiredArgsConstructor
public class HomeSubjectController {

    private final HomeSubjectService homeSubjectService;

    /**
     * 分页查询专题
     */
    @GetMapping("/list")
    public Result<IPage<HomeSubject>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        IPage<HomeSubject> result = homeSubjectService.listPage(new Page<>(page, size), name);
        return Result.ok(result);
    }

    /**
     * 查询当前启用的专题列表
     */
    @GetMapping("/active")
    public Result<List<HomeSubject>> listActiveSubjects() {
        List<HomeSubject> list = homeSubjectService.listActiveSubjects();
        return Result.ok(list);
    }

    /**
     * 根据ID查询专题
     */
    @GetMapping("/{id}")
    public Result<HomeSubject> getById(@PathVariable Long id) {
        HomeSubject subject = homeSubjectService.getById(id);
        return Result.ok(subject);
    }

    /**
     * 保存专题
     */
    @PostMapping
    public Result<Void> save(@RequestBody HomeSubject subject) {
        homeSubjectService.save(subject);
        return Result.ok();
    }

    /**
     * 更新专题
     */
    @PutMapping
    public Result<Void> update(@RequestBody HomeSubject subject) {
        homeSubjectService.updateById(subject);
        return Result.ok();
    }

    /**
     * 批量删除专题
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        homeSubjectService.removeBatch(ids);
        return Result.ok();
    }
}
