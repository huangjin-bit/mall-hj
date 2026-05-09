package com.hj.mall.product.service;

import com.hj.mall.product.entity.SkuImages;

import java.util.List;

public interface SkuImagesService {

    List<SkuImages> listBySkuId(Long skuId);

    void saveBatch(Long skuId, List<SkuImages> images);

    void removeBySkuId(Long skuId);
}
