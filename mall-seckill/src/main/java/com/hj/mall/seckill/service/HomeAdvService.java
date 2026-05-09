package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.HomeAdv;

import java.util.List;

/**
 * 首页广告服务接口
 */
public interface HomeAdvService {

    /**
     * 分页查询广告
     * @param page 分页参数
     * @param name 广告名称（可选）
     * @param type 类型（可选）
     * @return 分页结果
     */
    IPage<HomeAdv> listPage(Page<HomeAdv> page, String name, Integer type);

    /**
     * 查询当前启用的广告列表
     * @param type 类型（可选）
     * @return 广告列表
     */
    List<HomeAdv> listActiveAds(Integer type);

    /**
     * 根据ID查询广告
     * @param id 广告ID
     * @return 广告信息
     */
    HomeAdv getById(Long id);

    /**
     * 保存广告
     * @param adv 广告信息
     */
    void save(HomeAdv adv);

    /**
     * 更新广告
     * @param adv 广告信息
     */
    void updateById(HomeAdv adv);

    /**
     * 批量删除广告
     * @param ids 广告ID列表
     */
    void removeBatch(List<Long> ids);
}
