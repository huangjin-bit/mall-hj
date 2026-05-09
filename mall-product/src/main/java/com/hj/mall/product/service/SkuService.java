package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.vo.SkuVO;

import java.util.List;

public interface SkuService {

    SkuVO getSkuDetail(Long skuId);

    SkuInfo getSkuInfo(Long skuId);

    IPage<SkuInfo> listPage(Page<SkuInfo> page, Long spuId);

    void save(SkuInfo sku);

    void updateById(SkuInfo sku);

    void removeBatch(List<Long> ids);
}
