package com.hj.mall.order.service;

import com.hj.mall.order.entity.OrderOperateHistory;

import java.util.List;

public interface OrderOperateHistoryService {

    void save(OrderOperateHistory history);

    List<OrderOperateHistory> listByOrderId(Long orderId);
}
