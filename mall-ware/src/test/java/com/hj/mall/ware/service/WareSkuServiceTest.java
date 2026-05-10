package com.hj.mall.ware.service;

import com.hj.mall.common.exception.BizException;
import com.hj.mall.ware.entity.WareSku;
import com.hj.mall.ware.feign.OrderFeignClient;
import com.hj.mall.ware.mapper.WareSkuMapper;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WareSkuServiceTest {

    @Autowired
    private WareSkuService wareSkuService;

    @Autowired
    private WareSkuMapper wareSkuMapper;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private OrderFeignClient orderFeignClient;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    private WareSku createWareSku(Long wareId, Long skuId, int stock) {
        WareSku ws = new WareSku();
        ws.setWareId(wareId);
        ws.setSkuId(skuId);
        ws.setStock(stock);
        ws.setStockLocked(0);
        ws.setStockTotal(stock);
        ws.setLowStock(10);
        return ws;
    }

    @Test
    void saveWareSku() {
        WareSku ws = createWareSku(1L, 3001L, 100);
        wareSkuService.save(ws);

        assertNotNull(ws.getId());
        WareSku found = wareSkuService.getById(ws.getId());
        assertEquals(100, found.getStock());
        assertEquals(0, found.getStockLocked());
    }

    @Test
    void getStockBySkuIds() {
        wareSkuService.save(createWareSku(1L, 4001L, 50));
        wareSkuService.save(createWareSku(2L, 4001L, 30));

        Map<Long, Integer> stockMap = wareSkuService.getStockBySkuIds(List.of(4001L));

        assertEquals(80, stockMap.get(4001L));
    }

    @Test
    void lockStock_shouldDeduct() {
        wareSkuService.save(createWareSku(1L, 5001L, 100));

        boolean locked = wareSkuService.lockStock(1L, "ORD20269001",
                List.of(Map.of("skuId", 5001L, "quantity", 10, "skuName", "测试SKU")));

        assertTrue(locked);
        WareSku ws = wareSkuMapper.selectList(null).stream()
                .filter(w -> w.getSkuId().equals(5001L)).findFirst().orElse(null);
        assertNotNull(ws);
        assertEquals(90, ws.getStock());
        assertEquals(10, ws.getStockLocked());
    }

    @Test
    void lockStock_insufficient_shouldThrow() {
        wareSkuService.save(createWareSku(1L, 5002L, 5));

        assertThrows(BizException.class, () ->
                wareSkuService.lockStock(2L, "ORD20269002",
                        List.of(Map.of("skuId", 5002L, "quantity", 10, "skuName", "缺货SKU"))));
    }

    @Test
    void removeBatchWareSkus() {
        WareSku ws1 = createWareSku(1L, 6001L, 20);
        WareSku ws2 = createWareSku(1L, 6002L, 30);
        wareSkuService.save(ws1);
        wareSkuService.save(ws2);

        wareSkuService.removeBatch(List.of(ws1.getId(), ws2.getId()));

        assertThrows(BizException.class, () -> wareSkuService.getById(ws1.getId()));
        assertThrows(BizException.class, () -> wareSkuService.getById(ws2.getId()));
    }
}