package com.hj.mall.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.mall.auth.entity.SysDept;
import com.hj.mall.auth.mapper.SysDeptMapper;
import com.hj.mall.auth.service.SysDeptService;
import com.hj.mall.auth.vo.SysDeptVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统部门服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDeptVO> getDeptTree() {
        log.debug("[SysDeptServiceImpl] 查询部门树");

        List<SysDept> depts = listAll();
        List<SysDept> deptTree = buildDeptTree(depts);

        return deptTree.stream()
            .map(this::convertToDeptVO)
            .collect(Collectors.toList());
    }

    @Override
    public List<SysDept> listAll() {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getStatus, 1);
        wrapper.orderByAsc(SysDept::getSort);
        return sysDeptMapper.selectList(wrapper);
    }

    /**
     * 构建部门树
     */
    private List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> tree = new ArrayList<>();
        List<SysDept> rootDepts = depts.stream()
            .filter(dept -> dept.getParentId() == null || dept.getParentId() == 0)
            .collect(Collectors.toList());

        for (SysDept rootDept : rootDepts) {
            buildDeptChildren(rootDept, depts);
            tree.add(rootDept);
        }

        return tree;
    }

    /**
     * 递归构建子部门
     */
    private void buildDeptChildren(SysDept parent, List<SysDept> allDepts) {
        List<SysDept> children = allDepts.stream()
            .filter(dept -> parent.getId().equals(dept.getParentId()))
            .collect(Collectors.toList());

        if (!children.isEmpty()) {
            parent.setChildren(new ArrayList<>());
            parent.getChildren().addAll(children);

            for (SysDept child : children) {
                buildDeptChildren(child, allDepts);
            }
        }
    }

    /**
     * 转换为部门VO
     */
    private SysDeptVO convertToDeptVO(SysDept dept) {
        SysDeptVO deptVO = SysDeptVO.fromSysDept(dept);

        if (dept.getChildren() != null && !dept.getChildren().isEmpty()) {
            List<SysDeptVO> children = dept.getChildren().stream()
                .map(this::convertToDeptVO)
                .collect(Collectors.toList());
            deptVO.setChildren(children);
        }

        return deptVO;
    }

    @Override
    public SysDept getById(Long id) {
        return sysDeptMapper.selectById(id);
    }

    @Override
    public boolean save(SysDept dept) {
        log.info("[SysDeptServiceImpl] 创建部门, deptName={}", dept.getDeptName());

        // 构建祖级信息
        if (dept.getParentId() != null && dept.getParentId() != 0) {
            SysDept parent = sysDeptMapper.selectById(dept.getParentId());
            if (parent != null) {
                String ancestors = parent.getAncestors() != null ? parent.getAncestors() : "";
                dept.setAncestors(ancestors + parent.getId() + ",");
            }
        } else {
            dept.setAncestors("0,");
        }

        return sysDeptMapper.insert(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysDept dept) {
        log.info("[SysDeptServiceImpl] 更新部门, id={}", dept.getId());

        SysDept existDept = sysDeptMapper.selectById(dept.getId());
        if (existDept == null) {
            throw new RuntimeException("部门不存在");
        }

        // 如果修改了父部门，需要更新祖级信息
        if (!existDept.getParentId().equals(dept.getParentId())) {
            if (dept.getParentId() != null && dept.getParentId() != 0) {
                SysDept parent = sysDeptMapper.selectById(dept.getParentId());
                if (parent != null) {
                    String ancestors = parent.getAncestors() != null ? parent.getAncestors() : "";
                    dept.setAncestors(ancestors + parent.getId() + ",");
                }
            } else {
                dept.setAncestors("0,");
            }
        }

        return sysDeptMapper.updateById(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        log.info("[SysDeptServiceImpl] 删除部门, id={}", id);

        // 检查是否有子部门
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, id);
        long count = sysDeptMapper.selectCount(wrapper);

        if (count > 0) {
            throw new RuntimeException("存在子部门，不允许删除");
        }

        return sysDeptMapper.deleteById(id) > 0;
    }
}
