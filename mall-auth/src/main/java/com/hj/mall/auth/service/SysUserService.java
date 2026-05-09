package com.hj.mall.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.dto.SysUserDTO;
import com.hj.mall.auth.vo.AdminLoginVO;
import com.hj.mall.auth.vo.RouterVO;
import com.hj.mall.auth.vo.UserInfoVO;

/**
 * 系统用户服务接口
 */
public interface SysUserService {

    /**
     * 管理员登录
     *
     * @param username  用户名
     * @param password  密码
     * @param ip        客户端IP
     * @return 登录结果
     */
    AdminLoginVO login(String username, String password, String ip);

    /**
     * 获取用户信息
     *
     * @param userId    用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 获取用户路由
     *
     * @param userId    用户ID
     * @return 路由列表
     */
    java.util.List<RouterVO> getRouters(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param current   当前页
     * @param size      每页大小
     * @param username  用户名（可选）
     * @param phone     手机号（可选）
     * @param status    状态（可选）
     * @param deptId    部门ID（可选）
     * @return 用户分页列表
     */
    IPage<com.hj.mall.auth.entity.SysUser> listPage(long current, long size, String username, String phone, Integer status, Long deptId);

    /**
     * 根据ID查询用户
     *
     * @param id    用户ID
     * @return 用户信息
     */
    com.hj.mall.auth.entity.SysUser getById(Long id);

    /**
     * 创建用户
     *
     * @param userDTO   用户信息
     * @return 创建结果
     */
    boolean save(SysUserDTO userDTO);

    /**
     * 更新用户
     *
     * @param userDTO   用户信息
     * @return 更新结果
     */
    boolean updateById(SysUserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id    用户ID
     * @return 删除结果
     */
    boolean delete(Long id);

    /**
     * 重置密码
     *
     * @param userId        用户ID
     * @param newPassword   新密码
     * @return 重置结果
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 分配角色
     *
     * @param userId    用户ID
     * @param roleIds   角色ID列表
     * @return 分配结果
     */
    boolean assignRoles(Long userId, Long[] roleIds);

    /**
     * 更新个人信息
     *
     * @param userId    用户ID
     * @param userDTO   用户信息
     * @return 更新结果
     */
    boolean updateProfile(Long userId, SysUserDTO userDTO);

    /**
     * 修改密码
     *
     * @param userId        用户ID
     * @param oldPassword   旧密码
     * @param newPassword   新密码
     * @return 修改结果
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
}
