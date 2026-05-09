package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.entity.OrderOperateHistory;
import com.hj.mall.order.mapper.OrderMapper;
import com.hj.mall.order.service.OrderItemService;
import com.hj.mall.order.service.OrderOperateHistoryService;
import com.hj.mall.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final OrderOperateHistoryService historyService;

    @Override
    public IPage<Order> listPage(Page<Order> page, String key, Integer status, Long memberId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(key)) {
            wrapper.like(Order::getOrderSn, key)
                    .or()
                    .like(Order::getReceiverName, key)
                    .or()
                    .like(Order::getReceiverPhone, key);
        }

        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }

        if (memberId != null) {
            wrapper.eq(Order::getMemberId, memberId);
        }

        wrapper.eq(Order::getDeleteStatus, 0)
                .orderByDesc(Order::getCreateTime);

        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public Order getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BizException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public Order getByOrderSn(String orderSn) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderSn, orderSn);
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public List<OrderItem> listOrderItems(Long orderId) {
        return orderItemService.listByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Order order) {
        orderMapper.insert(order);

        // 记录操作历史
        OrderOperateHistory history = new OrderOperateHistory();
        history.setOrderId(order.getId());
        history.setOrderSn(order.getOrderSn());
        history.setOperateType("创建订单");
        history.setNote("订单创建成功");
        history.setOperateBy(order.getMemberId());
        historyService.save(history);

        log.info("[OrderService] 订单创建成功, orderSn={}", order.getOrderSn());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long orderId, Integer status, String note, Long operateBy) {
        Order order = getById(orderId);
        order.setStatus(status);
        orderMapper.updateById(order);

        // 记录操作历史
        OrderOperateHistory history = new OrderOperateHistory();
        history.setOrderId(orderId);
        history.setOrderSn(order.getOrderSn());
        history.setOperateType("状态变更");
        history.setNote(note);
        history.setOperateBy(operateBy);
        historyService.save(history);

        log.info("[OrderService] 订单状态更新成功, orderId={}, status={}", orderId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        // 逻辑删除
        for (Long id : ids) {
            Order order = new Order();
            order.setId(id);
            order.setDeleteStatus(1);
            orderMapper.updateById(order);
        }
        log.info("[OrderService] 订单批量删除成功, ids={}", ids);
    }
}
