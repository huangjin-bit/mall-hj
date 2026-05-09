package com.hj.mall.product.service;

import com.hj.mall.product.entity.SpuAttrValue;

import java.util.List;

public interface SpuAttrValueService {

    List<SpuAttrValue> listBySpuId(Long spuId);

    void saveBatch(List<SpuAttrValue> values);

    void removeBySpuId(Long spuId);
}
