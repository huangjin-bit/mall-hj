package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.CouponSpuCategoryRelation;

import java.util.List;

/**
 * 优惠券分类关联服务接口
 */
public interface CouponSpuCategoryRelationService {

    /**
     * 分页查询优惠券分类关联
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<CouponSpuCategoryRelation> listPage(Page<CouponSpuCategoryRelation> page);

    /**
     * 根据ID查询优惠券分类关联
     * @param id ID
     * @return 优惠券分类关联信息
     */
    CouponSpuCategoryRelation getById(Long id);

    /**
     * 保存优惠券分类关联
     * @param entity 优惠券分类关联信息
     */
    void save(CouponSpuCategoryRelation entity);

    /**
     * 更新优惠券分类关联
     * @param entity 优惠券分类关联信息
     */
    void updateById(CouponSpuCategoryRelation entity);

    /**
     * 批量删除优惠券分类关联
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
