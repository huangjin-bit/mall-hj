package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.MemberPrice;
import com.hj.mall.seckill.mapper.MemberPriceMapper;
import com.hj.mall.seckill.service.MemberPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品会员价格服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPriceServiceImpl implements MemberPriceService {

    private final MemberPriceMapper memberPriceMapper;

    @Override
    public IPage<MemberPrice> listPage(Page<MemberPrice> page) {
        log.info("[MemberPriceService] 分页查询商品会员价格, page={}", page.getCurrent());

        LambdaQueryWrapper<MemberPrice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(MemberPrice::getCreateTime);

        return memberPriceMapper.selectPage(page, wrapper);
    }

    @Override
    public MemberPrice getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        MemberPrice entity = memberPriceMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(MemberPrice entity) {
        log.info("[MemberPriceService] 保存商品会员价格, skuId={}, memberLevelId={}",
                entity.getSkuId(), entity.getMemberLevelId());

        // 参数校验
        if (entity.getSkuId() == null) {
            throw new BizException(400, "商品SKU ID不能为空");
        }
        if (entity.getMemberLevelId() == null) {
            throw new BizException(400, "会员等级ID不能为空");
        }
        if (entity.getMemberPrice() == null) {
            throw new BizException(400, "会员价格不能为空");
        }
        if (entity.getAddOther() == null) {
            entity.setAddOther(0);
        }

        memberPriceMapper.insert(entity);
        log.info("[MemberPriceService] 商品会员价格保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(MemberPrice entity) {
        log.info("[MemberPriceService] 更新商品会员价格, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        memberPriceMapper.updateById(entity);
        log.info("[MemberPriceService] 商品会员价格更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[MemberPriceService] 批量删除商品会员价格, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        memberPriceMapper.deleteBatchIds(ids);
        log.info("[MemberPriceService] 批量删除成功, count={}", ids.size());
    }
}
