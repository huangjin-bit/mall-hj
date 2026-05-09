package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SeckillSkuRelation;
import com.hj.mall.seckill.mapper.SeckillSkuRelationMapper;
import com.hj.mall.seckill.service.SeckillSkuRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 秒杀商品关联服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillSkuRelationServiceImpl implements SeckillSkuRelationService {

    private final SeckillSkuRelationMapper seckillSkuRelationMapper;

    @Override
    public IPage<SeckillSkuRelation> listPage(Page<SeckillSkuRelation> page, Long sessionId) {
        log.info("[SeckillSkuRelationService] 分页查询秒杀商品, page={}, sessionId={}", page.getCurrent(), sessionId);

        LambdaQueryWrapper<SeckillSkuRelation> wrapper = new LambdaQueryWrapper<>();
        if (sessionId != null) {
            wrapper.eq(SeckillSkuRelation::getSessionId, sessionId);
        }
        wrapper.orderByAsc(SeckillSkuRelation::getSort);

        return seckillSkuRelationMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SeckillSkuRelation> listBySessionId(Long sessionId) {
        log.info("[SeckillSkuRelationService] 根据场次ID查询商品, sessionId={}", sessionId);

        LambdaQueryWrapper<SeckillSkuRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillSkuRelation::getSessionId, sessionId)
               .eq(SeckillSkuRelation::getStatus, 1)
               .orderByAsc(SeckillSkuRelation::getSort);

        return seckillSkuRelationMapper.selectList(wrapper);
    }

    @Override
    public SeckillSkuRelation getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SeckillSkuRelation relation = seckillSkuRelationMapper.selectById(id);
        if (relation == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return relation;
    }

    @Override
    public void save(SeckillSkuRelation relation) {
        log.info("[SeckillSkuRelationService] 保存秒杀商品, skuId={}", relation.getSkuId());

        // 参数校验
        if (relation.getSeckillPrice() == null || relation.getSeckillPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BizException(400, "秒杀价格必须大于0");
        }
        if (relation.getSeckillStock() == null || relation.getSeckillStock() <= 0) {
            throw new BizException(400, "秒杀库存必须大于0");
        }
        if (relation.getSeckillLimit() == null || relation.getSeckillLimit() <= 0) {
            throw new BizException(400, "限购数量必须大于0");
        }

        seckillSkuRelationMapper.insert(relation);
        log.info("[SeckillSkuRelationService] 商品保存成功, id={}", relation.getId());
    }

    @Override
    public void updateById(SeckillSkuRelation relation) {
        log.info("[SeckillSkuRelationService] 更新秒杀商品, id={}", relation.getId());

        SeckillSkuRelation existRelation = getById(relation.getId());
        if (existRelation == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        seckillSkuRelationMapper.updateById(relation);
        log.info("[SeckillSkuRelationService] 商品更新成功, id={}", relation.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[SeckillSkuRelationService] 批量删除秒杀商品, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        seckillSkuRelationMapper.deleteBatchIds(ids);
        log.info("[SeckillSkuRelationService] 批量删除成功, count={}", ids.size());
    }
}
