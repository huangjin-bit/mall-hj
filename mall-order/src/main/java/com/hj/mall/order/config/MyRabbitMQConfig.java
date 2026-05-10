package com.hj.mall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 配置 — 订单模块
 *
 * 交换机与队列拓扑：
 * 1. order-event-exchange (topic)
 *    ├── order.delay.queue   ← 延迟队列，TTL 30min，死信转发到 order-event-exchange
 *    │     routingKey = order.create.order
 *    └── order.release.order.queue  ← 真正消费关单消息
 *          routingKey = order.release.order
 *
 * 2. 库存解锁：order 关单后通知 ware 解锁库存
 *    order-event-exchange → stock.release.stock.queue (routingKey = order.release.other.#)
 */
@Configuration
public class MyRabbitMQConfig {

    /** JSON 序列化 */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /* ==================== 交换机 ==================== */

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    /* ==================== 延迟队列（死信队列模式） ==================== */

    /**
     * 延迟队列：订单创建后消息先进入此队列，TTL=30min 后转发到死信交换机
     */
    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "order-event-exchange");
        args.put("x-dead-letter-routing-key", "order.release.order");
        args.put("x-message-ttl", 1800000); // 30分钟
        return new Queue("order.delay.queue", true, false, false, args);
    }

    /**
     * 关单队列：延迟到期后消息到达此队列，消费者执行关单逻辑
     */
    @Bean
    public Queue orderReleaseOrderQueue() {
        return new Queue("order.release.order.queue", true, false, false);
    }

    /* ==================== 绑定关系 ==================== */

    /** 创建订单 → 延迟队列 */
    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.create.order", null);
    }

    /** 延迟到期 → 关单队列 */
    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.order", null);
    }

    /**
     * 订单关闭后通知库存服务解锁库存
     * order-event-exchange → stock.release.stock.queue
     */
    @Bean
    public Binding orderReleaseOtherBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.other.#", null);
    }
}
