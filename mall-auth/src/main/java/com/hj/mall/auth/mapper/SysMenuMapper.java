package com.hj.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.mall.auth.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单Mapper
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 查询所有菜单树
     *
     * @return 所有菜单
     */
    List<SysMenu> selectMenuTree();
}
