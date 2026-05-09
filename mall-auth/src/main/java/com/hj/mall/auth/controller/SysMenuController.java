package com.hj.mall.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.dto.SysMenuDTO;
import com.hj.mall.auth.entity.SysMenu;
import com.hj.mall.auth.service.SysMenuService;
import com.hj.mall.auth.vo.SysMenuVO;
import com.hj.mall.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 分页查询菜单列表
     */
    @GetMapping("/list")
    public Result<IPage<SysMenu>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {

        log.debug("[SysMenuController] 分页查询菜单列表, current={}, size={}", current, size);

        IPage<SysMenu> page = sysMenuService.listPage(current, size, name, status);
        return Result.ok(page);
    }

    /**
     * 查询菜单树
     */
    @GetMapping("/tree")
    public Result<List<SysMenuVO>> tree() {
        log.debug("[SysMenuController] 查询菜单树");

        List<SysMenuVO> tree = sysMenuService.selectMenuTree();
        return Result.ok(tree);
    }

    /**
     * 根据ID查询菜单
     */
    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        log.debug("[SysMenuController] 查询菜单, id={}", id);

        SysMenu menu = sysMenuService.getById(id);
        if (menu == null) {
            return Result.error(404, "菜单不存在");
        }
        return Result.ok(menu);
    }

    /**
     * 创建菜单
     */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody SysMenuDTO menuDTO) {
        log.info("[SysMenuController] 创建菜单, name={}", menuDTO.getName());

        try {
            sysMenuService.save(menuDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysMenuController] 创建菜单失败, name={}, error={}", menuDTO.getName(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新菜单
     */
    @PutMapping
    public Result<Void> updateById(@Valid @RequestBody SysMenuDTO menuDTO) {
        log.info("[SysMenuController] 更新菜单, id={}", menuDTO.getId());

        try {
            sysMenuService.updateById(menuDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysMenuController] 更新菜单失败, id={}, error={}", menuDTO.getId(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("[SysMenuController] 删除菜单, id={}", id);

        try {
            sysMenuService.delete(id);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysMenuController] 删除菜单失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }
}
