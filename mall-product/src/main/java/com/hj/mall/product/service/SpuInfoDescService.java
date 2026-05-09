package com.hj.mall.product.service;

import com.hj.mall.product.entity.SpuInfoDesc;

public interface SpuInfoDescService {

    SpuInfoDesc getById(Long spuId);

    void save(SpuInfoDesc desc);

    void updateById(SpuInfoDesc desc);
}
