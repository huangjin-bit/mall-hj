package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SeckillSkuRelation;

import java.util.List;

/**
 * 秒杀商品关联服务接口
 */
public interface SeckillSkuRelationService {

    /**
     * 分页查询秒杀商品
     * @param page 分页参数
     * @param sessionId 场次ID（可选）
     * @return 分页结果
     */
    IPage<SeckillSkuRelation> listPage(Page<SeckillSkuRelation> page, Long sessionId);

    /**
     * 根据场次ID查询商品列表
     * @param sessionId 场次ID
     * @return 商品列表
     */
    List<SeckillSkuRelation> listBySessionId(Long sessionId);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品信息
     */
    SeckillSkuRelation getById(Long id);

    /**
     * 保存商品
     * @param relation 商品信息
     */
    void save(SeckillSkuRelation relation);

    /**
     * 更新商品
     * @param relation 商品信息
     */
    void updateById(SeckillSkuRelation relation);

    /**
     * 批量删除商品
     * @param ids 商品ID列表
     */
    void removeBatch(List<Long> ids);
}
