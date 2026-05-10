package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SkuFullReduction;

import java.util.List;

/**
 * 商品满减信息服务接口
 */
public interface SkuFullReductionService {

    /**
     * 分页查询商品满减信息
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SkuFullReduction> listPage(Page<SkuFullReduction> page);

    /**
     * 根据ID查询商品满减信息
     * @param id ID
     * @return 商品满减信息
     */
    SkuFullReduction getById(Long id);

    /**
     * 保存商品满减信息
     * @param entity 商品满减信息
     */
    void save(SkuFullReduction entity);

    /**
     * 更新商品满减信息
     * @param entity 商品满减信息
     */
    void updateById(SkuFullReduction entity);

    /**
     * 批量删除商品满减信息
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
