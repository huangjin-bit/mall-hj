package com.hj.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.mall.auth.entity.SysRole;
import com.hj.mall.auth.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * 系统用户Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    java.util.List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询权限标识集合
     *
     * @param userId 用户ID
     * @return 权限标识集合
     */
    Set<String> selectPermsByUserId(@Param("userId") Long userId);
}
