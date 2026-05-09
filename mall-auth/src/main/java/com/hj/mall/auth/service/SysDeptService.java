package com.hj.mall.auth.service;

import com.hj.mall.auth.entity.SysDept;
import com.hj.mall.auth.vo.SysDeptVO;

import java.util.List;

/**
 * 系统部门服务接口
 */
public interface SysDeptService {

    /**
     * 查询部门树
     *
     * @return 部门树
     */
    List<SysDeptVO> getDeptTree();

    /**
     * 查询所有部门（平铺列表）
     *
     * @return 部门列表
     */
    List<SysDept> listAll();

    /**
     * 根据ID查询部门
     *
     * @param id    部门ID
     * @return 部门信息
     */
    SysDept getById(Long id);

    /**
     * 创建部门
     *
     * @param dept  部门信息
     * @return 创建结果
     */
    boolean save(SysDept dept);

    /**
     * 更新部门
     *
     * @param dept  部门信息
     * @return 更新结果
     */
    boolean updateById(SysDept dept);

    /**
     * 删除部门
     *
     * @param id    部门ID
     * @return 删除结果
     */
    boolean delete(Long id);
}
