package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SkuLadder;

import java.util.List;

/**
 * 商品阶梯价格服务接口
 */
public interface SkuLadderService {

    /**
     * 分页查询商品阶梯价格
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SkuLadder> listPage(Page<SkuLadder> page);

    /**
     * 根据ID查询商品阶梯价格
     * @param id ID
     * @return 商品阶梯价格信息
     */
    SkuLadder getById(Long id);

    /**
     * 保存商品阶梯价格
     * @param entity 商品阶梯价格信息
     */
    void save(SkuLadder entity);

    /**
     * 更新商品阶梯价格
     * @param entity 商品阶梯价格信息
     */
    void updateById(SkuLadder entity);

    /**
     * 批量删除商品阶梯价格
     * @param ids ID列表
     */
    void removeBatch(List<Long> ids);
}
