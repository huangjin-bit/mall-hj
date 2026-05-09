package com.hj.mall.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.dto.SysRoleDTO;
import com.hj.mall.auth.entity.SysRole;
import com.hj.mall.auth.service.SysRoleService;
import com.hj.mall.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public Result<IPage<SysRole>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status) {

        log.debug("[SysRoleController] 分页查询角色列表, current={}, size={}", current, size);

        IPage<SysRole> page = sysRoleService.listPage(current, size, roleName, status);
        return Result.ok(page);
    }

    /**
     * 查询所有角色（用于下拉框）
     */
    @GetMapping("/all")
    public Result<List<SysRole>> listAll() {
        log.debug("[SysRoleController] 查询所有角色");

        List<SysRole> roles = sysRoleService.listAll();
        return Result.ok(roles);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        log.debug("[SysRoleController] 查询角色, id={}", id);

        SysRole role = sysRoleService.getById(id);
        if (role == null) {
            return Result.error(404, "角色不存在");
        }
        return Result.ok(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<Void> save(@Valid @RequestBody SysRoleDTO roleDTO) {
        log.info("[SysRoleController] 创建角色, roleName={}", roleDTO.getRoleName());

        try {
            sysRoleService.save(roleDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysRoleController] 创建角色失败, roleName={}, error={}", roleDTO.getRoleName(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping
    public Result<Void> updateById(@Valid @RequestBody SysRoleDTO roleDTO) {
        log.info("[SysRoleController] 更新角色, id={}", roleDTO.getId());

        try {
            sysRoleService.updateById(roleDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysRoleController] 更新角色失败, id={}, error={}", roleDTO.getId(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("[SysRoleController] 删除角色, id={}", id);

        try {
            sysRoleService.delete(id);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysRoleController] 删除角色失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 获取角色已分配的菜单ID列表
     */
    @GetMapping("/{id}/menus")
    public Result<List<Long>> getAssignedMenuIds(@PathVariable Long id) {
        log.debug("[SysRoleController] 获取角色已分配的菜单ID列表, id={}", id);

        List<Long> menuIds = sysRoleService.getAssignedMenuIds(id);
        return Result.ok(menuIds);
    }

    /**
     * 为角色分配菜单
     */
    @PutMapping("/{id}/menus")
    public Result<Void> assignMenus(
            @PathVariable Long id,
            @RequestBody List<Long> menuIds) {

        log.info("[SysRoleController] 为角色分配菜单, id={}, menuIds={}", id, menuIds);

        try {
            sysRoleService.assignMenus(id, menuIds);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysRoleController] 为角色分配菜单失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }
}
