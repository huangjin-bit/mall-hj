package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberStatisticsInfo;

/**
 * 会员统计信息服务接口
 */
public interface MemberStatisticsInfoService {

    /**
     * 保存会员统计信息
     * @param statisticsInfo 统计信息
     */
    void save(MemberStatisticsInfo statisticsInfo);

    /**
     * 更新会员统计信息
     * @param statisticsInfo 统计信息
     */
    void update(MemberStatisticsInfo statisticsInfo);

    /**
     * 根据ID删除统计信息
     * @param id 统计信息ID
     */
    void delete(Long id);

    /**
     * 根据ID查询统计信息
     * @param id 统计信息ID
     * @return 统计信息
     */
    MemberStatisticsInfo getById(Long id);

    /**
     * 分页查询会员统计信息列表
     * @param page 分页参数
     * @param memberId 会员ID（可选）
     * @return 分页结果
     */
    IPage<MemberStatisticsInfo> listPage(Page<MemberStatisticsInfo> page, Long memberId);
}
