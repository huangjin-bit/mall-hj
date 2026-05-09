package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.mall.product.entity.Category;
import com.hj.mall.product.mapper.CategoryMapper;
import com.hj.mall.product.service.CategoryService;
import com.hj.mall.product.vo.CategoryTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_TREE_KEY = "product:category:tree";
    private static final long CATEGORY_TREE_TTL_HOURS = 1;

    private final CategoryMapper categoryMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<CategoryTreeVO> getCategoryTree() {
        String json = redisTemplate.opsForValue().get(CATEGORY_TREE_KEY);
        if (json != null) {
            return deserializeTree(json);
        }

        List<CategoryTreeVO> tree = buildTree();
        cacheTree(tree);
        return tree;
    }

    @Override
    public void refreshCategoryTreeCache() {
        List<CategoryTreeVO> tree = buildTree();
        cacheTree(tree);
        log.info("[CategoryService] 分类树缓存已刷新");
    }

    private List<CategoryTreeVO> buildTree() {
        List<Category> all = categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getStatus, 1)
                        .orderByAsc(Category::getSort)
        );

        List<CategoryTreeVO> roots = new ArrayList<>();
        for (Category c : all) {
            if (c.getLevel() == 1) {
                roots.add(toVO(c));
            }
        }

        for (CategoryTreeVO l1 : roots) {
            List<CategoryTreeVO> l2List = new ArrayList<>();
            for (Category c : all) {
                if (c.getParentId().equals(l1.getId())) {
                    l2List.add(toVO(c));
                }
            }
            l1.setChildren(l2List);

            for (CategoryTreeVO l2 : l2List) {
                List<CategoryTreeVO> l3List = new ArrayList<>();
                for (Category c : all) {
                    if (c.getParentId().equals(l2.getId())) {
                        l3List.add(toVO(c));
                    }
                }
                l2.setChildren(l3List);
            }
        }
        return roots;
    }

    private CategoryTreeVO toVO(Category c) {
        CategoryTreeVO vo = new CategoryTreeVO();
        vo.setId(c.getId());
        vo.setName(c.getName());
        vo.setParentId(c.getParentId());
        vo.setLevel(c.getLevel());
        vo.setIcon(c.getIcon());
        vo.setSort(c.getSort());
        return vo;
    }

    private void cacheTree(List<CategoryTreeVO> tree) {
        try {
            String json = objectMapper.writeValueAsString(tree);
            redisTemplate.opsForValue().set(CATEGORY_TREE_KEY, json, CATEGORY_TREE_TTL_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("[CategoryService] 序列化分类树失败", e);
        }
    }

    private List<CategoryTreeVO> deserializeTree(String json) {
        try {
            return objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryTreeVO.class));
        } catch (Exception e) {
            log.error("[CategoryService] 反序列化分类树失败，重新构建", e);
            return buildTree();
        }
    }

    @Override
    public IPage<Category> listPage(Page<Category> page) {
        return categoryMapper.selectPage(page,
                new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
    }

    @Override
    public Category getById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public void save(Category category) {
        categoryMapper.insert(category);
        redisTemplate.delete(CATEGORY_TREE_KEY);
    }

    @Override
    public void updateById(Category category) {
        categoryMapper.updateById(category);
        redisTemplate.delete(CATEGORY_TREE_KEY);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        categoryMapper.deleteBatchIds(ids);
        redisTemplate.delete(CATEGORY_TREE_KEY);
    }
}
