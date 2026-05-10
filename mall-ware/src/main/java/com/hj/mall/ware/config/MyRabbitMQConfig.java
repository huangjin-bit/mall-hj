package com.hj.mall.ware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 配置 — 库存模块
 *
 * 交换机与队列拓扑：
 * 1. stock-event-exchange (topic)
 *    ├── stock.delay.queue   ← 延迟队列，TTL 2min，死信转发到 stock-event-exchange
 *    │     routingKey = stock.locked
 *    └── stock.release.stock.queue  ← 真正消费解锁库存消息
 *          routingKey = stock.release.#
 *
 * 流程：
 *   下单锁库存 → 发消息到 stock.delay.queue（2min延迟）
 *   → 到期后转到 stock.release.stock.queue
 *   → 消费者检查订单状态：
 *       - 订单已取消/不存在 → 解锁库存
 *       - 订单正常 → 不处理（ack）
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
    public Exchange stockEventExchange() {
        return new TopicExchange("stock-event-exchange", true, false);
    }

    /* ==================== 延迟队列（死信队列模式） ==================== */

    /**
     * 库存锁定延迟队列：锁定库存后消息先进入此队列，TTL=2min 后转发
     * 生产环境建议设为 40-50min（比订单关单时间稍长）
     */
    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "stock-event-exchange");
        args.put("x-dead-letter-routing-key", "stock.release");
        args.put("x-message-ttl", 120000); // 2分钟（演示用，生产建议 2400000=40min）
        return new Queue("stock.delay.queue", true, false, false, args);
    }

    /**
     * 库存解锁队列：延迟到期后消息到达此队列，消费者执行解锁逻辑
     */
    @Bean
    public Queue stockReleaseStockQueue() {
        return new Queue("stock.release.stock.queue", true, false, false);
    }

    /* ==================== 绑定关系 ==================== */

    /** 锁定库存 → 延迟队列 */
    @Bean
    public Binding stockLockedBinding() {
        return new Binding("stock.delay.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.locked", null);
    }

    /** 延迟到期 → 解锁队列 */
    @Bean
    public Binding stockReleaseBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.release.#", null);
    }
}
