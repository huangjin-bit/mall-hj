package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.order.entity.OrderReturnReason;
import com.hj.mall.order.mapper.OrderReturnReasonMapper;
import com.hj.mall.order.service.OrderReturnReasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderReturnReasonServiceImpl implements OrderReturnReasonService {

    private final OrderReturnReasonMapper returnReasonMapper;

    @Override
    public IPage<OrderReturnReason> listPage(Page<OrderReturnReason> page) {
        LambdaQueryWrapper<OrderReturnReason> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(OrderReturnReason::getSort);
        return returnReasonMapper.selectPage(page, wrapper);
    }

    @Override
    public OrderReturnReason getById(Long id) {
        OrderReturnReason reason = returnReasonMapper.selectById(id);
        if (reason == null) {
            throw new BizException(ResultCode.PARAM_ERROR);
        }
        return reason;
    }

    @Override
    public void save(OrderReturnReason reason) {
        reason.setCreateTime(LocalDateTime.now());
        reason.setStatus(1); // 默认启用
        returnReasonMapper.insert(reason);
        log.info("[OrderReturnReasonService] 退货原因创建成功, name={}", reason.getName());
    }

    @Override
    public void updateById(OrderReturnReason reason) {
        reason.setUpdateTime(LocalDateTime.now());
        returnReasonMapper.updateById(reason);
        log.info("[OrderReturnReasonService] 退货原因更新成功, id={}", reason.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        returnReasonMapper.deleteBatchIds(ids);
        log.info("[OrderReturnReasonService] 退货原因批量删除成功, ids={}", ids);
    }
}
