package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SeckillOrder;

/**
 * 秒杀订单服务接口
 */
public interface SeckillOrderService {

    /**
     * 分页查询秒杀订单
     * @param page 分页参数
     * @param memberId 会员ID（可选）
     * @return 分页结果
     */
    IPage<SeckillOrder> listPage(Page<SeckillOrder> page, Long memberId);

    /**
     * 根据会员ID查询订单列表
     * @param memberId 会员ID
     * @return 订单列表
     */
    java.util.List<SeckillOrder> listByMemberId(Long memberId);

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    SeckillOrder getById(Long id);

    /**
     * 创建秒杀订单
     * @param order 订单信息
     */
    void save(SeckillOrder order);

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);
}
