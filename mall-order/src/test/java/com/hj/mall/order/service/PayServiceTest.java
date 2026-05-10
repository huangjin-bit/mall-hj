package com.hj.mall.order.service;

import com.hj.mall.common.exception.BizException;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.feign.*;
import com.hj.mall.order.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PayServiceTest {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private CartFeignClient cartFeignClient;

    @MockitoBean
    private MemberFeignClient memberFeignClient;

    @MockitoBean
    private ProductFeignClient productFeignClient;

    @MockitoBean
    private WareFeignClient wareFeignClient;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        ValueOperations<String, String> valueOps = org.mockito.Mockito.mock(ValueOperations.class);
        org.mockito.Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOps);
        org.mockito.Mockito.when(valueOps.get(org.mockito.Mockito.anyString())).thenReturn(null);
    }

    private String createTestOrder() {
        String orderSn = "ORD_PAY_" + System.nanoTime();
        Order order = new Order();
        order.setOrderSn(orderSn);
        order.setMemberId(9001L);
        order.setTotalAmount(new BigDecimal("199.00"));
        order.setPayAmount(new BigDecimal("199.00"));
        order.setStatus(0);
        order.setOrderType(1);
        order.setReceiverName("测试用户");
        order.setReceiverPhone("13800138000");
        order.setDeleteStatus(0);
        orderService.save(order);
        return orderSn;
    }

    @Test
    void createAlipayOrder_orderNotExist_shouldThrow() {
        assertThrows(BizException.class, () -> payService.createAlipayOrder("NOTEXIST123"));
    }

    @Test
    void createAlipayOrder_alreadyPaid_shouldThrow() {
        String orderSn = createTestOrder();
        orderService.handlePayResult(orderSn, 1);
        assertThrows(BizException.class, () -> payService.createAlipayOrder(orderSn));
    }

    @Test
    void handleAlipayNotify_invalidSignature_shouldReturnFalse() {
        String orderSn = createTestOrder();

        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", orderSn);
        params.put("trade_status", "TRADE_SUCCESS");
        params.put("trade_no", "2026051000001");

        // 测试环境中密钥不正确，签名验证必然失败
        boolean result = payService.handleAlipayNotify(params);
        assertFalse(result);
    }

    @Test
    void handleAlipayNotify_wrongTradeStatus_shouldReturnFalse() {
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", "NOTEXIST");
        params.put("trade_status", "WAIT_BUYER_PAY");
        params.put("trade_no", "2026051000002");

        boolean result = payService.handleAlipayNotify(params);
        assertFalse(result);
    }
}
