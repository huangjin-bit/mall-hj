package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.CouponSpuRelation;
import com.hj.mall.seckill.mapper.CouponSpuRelationMapper;
import com.hj.mall.seckill.service.CouponSpuRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券商品关联服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponSpuRelationServiceImpl implements CouponSpuRelationService {

    private final CouponSpuRelationMapper couponSpuRelationMapper;

    @Override
    public IPage<CouponSpuRelation> listPage(Page<CouponSpuRelation> page) {
        log.info("[CouponSpuRelationService] 分页查询优惠券商品关联, page={}", page.getCurrent());

        LambdaQueryWrapper<CouponSpuRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CouponSpuRelation::getCreateTime);

        return couponSpuRelationMapper.selectPage(page, wrapper);
    }

    @Override
    public CouponSpuRelation getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        CouponSpuRelation entity = couponSpuRelationMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(CouponSpuRelation entity) {
        log.info("[CouponSpuRelationService] 保存优惠券商品关联, couponId={}, spuId={}",
                entity.getCouponId(), entity.getSpuId());

        // 参数校验
        if (entity.getCouponId() == null) {
            throw new BizException(400, "优惠券ID不能为空");
        }
        if (entity.getSpuId() == null) {
            throw new BizException(400, "商品SPU ID不能为空");
        }

        couponSpuRelationMapper.insert(entity);
        log.info("[CouponSpuRelationService] 优惠券商品关联保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(CouponSpuRelation entity) {
        log.info("[CouponSpuRelationService] 更新优惠券商品关联, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        couponSpuRelationMapper.updateById(entity);
        log.info("[CouponSpuRelationService] 优惠券商品关联更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[CouponSpuRelationService] 批量删除优惠券商品关联, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        couponSpuRelationMapper.deleteBatchIds(ids);
        log.info("[CouponSpuRelationService] 批量删除成功, count={}", ids.size());
    }
}
