package com.hj.mall.order.service;

import com.hj.mall.order.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> listByOrderId(Long orderId);

    void saveBatch(List<OrderItem> items);
}
