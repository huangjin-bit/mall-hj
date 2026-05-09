package com.hj.mall.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.dto.SysUserDTO;
import com.hj.mall.auth.entity.SysUser;
import com.hj.mall.auth.service.SysUserService;
import com.hj.mall.auth.util.JwtUtil;
import com.hj.mall.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;
    private final JwtUtil jwtUtil;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Result<IPage<SysUser>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long deptId) {

        log.debug("[SysUserController] 分页查询用户列表, current={}, size={}", current, size);

        IPage<SysUser> page = sysUserService.listPage(current, size, username, phone, status, deptId);
        return Result.ok(page);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        log.debug("[SysUserController] 查询用户, id={}", id);

        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.ok(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<Void> save(@Validated(SysUserDTO.Create.class) @RequestBody SysUserDTO userDTO) {
        log.info("[SysUserController] 创建用户, username={}", userDTO.getUsername());

        try {
            sysUserService.save(userDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 创建用户失败, username={}, error={}", userDTO.getUsername(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<Void> updateById(@Valid @RequestBody SysUserDTO userDTO) {
        log.info("[SysUserController] 更新用户, id={}", userDTO.getId());

        try {
            sysUserService.updateById(userDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 更新用户失败, id={}, error={}", userDTO.getId(), e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("[SysUserController] 删除用户, id={}", id);

        try {
            sysUserService.delete(id);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 删除用户失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(HttpServletRequest request, @Valid @RequestBody SysUserDTO userDTO) {
        Long userId = jwtUtil.getUserId(extractToken(request));

        log.info("[SysUserController] 更新个人信息, userId={}", userId);

        try {
            sysUserService.updateProfile(userId, userDTO);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 更新个人信息失败, userId={}, error={}", userId, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(
            HttpServletRequest request,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        Long userId = jwtUtil.getUserId(extractToken(request));

        log.info("[SysUserController] 修改密码, userId={}", userId);

        try {
            sysUserService.updatePassword(userId, oldPassword, newPassword);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 修改密码失败, userId={}, error={}", userId, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 重置用户密码（管理员操作）
     */
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {

        log.info("[SysUserController] 重置用户密码, id={}", id);

        try {
            sysUserService.resetPassword(id, newPassword);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 重置用户密码失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 分配角色
     */
    @PutMapping("/{id}/roles")
    public Result<Void> assignRoles(
            @PathVariable Long id,
            @RequestBody Long[] roleIds) {

        log.info("[SysUserController] 分配角色, id={}, roleIds={}", id, roleIds);

        try {
            sysUserService.assignRoles(id, roleIds);
            return Result.ok();
        } catch (RuntimeException e) {
            log.error("[SysUserController] 分配角色失败, id={}, error={}", id, e.getMessage());
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 从请求中提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}
