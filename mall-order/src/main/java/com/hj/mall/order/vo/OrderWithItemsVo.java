package com.hj.mall.order.vo;

import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 订单及订单项VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderWithItemsVo extends Order {

    private List<OrderItem> orderItems;

    private Integer totalQuantity;
}
