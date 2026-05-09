package com.hj.mall.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.mall.auth.dto.SysUserDTO;
import com.hj.mall.auth.entity.SysMenu;
import com.hj.mall.auth.entity.SysRole;
import com.hj.mall.auth.entity.SysUser;
import com.hj.mall.auth.entity.SysUserRole;
import com.hj.mall.auth.mapper.SysMenuMapper;
import com.hj.mall.auth.mapper.SysRoleMapper;
import com.hj.mall.auth.mapper.SysUserRoleMapper;
import com.hj.mall.auth.mapper.SysUserMapper;
import com.hj.mall.auth.service.SysMenuService;
import com.hj.mall.auth.service.SysUserService;
import com.hj.mall.auth.util.JwtUtil;
import com.hj.mall.auth.vo.AdminLoginVO;
import com.hj.mall.auth.vo.RouterVO;
import com.hj.mall.auth.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysMenuService sysMenuService;
    private final JwtUtil jwtUtil;

    private static final int USER_STATUS_NORMAL = 1;
    private static final int USER_STATUS_DISABLED = 0;

    @Override
    public AdminLoginVO login(String username, String password, String ip) {
        log.info("[SysUserServiceImpl] 管理员登录, username={}, ip={}", username, ip);

        // 1. 查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            log.warn("[SysUserServiceImpl] 用户不存在, username={}", username);
            throw new RuntimeException("用户名或密码错误");
        }

        // 2. 验证密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("[SysUserServiceImpl] 密码错误, username={}", username);
            throw new RuntimeException("用户名或密码错误");
        }

        // 3. 检查状态
        if (user.getStatus() == null || user.getStatus() != USER_STATUS_NORMAL) {
            log.warn("[SysUserServiceImpl] 账号已被禁用, username={}", username);
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 更新登录信息
        user.setLoginIp(ip);
        user.setLoginDate(LocalDateTime.now());
        sysUserMapper.updateById(user);

        // 5. 生成 Token
        String token = jwtUtil.generateAdminToken(user.getId(), user.getUsername());

        // 6. 返回登录结果
        return AdminLoginVO.builder()
            .token(token)
            .userId(user.getId())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .avatar(user.getAvatar())
            .build();
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        log.debug("[SysUserServiceImpl] 获取用户信息, userId={}", userId);

        // 1. 查询用户
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 查询角色
        List<SysRole> roles = sysUserMapper.selectRolesByUserId(userId);
        List<String> roleKeys = roles.stream()
            .map(SysRole::getRoleKey)
            .collect(Collectors.toList());

        // 3. 查询权限
        Set<String> permissions = sysUserMapper.selectPermsByUserId(userId);

        return UserInfoVO.builder()
            .user(user)
            .roles(roleKeys)
            .permissions(permissions)
            .build();
    }

    @Override
    public List<RouterVO> getRouters(Long userId) {
        log.debug("[SysUserServiceImpl] 获取用户路由, userId={}", userId);

        // 1. 查询用户菜单
        List<SysMenu> menus = sysMenuMapper.selectMenusByUserId(userId);

        // 2. 构建路由树
        return sysMenuService.buildRouterTree(menus);
    }

    @Override
    public IPage<SysUser> listPage(long current, long size, String username, String phone, Integer status, Long deptId) {
        log.debug("[SysUserServiceImpl] 分页查询用户列表, current={}, size={}", current, size);

        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        if (StringUtils.hasText(phone)) {
            wrapper.like(SysUser::getPhone, phone);
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        if (deptId != null) {
            wrapper.eq(SysUser::getDeptId, deptId);
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        IPage<SysUser> userPage = sysUserMapper.selectPage(page, wrapper);

        // 为每个用户填充角色信息
        userPage.getRecords().forEach(user -> {
            List<SysRole> roles = sysUserMapper.selectRolesByUserId(user.getId());
            user.setRoles(roles);
        });

        return userPage;
    }

    @Override
    public SysUser getById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user != null) {
            List<SysRole> roles = sysUserMapper.selectRolesByUserId(id);
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysUserDTO userDTO) {
        log.info("[SysUserServiceImpl] 创建用户, username={}", userDTO.getUsername());

        // 1. 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, userDTO.getUsername());
        if (sysUserMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建用户
        SysUser user = new SysUser();
        user.setUsername(userDTO.getUsername());
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setAvatar(userDTO.getAvatar());
        user.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : USER_STATUS_NORMAL);
        user.setDeptId(userDTO.getDeptId());
        user.setRemark(userDTO.getRemark());

        // 3. 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        int result = sysUserMapper.insert(user);

        // 4. 分配角色
        if (userDTO.getRoleIds() != null && userDTO.getRoleIds().length > 0) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysUserDTO userDTO) {
        log.info("[SysUserServiceImpl] 更新用户, id={}", userDTO.getId());

        SysUser user = sysUserMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setAvatar(userDTO.getAvatar());
        user.setStatus(userDTO.getStatus());
        user.setDeptId(userDTO.getDeptId());
        user.setRemark(userDTO.getRemark());

        // 如果提供了新密码，则更新密码
        if (StringUtils.hasText(userDTO.getPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        int result = sysUserMapper.updateById(user);

        // 更新角色
        if (userDTO.getRoleIds() != null) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        log.info("[SysUserServiceImpl] 删除用户, id={}", id);

        // 删除用户角色关联
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, id);
        sysUserRoleMapper.delete(wrapper);

        // 删除用户
        return sysUserMapper.deleteById(id) > 0;
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        log.info("[SysUserServiceImpl] 重置密码, userId={}", userId);

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(newPassword));

        return sysUserMapper.updateById(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, Long[] roleIds) {
        log.info("[SysUserServiceImpl] 分配角色, userId={}, roleIds={}", userId, roleIds);

        // 1. 删除原有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        sysUserRoleMapper.delete(wrapper);

        // 2. 分配新角色
        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }

        return true;
    }

    @Override
    public boolean updateProfile(Long userId, SysUserDTO userDTO) {
        log.info("[SysUserServiceImpl] 更新个人信息, userId={}", userId);

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setAvatar(userDTO.getAvatar());

        return sysUserMapper.updateById(user) > 0;
    }

    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        log.info("[SysUserServiceImpl] 修改密码, userId={}", userId);

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新新密码
        user.setPassword(passwordEncoder.encode(newPassword));

        return sysUserMapper.updateById(user) > 0;
    }
}
