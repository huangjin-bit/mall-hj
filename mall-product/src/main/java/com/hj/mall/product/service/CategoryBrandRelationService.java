package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.entity.CategoryBrandRelation;

import java.util.List;

public interface CategoryBrandRelationService {

    IPage<CategoryBrandRelation> listPage(Page<CategoryBrandRelation> page, Long categoryId, Long brandId);

    void save(CategoryBrandRelation relation);

    void updateById(CategoryBrandRelation relation);

    void removeBatch(List<Long> ids);

    List<Brand> listBrandsByCategory(Long categoryId);
}
