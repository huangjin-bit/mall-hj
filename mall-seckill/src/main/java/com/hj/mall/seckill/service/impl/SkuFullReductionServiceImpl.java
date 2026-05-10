package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SkuFullReduction;
import com.hj.mall.seckill.mapper.SkuFullReductionMapper;
import com.hj.mall.seckill.service.SkuFullReductionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品满减信息服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SkuFullReductionServiceImpl implements SkuFullReductionService {

    private final SkuFullReductionMapper skuFullReductionMapper;

    @Override
    public IPage<SkuFullReduction> listPage(Page<SkuFullReduction> page) {
        log.info("[SkuFullReductionService] 分页查询商品满减信息, page={}", page.getCurrent());

        LambdaQueryWrapper<SkuFullReduction> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SkuFullReduction::getCreateTime);

        return skuFullReductionMapper.selectPage(page, wrapper);
    }

    @Override
    public SkuFullReduction getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SkuFullReduction entity = skuFullReductionMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(SkuFullReduction entity) {
        log.info("[SkuFullReductionService] 保存商品满减信息, skuId={}", entity.getSkuId());

        // 参数校验
        if (entity.getSkuId() == null) {
            throw new BizException(400, "商品SKU ID不能为空");
        }
        if (entity.getFullPrice() == null || entity.getFullPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BizException(400, "满减金额必须大于0");
        }
        if (entity.getReducePrice() == null || entity.getReducePrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BizException(400, "优惠金额必须大于0");
        }
        if (entity.getReducePrice().compareTo(entity.getFullPrice()) >= 0) {
            throw new BizException(400, "优惠金额必须小于满减金额");
        }
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }

        skuFullReductionMapper.insert(entity);
        log.info("[SkuFullReductionService] 商品满减信息保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(SkuFullReduction entity) {
        log.info("[SkuFullReductionService] 更新商品满减信息, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        skuFullReductionMapper.updateById(entity);
        log.info("[SkuFullReductionService] 商品满减信息更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[SkuFullReductionService] 批量删除商品满减信息, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        skuFullReductionMapper.deleteBatchIds(ids);
        log.info("[SkuFullReductionService] 批量删除成功, count={}", ids.size());
    }
}
