package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberLoginLog;
import com.hj.mall.member.mapper.MemberLoginLogMapper;
import com.hj.mall.member.service.MemberLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 会员登录日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLoginLogServiceImpl implements MemberLoginLogService {

    private final MemberLoginLogMapper loginLogMapper;

    @Override
    public void save(MemberLoginLog loginLog) {
        log.info("[MemberLoginLogService] 保存登录日志, memberId={}", loginLog.getMemberId());
        loginLogMapper.insert(loginLog);
        log.info("[MemberLoginLogService] 登录日志保存成功, id={}", loginLog.getId());
    }

    @Override
    public IPage<MemberLoginLog> listPage(Page<MemberLoginLog> page, Long memberId) {
        log.info("[MemberLoginLogService] 分页查询登录日志, page={}, memberId={}", page.getCurrent(), memberId);

        LambdaQueryWrapper<MemberLoginLog> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(MemberLoginLog::getMemberId, memberId);
        }
        wrapper.orderByDesc(MemberLoginLog::getCreateTime);

        return loginLogMapper.selectPage(page, wrapper);
    }
}
