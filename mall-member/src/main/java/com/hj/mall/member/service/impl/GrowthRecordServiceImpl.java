package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.GrowthRecord;
import com.hj.mall.member.mapper.GrowthRecordMapper;
import com.hj.mall.member.service.GrowthRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 成长值记录服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GrowthRecordServiceImpl implements GrowthRecordService {

    private final GrowthRecordMapper recordMapper;

    @Override
    public void save(GrowthRecord record) {
        log.info("[GrowthRecordService] 保存成长值记录, memberId={}", record.getMemberId());
        recordMapper.insert(record);
        log.info("[GrowthRecordService] 成长值记录保存成功, id={}", record.getId());
    }

    @Override
    public IPage<GrowthRecord> listPage(Page<GrowthRecord> page, Long memberId) {
        log.info("[GrowthRecordService] 分页查询成长值记录, page={}, memberId={}", page.getCurrent(), memberId);

        LambdaQueryWrapper<GrowthRecord> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(GrowthRecord::getMemberId, memberId);
        }
        wrapper.orderByDesc(GrowthRecord::getCreateTime);

        return recordMapper.selectPage(page, wrapper);
    }
}
