package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SeckillPromotion;

import java.util.List;

/**
 * 秒杀活动服务接口
 */
public interface SeckillPromotionService {

    /**
     * 分页查询秒杀活动
     * @param page 分页参数
     * @param title 活动标题（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    IPage<SeckillPromotion> listPage(Page<SeckillPromotion> page, String title, Integer status);

    /**
     * 根据ID查询秒杀活动
     * @param id ID
     * @return 秒杀活动信息
     */
    SeckillPromotion getById(Long id);

    /**
     * 保存秒杀活动
     * @param entity 秒杀活动信息
     */
    void save(SeckillPromotion entity);

    /**
     * 更新秒杀活动
     * @param entity 秒杀活动信息
     */
    void updateById(SeckillPromotion entity);

    /**
     * 批量删除秒杀活动
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
