package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.IntegrationRecord;

/**
 * 积分记录服务接口
 */
public interface IntegrationRecordService {

    /**
     * 保存积分记录
     * @param record 积分记录
     */
    void save(IntegrationRecord record);

    /**
     * 分页查询会员积分记录
     * @param page 分页参数
     * @param memberId 会员ID
     * @return 分页结果
     */
    IPage<IntegrationRecord> listPage(Page<IntegrationRecord> page, Long memberId);
}
