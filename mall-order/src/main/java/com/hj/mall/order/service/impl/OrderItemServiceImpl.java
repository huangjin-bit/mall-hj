package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.mapper.OrderItemMapper;
import com.hj.mall.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItem> listByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        return orderItemMapper.selectList(wrapper);
    }

    @Override
    public void saveBatch(List<OrderItem> items) {
        for (OrderItem item : items) {
            orderItemMapper.insert(item);
        }
        log.info("[OrderItemService] 批量保存订单项成功, count={}", items.size());
    }
}
