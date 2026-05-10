package com.hj.mall.ware.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.mall.common.result.Result;
import com.hj.mall.ware.entity.WareOrderTaskDetail;
import com.hj.mall.ware.entity.WareOrderTask;
import com.hj.mall.ware.feign.OrderFeignClient;
import com.hj.mall.ware.service.WareOrderTaskDetailService;
import com.hj.mall.ware.service.WareOrderTaskService;
import com.hj.mall.ware.service.WareSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
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
    private final WareOrderTaskDetailService wareOrderTaskDetailService;
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
            // 查询工作单，获取订单号
            WareOrderTask task = wareOrderTaskService.getById(detail.getTaskId());
            if (task == null) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            // 远程查询订单状态
            Result<Object> result = orderFeignClient.getOrderStatus(task.getOrderSn());
            if (result.getCode() == 200) {
                Object orderData = result.getData();
                if (orderData == null) {
                    // 订单不存在，解锁库存
                    wareSkuService.unlockStock(detail.getId());
                } else {
                    // 订单存在，检查状态
                    Map<String, Object> orderMap = objectMapper.convertValue(orderData, Map.class);
                    Integer status = (Integer) orderMap.get("status");
                    if (status != null && status == 4) {
                        // 订单已关闭，解锁库存
                        wareSkuService.unlockStock(detail.getId());
                    }
                    // 其他状态（待付款/已付款等）不解锁
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
            // 根据订单号找到工作单
            WareOrderTask task = wareOrderTaskService.getOne(
                    new LambdaQueryWrapper<WareOrderTask>().eq(WareOrderTask::getOrderSn, orderSn));
            if (task != null) {
                // 找到该工作单下所有已锁定的明细
                List<WareOrderTaskDetail> details = wareOrderTaskDetailService.list(
                        new LambdaQueryWrapper<WareOrderTaskDetail>()
                                .eq(WareOrderTaskDetail::getTaskId, task.getId())
                                .eq(WareOrderTaskDetail::getLockStatus, 1));
                for (WareOrderTaskDetail detail : details) {
                    wareSkuService.unlockStock(detail.getId());
                }
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("订单关闭解锁库存失败，orderSn={}", orderSn, e);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
