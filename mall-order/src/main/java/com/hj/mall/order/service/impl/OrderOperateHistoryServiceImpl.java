package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.order.entity.OrderOperateHistory;
import com.hj.mall.order.mapper.OrderOperateHistoryMapper;
import com.hj.mall.order.service.OrderOperateHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOperateHistoryServiceImpl implements OrderOperateHistoryService {

    private final OrderOperateHistoryMapper historyMapper;

    @Override
    public void save(OrderOperateHistory history) {
        history.setCreateTime(LocalDateTime.now());
        historyMapper.insert(history);
        log.info("[OrderOperateHistoryService] 保存操作历史成功, orderId={}, operateType={}",
                history.getOrderId(), history.getOperateType());
    }

    @Override
    public List<OrderOperateHistory> listByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderOperateHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderOperateHistory::getOrderId, orderId)
                .orderByDesc(OrderOperateHistory::getCreateTime);
        return historyMapper.selectList(wrapper);
    }
}
