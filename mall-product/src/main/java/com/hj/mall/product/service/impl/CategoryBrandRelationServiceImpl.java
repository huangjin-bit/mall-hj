package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.entity.CategoryBrandRelation;
import com.hj.mall.product.mapper.BrandMapper;
import com.hj.mall.product.mapper.CategoryBrandRelationMapper;
import com.hj.mall.product.service.CategoryBrandRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryBrandRelationServiceImpl implements CategoryBrandRelationService {

    private final CategoryBrandRelationMapper relationMapper;
    private final BrandMapper brandMapper;

    @Override
    public IPage<CategoryBrandRelation> listPage(Page<CategoryBrandRelation> page, Long categoryId, Long brandId) {
        LambdaQueryWrapper<CategoryBrandRelation> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(CategoryBrandRelation::getCategoryId, categoryId);
        }
        if (brandId != null) {
            wrapper.eq(CategoryBrandRelation::getBrandId, brandId);
        }
        wrapper.orderByAsc(CategoryBrandRelation::getSort);
        return relationMapper.selectPage(page, wrapper);
    }

    @Override
    public void save(CategoryBrandRelation relation) {
        relationMapper.insert(relation);
    }

    @Override
    public void updateById(CategoryBrandRelation relation) {
        relationMapper.updateById(relation);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        relationMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Brand> listBrandsByCategory(Long categoryId) {
        List<CategoryBrandRelation> relations = relationMapper.selectList(
                new LambdaQueryWrapper<CategoryBrandRelation>()
                        .eq(CategoryBrandRelation::getCategoryId, categoryId)
        );
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> brandIds = relations.stream().map(CategoryBrandRelation::getBrandId).toList();
        return brandMapper.selectBatchIds(brandIds);
    }
}
