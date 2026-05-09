package com.hj.mall.auth.vo;

import com.hj.mall.auth.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 系统菜单VO（带子菜单）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuVO implements Serializable {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
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
    private Integer sort;

    /**
     * 是否可见：0-隐藏 1-显示
     */
    private Integer visible;

    /**
     * 菜单状态：0-禁用 1-正常
     */
    private Integer status;

    /**
     * 子菜单
     */
    private List<SysMenuVO> children;

    /**
     * 从 SysMenu 转换
     */
    public static SysMenuVO fromSysMenu(SysMenu menu) {
        return SysMenuVO.builder()
            .id(menu.getId())
            .parentId(menu.getParentId())
            .name(menu.getName())
            .path(menu.getPath())
            .component(menu.getComponent())
            .redirect(menu.getRedirect())
            .menuType(menu.getMenuType())
            .perms(menu.getPerms())
            .icon(menu.getIcon())
            .sort(menu.getSort())
            .visible(menu.getVisible())
            .status(menu.getStatus())
            .build();
    }
}
