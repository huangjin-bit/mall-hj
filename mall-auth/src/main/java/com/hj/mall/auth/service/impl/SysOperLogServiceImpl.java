package com.hj.mall.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.mall.auth.entity.SysOperLog;
import com.hj.mall.auth.mapper.SysOperLogMapper;
import com.hj.mall.auth.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 系统操作日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    private final SysOperLogMapper sysOperLogMapper;

    @Override
    public IPage<SysOperLog> listPage(long current, long size, String title, Integer operType) {
        log.debug("[SysOperLogServiceImpl] 分页查询操作日志, current={}, size={}", current, size);

        Page<SysOperLog> page = new Page<>(current, size);
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(title)) {
            wrapper.like(SysOperLog::getTitle, title);
        }
        if (operType != null) {
            wrapper.eq(SysOperLog::getOperType, operType);
        }

        wrapper.orderByDesc(SysOperLog::getCreateTime);

        return sysOperLogMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean save(SysOperLog operLog) {
        return sysOperLogMapper.insert(operLog) > 0;
    }
}
