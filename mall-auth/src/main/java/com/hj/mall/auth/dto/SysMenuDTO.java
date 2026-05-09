package com.hj.mall.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统菜单DTO
 */
@Data
public class SysMenuDTO implements Serializable {

    /**
     * 菜单ID（修改时需要）
     */
    private Long id;

    /**
     * 父菜单ID（根菜单为0）
     */
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 菜单类型：M-目录 C-菜单 F-按钮
     */
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 是否可见：0-隐藏 1-显示
     */
    private Integer visible;

    /**
     * 菜单状态：0-禁用 1-正常
     */
    private Integer status;
}
