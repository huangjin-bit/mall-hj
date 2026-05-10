package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SeckillSkuNotice;

import java.util.List;

/**
 * 秒杀商品提醒服务接口
 */
public interface SeckillSkuNoticeService {

    /**
     * 分页查询秒杀商品提醒
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SeckillSkuNotice> listPage(Page<SeckillSkuNotice> page);

    /**
     * 根据ID查询秒杀商品提醒
     * @param id ID
     * @return 秒杀商品提醒信息
     */
    SeckillSkuNotice getById(Long id);

    /**
     * 保存秒杀商品提醒
     * @param entity 秒杀商品提醒信息
     */
    void save(SeckillSkuNotice entity);

    /**
     * 更新秒杀商品提醒
     * @param entity 秒杀商品提醒信息
     */
    void updateById(SeckillSkuNotice entity);

    /**
     * 批量删除秒杀商品提醒
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
