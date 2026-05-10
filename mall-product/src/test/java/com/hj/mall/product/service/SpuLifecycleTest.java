package com.hj.mall.product.service;

import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.entity.SpuInfo;
import com.hj.mall.product.vo.SpuCommentSubmitVO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class SpuLifecycleTest {

    @Autowired
    private SpuService spuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpuCommentService spuCommentService;

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
    void createBrandAndSpu() {
        Brand brand = new Brand();
        brand.setName("华为");
        brand.setLogo("huawei.png");
        brand.setSort(1);
        brand.setStatus(1);
        brandService.save(brand);
        assertNotNull(brand.getId());

        SpuInfo spu = new SpuInfo();
        spu.setSpuName("华为Mate60 Pro");
        spu.setCategoryId(1L);
        spu.setBrandId(brand.getId());
        spu.setPublishStatus(0);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuService.save(spu);

        assertNotNull(spu.getId());
        assertEquals(brand.getId(), spu.getBrandId());
    }

    @Test
    void publishSpu() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("待上架商品");
        spu.setPublishStatus(0);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuService.save(spu);

        spuService.publish(spu.getId(), 1);

        SpuInfo found = spuService.getSpuInfo(spu.getId());
        assertEquals(1, found.getPublishStatus());
    }

    @Test
    void unpublishSpu() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("已上架商品");
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuService.save(spu);

        spuService.publish(spu.getId(), 0);

        SpuInfo found = spuService.getSpuInfo(spu.getId());
        assertEquals(0, found.getPublishStatus());
    }

    @Test
    void listSpuByStatus() {
        SpuInfo spu1 = new SpuInfo();
        spu1.setSpuName("上架SPU");
        spu1.setPublishStatus(1);
        spu1.setAuditStatus(1);
        spu1.setSort(1);
        spuService.save(spu1);

        SpuInfo spu2 = new SpuInfo();
        spu2.setSpuName("下架SPU");
        spu2.setPublishStatus(0);
        spu2.setAuditStatus(1);
        spu2.setSort(2);
        spuService.save(spu2);

        IPage<SpuInfo> published = spuService.listPage(new Page<>(1, 10), null, 1);
        assertTrue(published.getRecords().stream().allMatch(s -> s.getPublishStatus() == 1));
    }

    @Test
    void addComment() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("评论测试商品");
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuService.save(spu);

        SpuCommentSubmitVO vo = new SpuCommentSubmitVO();
        vo.setSpuId(spu.getId());
        vo.setSkuId(3001L);
        vo.setStar(5);
        vo.setContent("非常好，物超所值！");

        spuCommentService.submitComment(1001L, "测试用户", "avatar.jpg", vo);

        IPage<com.hj.mall.product.entity.SpuComment> comments =
                spuCommentService.queryBySpuId(new Page<>(1, 10), spu.getId());
        assertTrue(comments.getTotal() >= 1);
        assertEquals("非常好，物超所值！", comments.getRecords().get(0).getContent());
    }

    @Test
    void likeComment() {
        SpuInfo spu = new SpuInfo();
        spu.setSpuName("点赞测试商品");
        spu.setPublishStatus(1);
        spu.setAuditStatus(1);
        spu.setSort(1);
        spuService.save(spu);

        SpuCommentSubmitVO vo = new SpuCommentSubmitVO();
        vo.setSpuId(spu.getId());
        vo.setSkuId(3002L);
        vo.setStar(4);
        vo.setContent("不错");
        spuCommentService.submitComment(1002L, "用户B", "avatar2.jpg", vo);

        IPage<com.hj.mall.product.entity.SpuComment> comments =
                spuCommentService.queryBySpuId(new Page<>(1, 10), spu.getId());
        Long commentId = comments.getRecords().get(0).getId();

        spuCommentService.likeComment(commentId);

        com.hj.mall.product.vo.SpuCommentVO detail = spuCommentService.getCommentDetail(commentId);
        assertEquals(1, detail.getLikesCount());
    }
}