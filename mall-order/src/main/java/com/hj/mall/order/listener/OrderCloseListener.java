package com.hj.mall.order.listener;

import com.hj.mall.order.dto.OrderTo;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 订单关单监听器
 *
 * 监听 order.release.order.queue 队列
 * 延迟队列到期后，检查订单是否已支付，未支付则关闭订单
 */
@Slf4j
@Component
@RabbitListener(queues = "order.release.order.queue")
@RequiredArgsConstructor
public class OrderCloseListener {

    private final OrderService orderService;

    @RabbitHandler
    public void handleOrderClose(OrderTo orderTo, Channel channel, Message message) throws IOException {
        log.info("[OrderCloseListener] 收到关单消息，orderSn={}", orderTo.getOrderSn());
        try {
            Order order = new Order();
            order.setId(orderTo.getId());
            orderService.closeOrder(order);
            // 手动ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("[OrderCloseListener] 关单失败，orderSn={}", orderTo.getOrderSn(), e);
            // 拒绝消息，重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
