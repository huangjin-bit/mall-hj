package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.SeckillOrder;
import com.hj.mall.seckill.mapper.SeckillOrderMapper;
import com.hj.mall.seckill.service.SeckillOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 秒杀订单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillOrderServiceImpl implements SeckillOrderService {

    private final SeckillOrderMapper seckillOrderMapper;

    @Override
    public IPage<SeckillOrder> listPage(Page<SeckillOrder> page, Long memberId) {
        log.info("[SeckillOrderService] 分页查询秒杀订单, page={}, memberId={}", page.getCurrent(), memberId);

        LambdaQueryWrapper<SeckillOrder> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(SeckillOrder::getMemberId, memberId);
        }
        wrapper.orderByDesc(SeckillOrder::getCreateTime);

        return seckillOrderMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SeckillOrder> listByMemberId(Long memberId) {
        log.info("[SeckillOrderService] 根据会员ID查询订单, memberId={}", memberId);

        LambdaQueryWrapper<SeckillOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillOrder::getMemberId, memberId)
               .orderByDesc(SeckillOrder::getCreateTime);

        return seckillOrderMapper.selectList(wrapper);
    }

    @Override
    public SeckillOrder getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        SeckillOrder order = seckillOrderMapper.selectById(id);
        if (order == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SeckillOrder order) {
        log.info("[SeckillOrderService] 创建秒杀订单, memberId={}, skuId={}", order.getMemberId(), order.getSkuId());

        // 参数校验
        if (order.getMemberId() == null || order.getSkuId() == null) {
            throw new BizException(400, "会员ID和商品ID不能为空");
        }
        if (order.getSeckillPrice() == null || order.getSeckillPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BizException(400, "秒杀价格必须大于0");
        }
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            throw new BizException(400, "购买数量必须大于0");
        }

        order.setStatus(1);
        seckillOrderMapper.insert(order);
        log.info("[SeckillOrderService] 订单创建成功, id={}", order.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        log.info("[SeckillOrderService] 更新订单状态, id={}, status={}", id, status);

        SeckillOrder order = getById(id);
        order.setStatus(status);
        seckillOrderMapper.updateById(order);
        log.info("[SeckillOrderService] 订单状态更新成功, id={}", id);
    }
}
