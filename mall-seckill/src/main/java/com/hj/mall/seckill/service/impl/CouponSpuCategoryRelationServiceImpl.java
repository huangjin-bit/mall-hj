package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.CouponSpuCategoryRelation;
import com.hj.mall.seckill.mapper.CouponSpuCategoryRelationMapper;
import com.hj.mall.seckill.service.CouponSpuCategoryRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券分类关联服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponSpuCategoryRelationServiceImpl implements CouponSpuCategoryRelationService {

    private final CouponSpuCategoryRelationMapper couponSpuCategoryRelationMapper;

    @Override
    public IPage<CouponSpuCategoryRelation> listPage(Page<CouponSpuCategoryRelation> page) {
        log.info("[CouponSpuCategoryRelationService] 分页查询优惠券分类关联, page={}", page.getCurrent());

        LambdaQueryWrapper<CouponSpuCategoryRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CouponSpuCategoryRelation::getCreateTime);

        return couponSpuCategoryRelationMapper.selectPage(page, wrapper);
    }

    @Override
    public CouponSpuCategoryRelation getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        CouponSpuCategoryRelation entity = couponSpuCategoryRelationMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(CouponSpuCategoryRelation entity) {
        log.info("[CouponSpuCategoryRelationService] 保存优惠券分类关联, couponId={}, categoryId={}",
                entity.getCouponId(), entity.getCategoryId());

        // 参数校验
        if (entity.getCouponId() == null) {
            throw new BizException(400, "优惠券ID不能为空");
        }
        if (entity.getCategoryId() == null) {
            throw new BizException(400, "分类ID不能为空");
        }

        couponSpuCategoryRelationMapper.insert(entity);
        log.info("[CouponSpuCategoryRelationService] 优惠券分类关联保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(CouponSpuCategoryRelation entity) {
        log.info("[CouponSpuCategoryRelationService] 更新优惠券分类关联, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        couponSpuCategoryRelationMapper.updateById(entity);
        log.info("[CouponSpuCategoryRelationService] 优惠券分类关联更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[CouponSpuCategoryRelationService] 批量删除优惠券分类关联, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        couponSpuCategoryRelationMapper.deleteBatchIds(ids);
        log.info("[CouponSpuCategoryRelationService] 批量删除成功, count={}", ids.size());
    }
}
