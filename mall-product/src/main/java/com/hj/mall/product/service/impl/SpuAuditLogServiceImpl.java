package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.SpuAuditLog;
import com.hj.mall.product.mapper.SpuAuditLogMapper;
import com.hj.mall.product.service.SpuAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpuAuditLogServiceImpl implements SpuAuditLogService {

    private final SpuAuditLogMapper spuAuditLogMapper;

    @Override
    public IPage<SpuAuditLog> listPage(Page<SpuAuditLog> page, Long spuId) {
        LambdaQueryWrapper<SpuAuditLog> wrapper = new LambdaQueryWrapper<>();
        if (spuId != null) {
            wrapper.eq(SpuAuditLog::getSpuId, spuId);
        }
        wrapper.orderByDesc(SpuAuditLog::getCreateTime);
        return spuAuditLogMapper.selectPage(page, wrapper);
    }
}
