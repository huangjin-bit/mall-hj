package com.hj.mall.product.service;

import com.hj.mall.product.entity.SkuSaleAttrValue;

import java.util.List;

public interface SkuSaleAttrValueService {

    List<SkuSaleAttrValue> listBySkuId(Long skuId);

    List<SkuSaleAttrValue> listBySpuId(Long spuId);

    void saveBatch(List<SkuSaleAttrValue> values);

    void removeBySkuId(Long skuId);
}
