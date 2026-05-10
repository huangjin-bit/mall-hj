package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SeckillPromotion;
import com.hj.mall.seckill.mapper.SeckillPromotionMapper;
import com.hj.mall.seckill.service.SeckillPromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 秒杀活动服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillPromotionServiceImpl implements SeckillPromotionService {

    private final SeckillPromotionMapper seckillPromotionMapper;

    @Override
    public IPage<SeckillPromotion> listPage(Page<SeckillPromotion> page, String title, Integer status) {
        log.info("[SeckillPromotionService] 分页查询秒杀活动, page={}, title={}, status={}",
                page.getCurrent(), title, status);

        LambdaQueryWrapper<SeckillPromotion> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(title)) {
            wrapper.like(SeckillPromotion::getTitle, title);
        }
        if (status != null) {
            wrapper.eq(SeckillPromotion::getStatus, status);
        }
        wrapper.orderByDesc(SeckillPromotion::getCreateTime);

        return seckillPromotionMapper.selectPage(page, wrapper);
    }

    @Override
    public SeckillPromotion getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SeckillPromotion entity = seckillPromotionMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(SeckillPromotion entity) {
        log.info("[SeckillPromotionService] 保存秒杀活动, title={}", entity.getTitle());

        // 参数校验
        if (StringUtils.isBlank(entity.getTitle())) {
            throw new BizException(400, "活动标题不能为空");
        }
        if (entity.getStartTime() == null) {
            throw new BizException(400, "开始时间不能为空");
        }
        if (entity.getEndTime() == null) {
            throw new BizException(400, "结束时间不能为空");
        }
        if (entity.getStartTime().isAfter(entity.getEndTime())) {
            throw new BizException(400, "开始时间不能晚于结束时间");
        }
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }

        seckillPromotionMapper.insert(entity);
        log.info("[SeckillPromotionService] 秒杀活动保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(SeckillPromotion entity) {
        log.info("[SeckillPromotionService] 更新秒杀活动, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        seckillPromotionMapper.updateById(entity);
        log.info("[SeckillPromotionService] 秒杀活动更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[SeckillPromotionService] 批量删除秒杀活动, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        seckillPromotionMapper.deleteBatchIds(ids);
        log.info("[SeckillPromotionService] 批量删除成功, count={}", ids.size());
    }
}
