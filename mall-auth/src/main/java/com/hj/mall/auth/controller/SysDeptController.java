package com.hj.mall.auth.controller;

import com.hj.mall.auth.entity.SysDept;
import com.hj.mall.auth.service.SysDeptService;
import com.hj.mall.auth.vo.SysDeptVO;
import com.hj.mall.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统部门控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService sysDeptService;

    /**
     * 查询部门树
     */
    @GetMapping("/tree")
    public Result<List<SysDeptVO>> tree() {
        log.debug("[SysDeptController] 查询部门树");

        List<SysDeptVO> tree = sysDeptService.getDeptTree();
        return Result.ok(tree);
    }

    /**
     * 查询所有部门（平铺列表）
     */
    @GetMapping("/list")
    public Result<List<SysDept>> listAll() {
        log.debug("[SysDeptController] 查询所有部门");

        List<SysDept> depts = sysDeptService.listAll();
        return Result.ok(depts);
    }

    /**
     * 根据ID查询部门
     */
    @GetMapping("/{id}")
    public Result<SysDept> getById(@PathVariable Long id) {
        log.debug("[SysDeptController] 查询部门, id={}", id);

        SysDept dept = sysDeptService.getById(id);
        if (dept == null) {
            return Result.error(404, "部门不存在");
        }
        return Result.ok(dept);
    }

    /**
     * 创建部门
     */
    @PostMapping
    public Result<Void> save(@RequestBody SysDept dept) {
        log.info("[SysDeptController] 创建部门, deptName={}", dept.getDeptName());

        try {
            sysDeptService.save(dept);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysDeptController] 创建部门失败, deptName={}, error={}", dept.getDeptName(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新部门
     */
    @PutMapping
    public Result<Void> updateById(@RequestBody SysDept dept) {
        log.info("[SysDeptController] 更新部门, id={}", dept.getId());

        try {
            sysDeptService.updateById(dept);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysDeptController] 更新部门失败, id={}, error={}", dept.getId(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("[SysDeptController] 删除部门, id={}", id);

        try {
            sysDeptService.delete(id);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysDeptController] 删除部门失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }
}
