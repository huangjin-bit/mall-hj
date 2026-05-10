package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SeckillSkuNotice;
import com.hj.mall.seckill.mapper.SeckillSkuNoticeMapper;
import com.hj.mall.seckill.service.SeckillSkuNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 秒杀商品提醒服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillSkuNoticeServiceImpl implements SeckillSkuNoticeService {

    private final SeckillSkuNoticeMapper seckillSkuNoticeMapper;

    @Override
    public IPage<SeckillSkuNotice> listPage(Page<SeckillSkuNotice> page) {
        log.info("[SeckillSkuNoticeService] 分页查询秒杀商品提醒, page={}", page.getCurrent());

        LambdaQueryWrapper<SeckillSkuNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SeckillSkuNotice::getCreateTime);

        return seckillSkuNoticeMapper.selectPage(page, wrapper);
    }

    @Override
    public SeckillSkuNotice getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SeckillSkuNotice entity = seckillSkuNoticeMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(SeckillSkuNotice entity) {
        log.info("[SeckillSkuNoticeService] 保存秒杀商品提醒, memberId={}, skuId={}",
                entity.getMemberId(), entity.getSkuId());

        // 参数校验
        if (entity.getMemberId() == null) {
            throw new BizException(400, "会员ID不能为空");
        }
        if (entity.getSkuId() == null) {
            throw new BizException(400, "商品SKU ID不能为空");
        }
        if (entity.getSessionId() == null) {
            throw new BizException(400, "秒杀场次ID不能为空");
        }
        if (entity.getNoticeType() == null) {
            entity.setNoticeType(1); // 默认站内信
        }

        seckillSkuNoticeMapper.insert(entity);
        log.info("[SeckillSkuNoticeService] 秒杀商品提醒保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(SeckillSkuNotice entity) {
        log.info("[SeckillSkuNoticeService] 更新秒杀商品提醒, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        seckillSkuNoticeMapper.updateById(entity);
        log.info("[SeckillSkuNoticeService] 秒杀商品提醒更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[SeckillSkuNoticeService] 批量删除秒杀商品提醒, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        seckillSkuNoticeMapper.deleteBatchIds(ids);
        log.info("[SeckillSkuNoticeService] 批量删除成功, count={}", ids.size());
    }
}
