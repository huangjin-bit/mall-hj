package com.hj.mall.ware.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.WareOrderTaskDetail;
import com.hj.mall.ware.entity.WareOrderTask;
import com.hj.mall.ware.feign.OrderFeignClient;
import com.hj.mall.ware.service.WareOrderTaskService;
import com.hj.mall.ware.service.WareSkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 库存解锁监听器
 *
 * 监听 stock.release.stock.queue 队列，处理两种消息：
 * 1. WareOrderTaskDetail — 延迟队列到期，检查订单状态决定是否解锁
 * 2. OrderTo — 订单关闭后主动通知解锁该订单所有锁定库存
 */
@Slf4j
@Component
@RabbitListener(queues = "stock.release.stock.queue")
@RequiredArgsConstructor
public class StockReleaseListener {

    private final WareSkuService wareSkuService;
    private final WareOrderTaskService wareOrderTaskService;
    private final OrderFeignClient orderFeignClient;
    private final ObjectMapper objectMapper;

    /**
     * 处理延迟队列到期的库存锁定明细
     * 检查对应订单是否已取消/不存在，是则解锁
     */
    @RabbitHandler
    public void handleStockLockedRelease(WareOrderTaskDetail detail, Message message, com.rabbitmq.client.Channel channel) throws IOException {
        log.info("收到库存延迟解锁消息，taskDetailId={}", detail.getId());
        try {
            WareOrderTask task = wareOrderTaskService.getById(detail.getTaskId());
            if (task == null) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            Result<Object> result = orderFeignClient.getOrderStatus(task.getOrderSn());
            if (result.getCode() == 200) {
                Object orderData = result.getData();
                boolean shouldRelease = false;
                if (orderData == null) {
                    shouldRelease = true;
                } else {
                    Map<String, Object> orderMap = objectMapper.convertValue(orderData, Map.class);
                    Integer status = (Integer) orderMap.get("status");
                    if (status != null && status == 4) {
                        shouldRelease = true;
                    }
                }
                if (shouldRelease) {
                    wareSkuService.releaseStock(task.getId());
                }
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("库存解锁失败，taskDetailId={}", detail.getId(), e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    /**
     * 处理订单关闭后主动通知的解锁消息
     * 解锁该订单所有已锁定的库存
     */
    @RabbitHandler
    public void handleOrderCloseRelease(Map<String, Object> orderTo, Message message, com.rabbitmq.client.Channel channel) throws IOException {
        String orderSn = (String) orderTo.get("orderSn");
        log.info("收到订单关闭解锁消息，orderSn={}", orderSn);
        try {
            WareOrderTask task = wareOrderTaskService.getByOrderSn(orderSn);
            if (task != null) {
                wareSkuService.releaseStock(task.getId());
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("订单关闭解锁库存失败，orderSn={}", orderSn, e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
