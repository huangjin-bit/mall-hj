package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.HomeSubjectSpu;

import java.util.List;

/**
 * 首页专题商品关联服务接口
 */
public interface HomeSubjectSpuService {

    /**
     * 分页查询首页专题商品关联
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<HomeSubjectSpu> listPage(Page<HomeSubjectSpu> page);

    /**
     * 根据ID查询首页专题商品关联
     * @param id ID
     * @return 首页专题商品关联信息
     */
    HomeSubjectSpu getById(Long id);

    /**
     * 保存首页专题商品关联
     * @param entity 首页专题商品关联信息
     */
    void save(HomeSubjectSpu entity);

    /**
     * 更新首页专题商品关联
     * @param entity 首页专题商品关联信息
     */
    void updateById(HomeSubjectSpu entity);

    /**
     * 批量删除首页专题商品关联
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
