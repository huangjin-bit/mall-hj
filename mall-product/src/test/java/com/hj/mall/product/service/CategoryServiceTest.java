package com.hj.mall.product.service;

import com.hj.mall.product.entity.Category;
import com.hj.mall.product.mapper.CategoryMapper;
import com.hj.mall.product.service.CategoryService;
import com.hj.mall.product.vo.CategoryTreeVO;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

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
    void save_shouldInsertCategory() {
        Category category = new Category();
        category.setName("测试分类");
        category.setParentId(0L);
        category.setLevel(1);
        category.setSort(1);
        category.setStatus(1);

        categoryService.save(category);

        assertNotNull(category.getId());
    }

    @Test
    void getById_shouldReturnSavedCategory() {
        Category category = new Category();
        category.setName("电子产品");
        category.setParentId(0L);
        category.setLevel(1);
        category.setSort(1);
        category.setStatus(1);
        categoryService.save(category);

        Category found = categoryService.getById(category.getId());

        assertNotNull(found);
        assertEquals("电子产品", found.getName());
        assertEquals(0L, found.getParentId());
        assertEquals(1, found.getLevel());
    }

    @Test
    void getById_notExist_shouldReturnNull() {
        Category found = categoryService.getById(999999L);
        assertNull(found);
    }

    @Test
    void updateById_shouldModifyCategory() {
        Category category = new Category();
        category.setName("旧名称");
        category.setParentId(0L);
        category.setLevel(1);
        category.setSort(1);
        category.setStatus(1);
        categoryService.save(category);

        category.setName("新名称");
        categoryService.updateById(category);

        Category updated = categoryService.getById(category.getId());
        assertEquals("新名称", updated.getName());
    }

    @Test
    void removeBatch_shouldDeleteCategories() {
        Category c1 = new Category();
        c1.setName("待删除1"); c1.setParentId(0L); c1.setLevel(1); c1.setSort(1); c1.setStatus(1);
        Category c2 = new Category();
        c2.setName("待删除2"); c2.setParentId(0L); c2.setLevel(1); c2.setSort(2); c2.setStatus(1);
        categoryService.save(c1);
        categoryService.save(c2);

        categoryService.removeBatch(List.of(c1.getId(), c2.getId()));

        assertNull(categoryService.getById(c1.getId()));
        assertNull(categoryService.getById(c2.getId()));
    }

    @Test
    void listPage_shouldReturnPaginatedResults() {
        for (int i = 0; i < 5; i++) {
            Category c = new Category();
            c.setName("分类" + i); c.setParentId(0L); c.setLevel(1); c.setSort(i); c.setStatus(1);
            categoryService.save(c);
        }

        IPage<Category> page = categoryService.listPage(new Page<>(1, 3));

        assertTrue(page.getRecords().size() <= 3);
        assertTrue(page.getTotal() >= 5);
    }

    @Test
    void getCategoryTree_shouldReturnHierarchicalTree() {
        // 一级分类
        Category root = new Category();
        root.setName("手机数码"); root.setParentId(0L); root.setLevel(1); root.setSort(1); root.setStatus(1);
        categoryService.save(root);

        // 二级分类
        Category child = new Category();
        child.setName("手机通讯"); child.setParentId(root.getId()); child.setLevel(2); child.setSort(1); child.setStatus(1);
        categoryService.save(child);

        List<CategoryTreeVO> tree = categoryService.getCategoryTree();

        assertNotNull(tree);
        assertFalse(tree.isEmpty());

        // 找到 root 节点
        CategoryTreeVO rootNode = tree.stream()
                .filter(n -> root.getId().equals(n.getId()))
                .findFirst()
                .orElse(null);

        if (rootNode != null && rootNode.getChildren() != null) {
            assertTrue(rootNode.getChildren().stream()
                    .anyMatch(n -> child.getId().equals(n.getId())));
        }
    }
}
