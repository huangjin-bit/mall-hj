package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SpuBounds;
import com.hj.mall.seckill.mapper.SpuBoundsMapper;
import com.hj.mall.seckill.service.SpuBoundsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SPU积分成长服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpuBoundsServiceImpl implements SpuBoundsService {

    private final SpuBoundsMapper spuBoundsMapper;

    @Override
    public IPage<SpuBounds> listPage(Page<SpuBounds> page) {
        log.info("[SpuBoundsService] 分页查询SPU积分成长, page={}", page.getCurrent());

        LambdaQueryWrapper<SpuBounds> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SpuBounds::getCreateTime);

        return spuBoundsMapper.selectPage(page, wrapper);
    }

    @Override
    public SpuBounds getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SpuBounds entity = spuBoundsMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(SpuBounds entity) {
        log.info("[SpuBoundsService] 保存SPU积分成长, spuId={}", entity.getSpuId());

        // 参数校验
        if (entity.getSpuId() == null) {
            throw new BizException(400, "商品SPU ID不能为空");
        }
        if (entity.getBuyBounds() == null || entity.getBuyBounds().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new BizException(400, "购买积分不能为负数");
        }
        if (entity.getGrowBounds() == null || entity.getGrowBounds().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new BizException(400, "成长值不能为负数");
        }
        if (entity.getWork() == null) {
            entity.setWork(1);
        }

        spuBoundsMapper.insert(entity);
        log.info("[SpuBoundsService] SPU积分成长保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(SpuBounds entity) {
        log.info("[SpuBoundsService] 更新SPU积分成长, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        spuBoundsMapper.updateById(entity);
        log.info("[SpuBoundsService] SPU积分成长更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[SpuBoundsService] 批量删除SPU积分成长, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        spuBoundsMapper.deleteBatchIds(ids);
        log.info("[SpuBoundsService] 批量删除成功, count={}", ids.size());
    }
}
