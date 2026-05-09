package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.GrowthRecord;

/**
 * 成长值记录服务接口
 */
public interface GrowthRecordService {

    /**
     * 保存成长值记录
     * @param record 成长值记录
     */
    void save(GrowthRecord record);

    /**
     * 分页查询会员成长值记录
     * @param page 分页参数
     * @param memberId 会员ID
     * @return 分页结果
     */
    IPage<GrowthRecord> listPage(Page<GrowthRecord> page, Long memberId);
}
