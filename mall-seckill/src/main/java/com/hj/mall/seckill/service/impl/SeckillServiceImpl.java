package com.hj.mall.seckill.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.mall.seckill.service.SeckillService;
import com.hj.mall.seckill.vo.SeckillSkuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 秒杀服务实现
 *
 * Redis数据结构：
 * 1. seckill:skus  — Hash，key=sessionId_skuId，value=SeckillSkuVO JSON
 * 2. seckill:stock:{randomCode} — Semaphore，信号量=秒杀库存数
 * 3. seckill:member:{memberId}_{sessionId}_{skuId} — 已购买标记（防止重复购买）
 *
 * 流程：
 * 1. 定时任务预热：将秒杀商品信息写入Redis Hash + 设置信号量
 * 2. 秒杀请求：校验时间、随机码、购买限制 → 尝试获取信号量 → 成功则发MQ创建订单
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeckillServiceImpl implements SeckillService {

    private static final String SECKILL_SKUS_KEY = "seckill:skus";
    private static final String SECKILL_STOCK_PREFIX = "seckill:stock:";
    private static final String SECKILL_MEMBER_PREFIX = "seckill:member:";

    private final StringRedisTemplate redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<SeckillSkuVO> getCurrentSeckillSkus() {
        long now = System.currentTimeMillis();
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SECKILL_SKUS_KEY);
        Map<String, String> all = ops.entries();
        if (all == null || all.isEmpty()) {
            return Collections.emptyList();
        }

        return all.values().stream()
                .map(this::parseSeckillSkuVO)
                .filter(vo -> vo != null)
                .filter(vo -> vo.getStartTime() != null && vo.getEndTime() != null
                        && now >= vo.getStartTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                        && now <= vo.getEndTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli())
                .collect(Collectors.toList());
    }

    @Override
    public SeckillSkuVO getSeckillSkuInfo(Long skuId) {
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SECKILL_SKUS_KEY);
        Map<String, String> all = ops.entries();
        if (all == null) {
            return null;
        }

        long now = System.currentTimeMillis();
        return all.values().stream()
                .map(this::parseSeckillSkuVO)
                .filter(vo -> vo != null)
                .filter(vo -> vo.getSkuId().equals(skuId)
                        && vo.getStartTime() != null && vo.getEndTime() != null
                        && now >= vo.getStartTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                        && now <= vo.getEndTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli())
                .findFirst()
                .orElse(null);
    }

    @Override
    public String kill(String killId, String code, Integer num, Long memberId) {
        // 1. 从Redis获取秒杀商品信息
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SECKILL_SKUS_KEY);
        String json = ops.get(killId);
        if (json == null) {
            log.warn("秒杀商品不存在: killId={}", killId);
            return null;
        }

        SeckillSkuVO vo = parseSeckillSkuVO(json);
        if (vo == null) {
            log.warn("秒杀商品解析失败: killId={}", killId);
            return null;
        }

        // 2. 校验随机码
        if (!vo.getRandomCode().equals(code)) {
            log.warn("秒杀随机码错误: killId={}", killId);
            return null;
        }

        // 3. 校验时间
        long now = System.currentTimeMillis();
        long start = vo.getStartTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        long end = vo.getEndTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (now < start || now > end) {
            log.warn("秒杀时间不在范围内: killId={}", killId);
            return null;
        }

        // 4. 校验购买数量
        if (num > vo.getSeckillLimit()) {
            log.warn("超过限购数量: killId={}, num={}, limit={}", killId, num, vo.getSeckillLimit());
            return null;
        }

        // 5. 检查是否已购买（幂等性）
        String memberKey = SECKILL_MEMBER_PREFIX + memberId + "_" + killId;
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(memberKey, num.toString(),
                end - now, TimeUnit.MILLISECONDS);
        if (absent == null || !absent) {
            log.warn("重复秒杀: memberId={}, killId={}", memberId, killId);
            return null;
        }

        // 6. 尝试获取库存（使用Redis原子操作）
        String stockKey = SECKILL_STOCK_PREFIX + vo.getRandomCode();
        Long remaining = redisTemplate.opsForValue().decrement(stockKey, num.longValue());
        if (remaining == null || remaining < 0) {
            // 库存不足，回滚
            redisTemplate.opsForValue().increment(stockKey, num.longValue());
            redisTemplate.delete(memberKey);
            log.warn("秒杀库存不足: killId={}", killId);
            return null;
        }

        // 7. 秒杀成功，生成订单号，发送MQ消息
        String orderSn = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> orderMsg = new HashMap<>();
        orderMsg.put("orderSn", orderSn);
        orderMsg.put("memberId", memberId);
        orderMsg.put("skuId", vo.getSkuId());
        orderMsg.put("num", num);
        orderMsg.put("seckillPrice", vo.getSeckillPrice());
        orderMsg.put("promotionSessionId", vo.getPromotionSessionId());

        try {
            rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderMsg);
            log.info("秒杀成功: memberId={}, skuId={}, orderSn={}", memberId, vo.getSkuId(), orderSn);
            return orderSn;
        } catch (Exception e) {
            // MQ发送失败，回滚库存
            redisTemplate.opsForValue().increment(stockKey, num.longValue());
            redisTemplate.delete(memberKey);
            log.error("秒杀MQ发送失败，已回滚: memberId={}, skuId={}", memberId, vo.getSkuId(), e);
            return null;
        }
    }

    /**
     * 解析SeckillSkuVO
     */
    private SeckillSkuVO parseSeckillSkuVO(String json) {
        try {
            return objectMapper.readValue(json, SeckillSkuVO.class);
        } catch (Exception e) {
            log.error("解析SeckillSkuVO失败: {}", json, e);
            return null;
        }
    }
}
