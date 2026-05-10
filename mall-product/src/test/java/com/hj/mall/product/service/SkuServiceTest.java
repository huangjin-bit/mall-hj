package com.hj.mall.product.service;

import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.entity.SpuInfo;
import com.hj.mall.product.mapper.SkuInfoMapper;
import com.hj.mall.product.mapper.SpuInfoMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hj.mall.common.exception.BizException;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class SkuServiceTest {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get(anyString())).thenReturn(null);
    }

    @Test
    void saveAndGetSku() {
        // 先创建 SPU
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("华为Mate60 Pro");
        spu.setCategoryId(1L);
        spu.setBrandId(1L);
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuInfoMapper.insert(spu);
        assertNotNull(spu.getId());

        // 创建 SKU
        SkuInfo sku = new SkuInfo();
        sku.setSpuId(spu.getId());
        sku.setSkuName("华为Mate60 Pro 12GB+512GB 雅丹黑");
        sku.setPrice(new BigDecimal("6999.00"));
        sku.setOriginalPrice(new BigDecimal("7999.00"));
        sku.setSkuImg("mate60pro.jpg");
        sku.setPublishStatus(1);
        sku.setSort(1);
        skuService.save(sku);

        assertNotNull(sku.getId());

        SkuInfo found = skuService.getSkuInfo(sku.getId());
        assertNotNull(found);
        assertEquals("华为Mate60 Pro 12GB+512GB 雅丹黑", found.getSkuName());
        assertEquals(new BigDecimal("6999.00"), found.getPrice());
        assertEquals(spu.getId(), found.getSpuId());
    }

    @Test
    void listPage_shouldReturnPaginatedSkus() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("测试SPU");
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuInfoMapper.insert(spu);

        for (int i = 0; i < 5; i++) {
            SkuInfo sku = new SkuInfo();
            sku.setSpuId(spu.getId());
            sku.setSkuName("SKU-" + i);
            sku.setPrice(new BigDecimal("99.00"));
            sku.setPublishStatus(1);
            sku.setSort(i);
            skuService.save(sku);
        }

        IPage<SkuInfo> page = skuService.listPage(new Page<>(1, 3), spu.getId());
        assertTrue(page.getRecords().size() <= 3);
        assertTrue(page.getTotal() >= 5);
    }

    @Test
    void updateSkuPrice() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("测试SPU2");
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuInfoMapper.insert(spu);

        SkuInfo sku = new SkuInfo();
        sku.setSpuId(spu.getId());
        sku.setSkuName("降价商品");
        sku.setPrice(new BigDecimal("199.00"));
        sku.setPublishStatus(1);
        sku.setSort(1);
        skuService.save(sku);

        sku.setPrice(new BigDecimal("149.00"));
        skuService.updateById(sku);

        SkuInfo updated = skuService.getSkuInfo(sku.getId());
        assertEquals(new BigDecimal("149.00"), updated.getPrice());
    }

    @Test
    void removeBatchSkus() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("测试SPU3");
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuInfoMapper.insert(spu);

        SkuInfo sku1 = new SkuInfo();
        sku1.setSpuId(spu.getId());
        sku1.setSkuName("待删SKU1");
        sku1.setPrice(new BigDecimal("99.00"));
        sku1.setPublishStatus(1);
        sku1.setSort(1);
        skuService.save(sku1);

        SkuInfo sku2 = new SkuInfo();
        sku2.setSpuId(spu.getId());
        sku2.setSkuName("待删SKU2");
        sku2.setPrice(new BigDecimal("99.00"));
        sku2.setPublishStatus(1);
        sku2.setSort(2);
        skuService.save(sku2);

        skuService.removeBatch(java.util.List.of(sku1.getId(), sku2.getId()));

        assertThrows(BizException.class, () -> skuService.getSkuInfo(sku1.getId()));
        assertThrows(BizException.class, () -> skuService.getSkuInfo(sku2.getId()));
    }
}
