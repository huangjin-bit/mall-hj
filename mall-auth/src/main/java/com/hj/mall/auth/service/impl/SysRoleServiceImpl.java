package com.hj.mall.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.mall.auth.dto.SysRoleDTO;
import com.hj.mall.auth.entity.SysRole;
import com.hj.mall.auth.entity.SysRoleMenu;
import com.hj.mall.auth.mapper.SysRoleMapper;
import com.hj.mall.auth.mapper.SysRoleMenuMapper;
import com.hj.mall.auth.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统角色服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public IPage<SysRole> listPage(long current, long size, String roleName, Integer status) {
        log.debug("[SysRoleServiceImpl] 分页查询角色列表, current={}, size={}", current, size);

        Page<SysRole> page = new Page<>(current, size);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        if (status != null) {
            wrapper.eq(SysRole::getStatus, status);
        }

        wrapper.orderByAsc(SysRole::getSort);

        return sysRoleMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysRole> listAll() {
        log.debug("[SysRoleServiceImpl] 查询所有角色");

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getStatus, 1);
        wrapper.orderByAsc(SysRole::getSort);

        return sysRoleMapper.selectList(wrapper);
    }

    @Override
    public SysRole getById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysRoleDTO roleDTO) {
        log.info("[SysRoleServiceImpl] 创建角色, roleName={}", roleDTO.getRoleName());

        SysRole role = new SysRole();
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setSort(roleDTO.getSort());
        role.setStatus(roleDTO.getStatus() != null ? roleDTO.getStatus() : 1);
        role.setRemark(roleDTO.getRemark());
        role.setDataScope(roleDTO.getDataScope() != null ? roleDTO.getDataScope() : 1);

        int result = sysRoleMapper.insert(role);

        // 分配菜单
        if (roleDTO.getMenuIds() != null && !roleDTO.getMenuIds().isEmpty()) {
            assignMenus(role.getId(), roleDTO.getMenuIds());
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysRoleDTO roleDTO) {
        log.info("[SysRoleServiceImpl] 更新角色, id={}", roleDTO.getId());

        SysRole role = sysRoleMapper.selectById(roleDTO.getId());
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }

        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setSort(roleDTO.getSort());
        role.setStatus(roleDTO.getStatus());
        role.setRemark(roleDTO.getRemark());
        role.setDataScope(roleDTO.getDataScope());

        int result = sysRoleMapper.updateById(role);

        // 更新菜单
        if (roleDTO.getMenuIds() != null) {
            assignMenus(role.getId(), roleDTO.getMenuIds());
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        log.info("[SysRoleServiceImpl] 删除角色, id={}", id);

        // 删除角色菜单关联
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, id);
        sysRoleMenuMapper.delete(wrapper);

        // 删除角色
        return sysRoleMapper.deleteById(id) > 0;
    }

    @Override
    public List<Long> getAssignedMenuIds(Long roleId) {
        log.debug("[SysRoleServiceImpl] 获取角色已分配的菜单ID列表, roleId={}", roleId);

        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        wrapper.select(SysRoleMenu::getMenuId);

        return sysRoleMenuMapper.selectList(wrapper).stream()
            .map(SysRoleMenu::getMenuId)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        log.info("[SysRoleServiceImpl] 为角色分配菜单, roleId={}, menuIds={}", roleId, menuIds);

        // 1. 删除原有菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuMapper.delete(wrapper);

        // 2. 分配新菜单
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                sysRoleMenuMapper.insert(roleMenu);
            }
        }

        return true;
    }
}
