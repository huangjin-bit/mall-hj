package com.hj.mall.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.dto.SysRoleDTO;
import com.hj.mall.auth.entity.SysRole;

import java.util.List;

/**
 * 系统角色服务接口
 */
public interface SysRoleService {

    /**
     * 分页查询角色列表
     *
     * @param current   当前页
     * @param size      每页大小
     * @param roleName  角色名称（可选）
     * @param status    状态（可选）
     * @return 角色分页列表
     */
    IPage<SysRole> listPage(long current, long size, String roleName, Integer status);

    /**
     * 查询所有角色（用于下拉框）
     *
     * @return 角色列表
     */
    List<SysRole> listAll();

    /**
     * 根据ID查询角色
     *
     * @param id    角色ID
     * @return 角色信息
     */
    SysRole getById(Long id);

    /**
     * 创建角色
     *
     * @param roleDTO   角色信息
     * @return 创建结果
     */
    boolean save(SysRoleDTO roleDTO);

    /**
     * 更新角色
     *
     * @param roleDTO   角色信息
     * @return 更新结果
     */
    boolean updateById(SysRoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param id    角色ID
     * @return 删除结果
     */
    boolean delete(Long id);

    /**
     * 获取角色已分配的菜单ID列表
     *
     * @param roleId    角色ID
     * @return 菜单ID列表
     */
    List<Long> getAssignedMenuIds(Long roleId);

    /**
     * 为角色分配菜单
     *
     * @param roleId    角色ID
     * @param menuIds   菜单ID列表
     * @return 分配结果
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);
}
