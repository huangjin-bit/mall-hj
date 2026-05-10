package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.MemberPrice;

import java.util.List;

/**
 * 商品会员价格服务接口
 */
public interface MemberPriceService {

    /**
     * 分页查询商品会员价格
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<MemberPrice> listPage(Page<MemberPrice> page);

    /**
     * 根据ID查询商品会员价格
     * @param id ID
     * @return 商品会员价格信息
     */
    MemberPrice getById(Long id);

    /**
     * 保存商品会员价格
     * @param entity 商品会员价格信息
     */
    void save(MemberPrice entity);

    /**
     * 更新商品会员价格
     * @param entity 商品会员价格信息
     */
    void updateById(MemberPrice entity);

    /**
     * 批量删除商品会员价格
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
