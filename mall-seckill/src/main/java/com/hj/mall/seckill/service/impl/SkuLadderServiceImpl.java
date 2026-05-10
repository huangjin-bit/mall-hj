package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SkuLadder;
import com.hj.mall.seckill.mapper.SkuLadderMapper;
import com.hj.mall.seckill.service.SkuLadderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品阶梯价格服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SkuLadderServiceImpl implements SkuLadderService {

    private final SkuLadderMapper skuLadderMapper;

    @Override
    public IPage<SkuLadder> listPage(Page<SkuLadder> page) {
        log.info("[SkuLadderService] 分页查询商品阶梯价格, page={}", page.getCurrent());

        LambdaQueryWrapper<SkuLadder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SkuLadder::getCreateTime);

        return skuLadderMapper.selectPage(page, wrapper);
    }

    @Override
    public SkuLadder getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SkuLadder entity = skuLadderMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(SkuLadder entity) {
        log.info("[SkuLadderService] 保存商品阶梯价格, skuId={}", entity.getSkuId());

        // 参数校验
        if (entity.getSkuId() == null) {
            throw new BizException(400, "商品SKU ID不能为空");
        }
        if (entity.getFullCount() == null || entity.getFullCount() <= 0) {
            throw new BizException(400, "满件数必须大于0");
        }
        if (entity.getDiscount() == null || entity.getDiscount().compareTo(java.math.BigDecimal.ZERO) <= 0
            || entity.getDiscount().compareTo(new java.math.BigDecimal("100")) >= 0) {
            throw new BizException(400, "折扣必须大于0且小于100");
        }
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }

        skuLadderMapper.insert(entity);
        log.info("[SkuLadderService] 商品阶梯价格保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(SkuLadder entity) {
        log.info("[SkuLadderService] 更新商品阶梯价格, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        skuLadderMapper.updateById(entity);
        log.info("[SkuLadderService] 商品阶梯价格更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[SkuLadderService] 批量删除商品阶梯价格, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        skuLadderMapper.deleteBatchIds(ids);
        log.info("[SkuLadderService] 批量删除成功, count={}", ids.size());
    }
}
