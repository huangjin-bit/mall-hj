package com.hj.mall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;

import java.util.List;

public interface OrderService {

    IPage<Order> listPage(Page<Order> page, String key, Integer status, Long memberId);

    Order getById(Long id);

    Order getByOrderSn(String orderSn);

    List<OrderItem> listOrderItems(Long orderId);

    void save(Order order);

    void updateStatus(Long orderId, Integer status, String note, Long operateBy);

    void removeBatch(List<Long> ids);
}
