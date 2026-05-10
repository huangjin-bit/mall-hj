package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.CouponSpuRelation;

import java.util.List;

/**
 * 优惠券商品关联服务接口
 */
public interface CouponSpuRelationService {

    /**
     * 分页查询优惠券商品关联
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<CouponSpuRelation> listPage(Page<CouponSpuRelation> page);

    /**
     * 根据ID查询优惠券商品关联
     * @param id ID
     * @return 优惠券商品关联信息
     */
    CouponSpuRelation getById(Long id);

    /**
     * 保存优惠券商品关联
     * @param entity 优惠券商品关联信息
     */
    void save(CouponSpuRelation entity);

    /**
     * 更新优惠券商品关联
     * @param entity 优惠券商品关联信息
     */
    void updateById(CouponSpuRelation entity);

    /**
     * 批量删除优惠券商品关联
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
