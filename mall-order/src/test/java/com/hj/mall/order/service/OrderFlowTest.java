package com.hj.mall.order.service;

import com.hj.mall.common.exception.BizException;
import com.hj.mall.order.constant.OrderConstant;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.entity.OrderOperateHistory;
import com.hj.mall.order.feign.*;
import com.hj.mall.order.mapper.OrderItemMapper;
import com.hj.mall.order.mapper.OrderMapper;
import com.hj.mall.order.mapper.OrderOperateHistoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderFlowTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderOperateHistoryMapper historyMapper;

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
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn(null);
    }

    // ==================== 辅助方法 ====================

    private Long memberId = 1001L;

    private Order createTestOrder(String orderSn, Integer status) {
        Order order = new Order();
        order.setOrderSn(orderSn);
        order.setMemberId(memberId);
        order.setTotalAmount(new BigDecimal("299.00"));
        order.setPayAmount(new BigDecimal("299.00"));
        order.setFreightAmount(BigDecimal.ZERO);
        order.setStatus(status);
        order.setPayType(1);
        order.setReceiverName("张三");
        order.setReceiverPhone("13800138000");
        order.setReceiverProvince("广东省");
        order.setReceiverCity("深圳市");
        order.setReceiverDistrict("南山区");
        order.setReceiverDetailAddress("科技园路1号");
        order.setDeleteStatus(0);
        order.setSourceType(1);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return order;
    }

    private OrderItem createTestItem(Order order, Long skuId, String skuName, BigDecimal price, int quantity) {
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setOrderSn(order.getOrderSn());
        item.setSkuId(skuId);
        item.setSpuId(2001L);
        item.setSkuName(skuName);
        item.setSpuName("测试商品");
        item.setSkuImg("test.jpg");
        item.setSkuPrice(price);
        item.setSkuQuantity(quantity);
        item.setSkuTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
        item.setCreateTime(LocalDateTime.now());
        return item;
    }

    // ==================== 测试用例 ====================

    @Test
    void createOrder_shouldPersist() {
        Order order = createTestOrder("ORD20260001", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        assertNotNull(order.getId());

        // 创建订单项
        OrderItem item = createTestItem(order, 3001L, "红色-XL", new BigDecimal("299.00"), 1);
        orderItemService.saveBatch(List.of(item));

        // 验证订单
        Order found = orderService.getByOrderSn("ORD20260001");
        assertNotNull(found);
        assertEquals(OrderConstant.OrderStatus.CREATE, found.getStatus());
        assertEquals(memberId, found.getMemberId());
        assertEquals(new BigDecimal("299.00"), found.getTotalAmount());

        // 验证订单项
        List<OrderItem> items = orderService.listOrderItems(found.getId());
        assertEquals(1, items.size());
        assertEquals("红色-XL", items.get(0).getSkuName());
        assertEquals(1, items.get(0).getSkuQuantity());

        // 验证操作历史
        LambdaQueryWrapper<OrderOperateHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderOperateHistory::getOrderId, found.getId());
        List<OrderOperateHistory> histories = historyMapper.selectList(wrapper);
        assertFalse(histories.isEmpty());
        assertEquals("创建订单", histories.get(0).getOperateType());
    }

    @Test
    void payOrder_shouldUpdateStatus() {
        Order order = createTestOrder("ORD20260002", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        // 模拟支付回调
        orderService.handlePayResult("ORD20260002", 1);

        Order paid = orderService.getByOrderSn("ORD20260002");
        assertEquals(OrderConstant.OrderStatus.PAYED, paid.getStatus());
        assertEquals(1, paid.getPayType());
    }

    @Test
    void shipOrder_shouldUpdateStatus() {
        Order order = createTestOrder("ORD20260003", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        // 支付
        orderService.handlePayResult("ORD20260003", 1);
        // 发货
        orderService.updateStatus(order.getId(), OrderConstant.OrderStatus.SENDED, "已发货", 1L);

        Order shipped = orderService.getByOrderSn("ORD20260003");
        assertEquals(OrderConstant.OrderStatus.SENDED, shipped.getStatus());
    }

    @Test
    void confirmReceive_shouldUpdateStatus() {
        Order order = createTestOrder("ORD20260004", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        // 支付 → 发货 → 确认收货
        orderService.handlePayResult("ORD20260004", 1);
        orderService.updateStatus(order.getId(), OrderConstant.OrderStatus.SENDED, "已发货", 1L);
        orderService.confirmReceive("ORD20260004", memberId);

        Order received = orderService.getByOrderSn("ORD20260004");
        assertEquals(OrderConstant.OrderStatus.RECEIVED, received.getStatus());
    }

    @Test
    void cancelOrder_shouldWork() {
        Order order = createTestOrder("ORD20260005", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        orderService.cancelOrder("ORD20260005", memberId);

        Order cancelled = orderService.getByOrderSn("ORD20260005");
        assertEquals(OrderConstant.OrderStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void cancelOrder_alreadyPaid_shouldThrow() {
        Order order = createTestOrder("ORD20260006", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);
        orderService.handlePayResult("ORD20260006", 1);

        assertThrows(BizException.class, () -> orderService.cancelOrder("ORD20260006", memberId));
    }

    @Test
    void closeExpiredOrder_shouldCancel() {
        Order order = createTestOrder("ORD20260007", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        // 模拟定时任务关单
        orderService.closeOrder(order);

        Order closed = orderService.getByOrderSn("ORD20260007");
        assertEquals(OrderConstant.OrderStatus.CANCELLED, closed.getStatus());
    }

    @Test
    void closeOrder_alreadyPaid_shouldNotCancel() {
        Order order = createTestOrder("ORD20260008", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);
        orderService.handlePayResult("ORD20260008", 1);

        // 已支付订单不应被关闭
        orderService.closeOrder(order);

        Order paid = orderService.getByOrderSn("ORD20260008");
        assertEquals(OrderConstant.OrderStatus.PAYED, paid.getStatus());
    }

    @Test
    void listOrdersByMember() {
        Order o1 = createTestOrder("ORD20260009A", OrderConstant.OrderStatus.CREATE);
        orderService.save(o1);
        Order o2 = createTestOrder("ORD20260009B", OrderConstant.OrderStatus.PAYED);
        orderService.save(o2);

        // 用不同member创建一个订单
        Order otherOrder = createTestOrder("ORD20260099", OrderConstant.OrderStatus.CREATE);
        otherOrder.setMemberId(9999L);
        orderService.save(otherOrder);

        IPage<Order> page = orderService.listPage(new Page<>(1, 10), null, null, memberId);
        assertTrue(page.getTotal() >= 2);
        page.getRecords().forEach(o -> assertEquals(memberId, o.getMemberId()));
    }

    @Test
    void operateHistory_shouldBeRecorded() {
        Order order = createTestOrder("ORD20260010", OrderConstant.OrderStatus.CREATE);
        orderService.save(order);

        // 支付
        orderService.handlePayResult("ORD20260010", 1);

        // 查询操作历史
        LambdaQueryWrapper<OrderOperateHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderOperateHistory::getOrderSn, "ORD20260010")
               .orderByAsc(OrderOperateHistory::getCreateTime);
        List<OrderOperateHistory> histories = historyMapper.selectList(wrapper);

        assertTrue(histories.size() >= 2);
        assertEquals("创建订单", histories.get(0).getOperateType());
        assertEquals("支付成功", histories.get(1).getOperateType());
    }
}
