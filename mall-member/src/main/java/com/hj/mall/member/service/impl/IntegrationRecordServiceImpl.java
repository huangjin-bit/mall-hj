package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.IntegrationRecord;
import com.hj.mall.member.mapper.IntegrationRecordMapper;
import com.hj.mall.member.service.IntegrationRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 积分记录服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationRecordServiceImpl implements IntegrationRecordService {

    private final IntegrationRecordMapper recordMapper;

    @Override
    public void save(IntegrationRecord record) {
        log.info("[IntegrationRecordService] 保存积分记录, memberId={}", record.getMemberId());
        recordMapper.insert(record);
        log.info("[IntegrationRecordService] 积分记录保存成功, id={}", record.getId());
    }

    @Override
    public IPage<IntegrationRecord> listPage(Page<IntegrationRecord> page, Long memberId) {
        log.info("[IntegrationRecordService] 分页查询积分记录, page={}, memberId={}", page.getCurrent(), memberId);

        LambdaQueryWrapper<IntegrationRecord> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(IntegrationRecord::getMemberId, memberId);
        }
        wrapper.orderByDesc(IntegrationRecord::getCreateTime);

        return recordMapper.selectPage(page, wrapper);
    }
}
