package com.hj.mall.order.service;

import com.hj.mall.order.entity.OrderReturnApply;
import com.hj.mall.order.entity.RefundInfo;
import com.hj.mall.order.feign.*;
import com.hj.mall.order.mapper.OrderReturnApplyMapper;
import com.hj.mall.order.mapper.RefundInfoMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderAfterSalesTest {

    @Autowired
    private OrderReturnApplyService returnApplyService;

    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private OrderReturnApplyMapper returnApplyMapper;

    @Autowired
    private RefundInfoMapper refundInfoMapper;

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

    @Test
    void submitReturnApply() {
        OrderReturnApply apply = new OrderReturnApply();
        apply.setOrderSn("ORD20268001");
        apply.setMemberId(1001L);
        apply.setSkuId(3001L);
        apply.setReturnType(1);
        apply.setReason("商品质量问题");
        apply.setReturnAmount(new BigDecimal("299.00"));
        apply.setSkuName("测试SKU");
        apply.setSkuCount(1);

        returnApplyService.save(apply);

        assertNotNull(apply.getId());
        assertEquals(0, apply.getStatus());
    }

    @Test
    void listReturnApplies() {
        OrderReturnApply a1 = new OrderReturnApply();
        a1.setOrderSn("ORD20268002");
        a1.setMemberId(1001L);
        a1.setSkuId(3001L);
        a1.setReason("不想要了");
        a1.setReturnAmount(new BigDecimal("99.00"));
        returnApplyService.save(a1);

        IPage<OrderReturnApply> page = returnApplyService.listPage(new Page<>(1, 10), 1001L, null);
        assertTrue(page.getTotal() >= 1);
    }

    @Test
    void approveReturn() {
        OrderReturnApply apply = new OrderReturnApply();
        apply.setOrderSn("ORD20268003");
        apply.setMemberId(1001L);
        apply.setSkuId(3001L);
        apply.setReason("发错货");
        apply.setReturnAmount(new BigDecimal("199.00"));
        returnApplyService.save(apply);

        returnApplyService.handle(apply.getId(), 1, 1L, "审核通过，同意退货");

        OrderReturnApply found = returnApplyMapper.selectById(apply.getId());
        assertEquals(1, found.getStatus());
        assertNotNull(found.getHandleTime());
    }

    @Test
    void rejectReturn() {
        OrderReturnApply apply = new OrderReturnApply();
        apply.setOrderSn("ORD20268004");
        apply.setMemberId(1001L);
        apply.setSkuId(3001L);
        apply.setReason("七天无理由");
        apply.setReturnAmount(new BigDecimal("50.00"));
        returnApplyService.save(apply);

        returnApplyService.handle(apply.getId(), 2, 1L, "超过退货期限");

        OrderReturnApply found = returnApplyMapper.selectById(apply.getId());
        assertEquals(2, found.getStatus());
    }

    @Test
    void createRefund() {
        RefundInfo info = new RefundInfo();
        info.setOrderSn("ORD20268001");
        info.setRefundSn("REF20268001");
        info.setRefundAmount(new BigDecimal("299.00"));
        info.setRefundChannel(1);

        refundInfoService.save(info);

        assertNotNull(info.getId());
        assertEquals(0, info.getRefundStatus());
    }

    @Test
    void updateRefundStatus() {
        RefundInfo info = new RefundInfo();
        info.setOrderSn("ORD20268005");
        info.setRefundSn("REF20268005");
        info.setRefundAmount(new BigDecimal("100.00"));
        refundInfoService.save(info);

        refundInfoService.updateStatus("REF20268005", 1);

        RefundInfo found = refundInfoService.getByOrderSn("ORD20268005");
        assertEquals(1, found.getRefundStatus());
    }
}