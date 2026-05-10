package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SpuBounds;

import java.util.List;

/**
 * SPU积分成长服务接口
 */
public interface SpuBoundsService {

    /**
     * 分页查询SPU积分成长
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SpuBounds> listPage(Page<SpuBounds> page);

    /**
     * 根据ID查询SPU积分成长
     * @param id ID
     * @return SPU积分成长信息
     */
    SpuBounds getById(Long id);

    /**
     * 保存SPU积分成长
     * @param entity SPU积分成长信息
     */
    void save(SpuBounds entity);

    /**
     * 更新SPU积分成长
     * @param entity SPU积分成长信息
     */
    void updateById(SpuBounds entity);

    /**
     * 批量删除SPU积分成长
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
