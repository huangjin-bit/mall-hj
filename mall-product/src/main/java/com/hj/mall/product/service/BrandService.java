package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.vo.BrandVO;

import java.util.List;

public interface BrandService {

    List<BrandVO> listBrands(Long categoryId);

    IPage<Brand> listPage(Page<Brand> page, String key);

    Brand getById(Long id);

    void save(Brand brand);

    void updateById(Brand brand);

    void removeBatch(List<Long> ids);
}
