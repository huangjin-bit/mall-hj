package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SeckillSession;
import com.hj.mall.seckill.mapper.SeckillSessionMapper;
import com.hj.mall.seckill.service.SeckillSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 秒杀场次服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillSessionServiceImpl implements SeckillSessionService {

    private final SeckillSessionMapper seckillSessionMapper;

    @Override
    public IPage<SeckillSession> listPage(Page<SeckillSession> page, String name) {
        log.info("[SeckillSessionService] 分页查询秒杀场次, page={}, name={}", page.getCurrent(), name);

        LambdaQueryWrapper<SeckillSession> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(SeckillSession::getName, name);
        }
        wrapper.orderByDesc(SeckillSession::getCreateTime);

        return seckillSessionMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SeckillSession> listActiveSessions() {
        log.info("[SeckillSessionService] 查询当前启用的秒杀场次");

        LambdaQueryWrapper<SeckillSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillSession::getStatus, 1)
               .le(SeckillSession::getStartTime, LocalDateTime.now())
               .ge(SeckillSession::getEndTime, LocalDateTime.now())
               .orderByAsc(SeckillSession::getStartTime);

        return seckillSessionMapper.selectList(wrapper);
    }

    @Override
    public SeckillSession getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SeckillSession session = seckillSessionMapper.selectById(id);
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return session;
    }

    @Override
    public void save(SeckillSession session) {
        log.info("[SeckillSessionService] 保存秒杀场次, name={}", session.getName());

        // 时间校验
        if (session.getStartTime().isAfter(session.getEndTime())) {
            throw new BizException(400, "开始时间不能晚于结束时间");
        }

        seckillSessionMapper.insert(session);
        log.info("[SeckillSessionService] 场次保存成功, id={}", session.getId());
    }

    @Override
    public void updateById(SeckillSession session) {
        log.info("[SeckillSessionService] 更新秒杀场次, id={}", session.getId());

        SeckillSession existSession = getById(session.getId());
        if (existSession == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        // 时间校验
        if (session.getStartTime().isAfter(session.getEndTime())) {
            throw new BizException(400, "开始时间不能晚于结束时间");
        }

        seckillSessionMapper.updateById(session);
        log.info("[SeckillSessionService] 场次更新成功, id={}", session.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[SeckillSessionService] 批量删除秒杀场次, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        seckillSessionMapper.deleteBatchIds(ids);
        log.info("[SeckillSessionService] 批量删除成功, count={}", ids.size());
    }
}
