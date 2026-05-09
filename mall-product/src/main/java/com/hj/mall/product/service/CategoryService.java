package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Category;
import com.hj.mall.product.vo.CategoryTreeVO;

import java.util.List;

public interface CategoryService {

    List<CategoryTreeVO> getCategoryTree();

    void refreshCategoryTreeCache();

    IPage<Category> listPage(Page<Category> page);

    Category getById(Long id);

    void save(Category category);

    void updateById(Category category);

    void removeBatch(List<Long> ids);
}
