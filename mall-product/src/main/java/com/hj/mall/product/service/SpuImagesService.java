package com.hj.mall.product.service;

import com.hj.mall.product.entity.SpuImages;

import java.util.List;

public interface SpuImagesService {

    List<SpuImages> listBySpuId(Long spuId);

    void saveBatch(Long spuId, List<String> images);

    void removeBySpuId(Long spuId);
}
