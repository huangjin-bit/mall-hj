package com.hj.mall.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.dto.SysMenuDTO;
import com.hj.mall.auth.entity.SysMenu;
import com.hj.mall.auth.vo.RouterVO;
import com.hj.mall.auth.vo.SysMenuVO;

import java.util.List;

/**
 * 系统菜单服务接口
 */
public interface SysMenuService {

    /**
     * 查询菜单树
     *
     * @return 菜单树
     */
    List<SysMenuVO> selectMenuTree();

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId    用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenusByUserId(Long userId);

    /**
     * 构建菜单树
     *
     * @param menus 菜单列表
     * @return 菜单树
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 构建前端路由树
     *
     * @param menus 菜单列表
     * @return 路由树
     */
    List<RouterVO> buildRouterTree(List<SysMenu> menus);

    /**
     * 分页查询菜单列表
     *
     * @param current   当前页
     * @param size      每页大小
     * @param name      菜单名称（可选）
     * @param status    状态（可选）
     * @return 菜单分页列表
     */
    IPage<SysMenu> listPage(long current, long size, String name, Integer status);

    /**
     * 根据ID查询菜单
     *
     * @param id    菜单ID
     * @return 菜单信息
     */
    SysMenu getById(Long id);

    /**
     * 创建菜单
     *
     * @param menuDTO   菜单信息
     * @return 创建结果
     */
    boolean save(SysMenuDTO menuDTO);

    /**
     * 更新菜单
     *
     * @param menuDTO   菜单信息
     * @return 更新结果
     */
    boolean updateById(SysMenuDTO menuDTO);

    /**
     * 删除菜单
     *
     * @param id    菜单ID
     * @return 删除结果
     */
    boolean delete(Long id);
}
