package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.HomeSubject;

import java.util.List;

/**
 * 首页专题服务接口
 */
public interface HomeSubjectService {

    /**
     * 分页查询专题
     * @param page 分页参数
     * @param name 专题名称（可选）
     * @return 分页结果
     */
    IPage<HomeSubject> listPage(Page<HomeSubject> page, String name);

    /**
     * 查询当前启用的专题列表
     * @return 专题列表
     */
    List<HomeSubject> listActiveSubjects();

    /**
     * 根据ID查询专题
     * @param id 专题ID
     * @return 专题信息
     */
    HomeSubject getById(Long id);

    /**
     * 保存专题
     * @param subject 专题信息
     */
    void save(HomeSubject subject);

    /**
     * 更新专题
     * @param subject 专题信息
     */
    void updateById(HomeSubject subject);

    /**
     * 批量删除专题
     * @param ids 专题ID列表
     */
    void removeBatch(List<Long> ids);
}
