package com.hj.mall.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.mall.auth.dto.SysMenuDTO;
import com.hj.mall.auth.entity.SysMenu;
import com.hj.mall.auth.mapper.SysMenuMapper;
import com.hj.mall.auth.service.SysMenuService;
import com.hj.mall.auth.vo.RouterVO;
import com.hj.mall.auth.vo.SysMenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统菜单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuVO> selectMenuTree() {
        log.debug("[SysMenuServiceImpl] 查询菜单树");

        List<SysMenu> menus = sysMenuMapper.selectMenuTree();
        List<SysMenu> menuTree = buildMenuTree(menus);

        return menuTree.stream()
            .map(this::convertToMenuVO)
            .collect(Collectors.toList());
    }

    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        log.debug("[SysMenuServiceImpl] 根据用户ID查询菜单, userId={}", userId);
        return sysMenuMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> tree = new ArrayList<>();
        List<SysMenu> rootMenus = menus.stream()
            .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
            .collect(Collectors.toList());

        for (SysMenu rootMenu : rootMenus) {
            buildChildren(rootMenu, menus);
            tree.add(rootMenu);
        }

        return tree;
    }

    /**
     * 递归构建子菜单
     */
    private void buildChildren(SysMenu parent, List<SysMenu> allMenus) {
        List<SysMenu> children = allMenus.stream()
            .filter(menu -> parent.getId().equals(menu.getParentId()))
            .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parent.setChildren(new ArrayList<>());
            parent.getChildren().addAll(children);

            for (SysMenu child : children) {
                buildChildren(child, allMenus);
            }
        }
    }

    @Override
    public List<RouterVO> buildRouterTree(List<SysMenu> menus) {
        List<SysMenu> menuTree = buildMenuTree(menus);
        List<RouterVO> routers = new ArrayList<>();

        for (SysMenu menu : menuTree) {
            RouterVO router = convertToRouter(menu);
            routers.add(router);
        }

        return routers;
    }

    /**
     * 转换为前端路由
     */
    private RouterVO convertToRouter(SysMenu menu) {
        RouterVO.RouterVOBuilder builder = RouterVO.builder()
            .name(menu.getName())
            .path(menu.getPath())
            .component(menu.getComponent())
            .redirect(menu.getRedirect());

        // 构建元信息
        RouterVO.Meta meta = RouterVO.Meta.builder()
            .title(menu.getName())
            .icon(menu.getIcon())
            .noCache(0)
            .build();
        builder.meta(meta);

        // 递归处理子菜单
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            List<RouterVO> children = menu.getChildren().stream()
                .filter(child -> "C".equals(child.getMenuType())) // 只包含菜单类型
                .map(this::convertToRouter)
                .collect(Collectors.toList());
            builder.children(children);
        }

        return builder.build();
    }

    /**
     * 转换为菜单VO
     */
    private SysMenuVO convertToMenuVO(SysMenu menu) {
        SysMenuVO menuVO = SysMenuVO.fromSysMenu(menu);

        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            List<SysMenuVO> children = menu.getChildren().stream()
                .map(this::convertToMenuVO)
                .collect(Collectors.toList());
            menuVO.setChildren(children);
        }

        return menuVO;
    }

    @Override
    public IPage<SysMenu> listPage(long current, long size, String name, Integer status) {
        log.debug("[SysMenuServiceImpl] 分页查询菜单列表, current={}, size={}", current, size);

        Page<SysMenu> page = new Page<>(current, size);
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(name)) {
            wrapper.like(SysMenu::getName, name);
        }
        if (status != null) {
            wrapper.eq(SysMenu::getStatus, status);
        }

        wrapper.orderByAsc(SysMenu::getSort);

        return sysMenuMapper.selectPage(page, wrapper);
    }

    @Override
    public SysMenu getById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    @Override
    public boolean save(SysMenuDTO menuDTO) {
        log.info("[SysMenuServiceImpl] 创建菜单, name={}", menuDTO.getName());

        SysMenu menu = new SysMenu();
        menu.setParentId(menuDTO.getParentId());
        menu.setName(menuDTO.getName());
        menu.setPath(menuDTO.getPath());
        menu.setComponent(menuDTO.getComponent());
        menu.setRedirect(menuDTO.getRedirect());
        menu.setMenuType(menuDTO.getMenuType());
        menu.setPerms(menuDTO.getPerms());
        menu.setIcon(menuDTO.getIcon());
        menu.setSort(menuDTO.getSort());
        menu.setVisible(menuDTO.getVisible() != null ? menuDTO.getVisible() : 1);
        menu.setStatus(menuDTO.getStatus() != null ? menuDTO.getStatus() : 1);

        return sysMenuMapper.insert(menu) > 0;
    }

    @Override
    public boolean updateById(SysMenuDTO menuDTO) {
        log.info("[SysMenuServiceImpl] 更新菜单, id={}", menuDTO.getId());

        SysMenu menu = sysMenuMapper.selectById(menuDTO.getId());
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }

        menu.setParentId(menuDTO.getParentId());
        menu.setName(menuDTO.getName());
        menu.setPath(menuDTO.getPath());
        menu.setComponent(menuDTO.getComponent());
        menu.setRedirect(menuDTO.getRedirect());
        menu.setMenuType(menuDTO.getMenuType());
        menu.setPerms(menuDTO.getPerms());
        menu.setIcon(menuDTO.getIcon());
        menu.setSort(menuDTO.getSort());
        menu.setVisible(menuDTO.getVisible());
        menu.setStatus(menuDTO.getStatus());

        return sysMenuMapper.updateById(menu) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        log.info("[SysMenuServiceImpl] 删除菜单, id={}", id);

        // 检查是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        long count = sysMenuMapper.selectCount(wrapper);

        if (count > 0) {
            throw new RuntimeException("存在子菜单，不允许删除");
        }

        return sysMenuMapper.deleteById(id) > 0;
    }
}
