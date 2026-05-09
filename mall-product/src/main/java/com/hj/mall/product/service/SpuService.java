package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.SpuInfo;
import com.hj.mall.product.vo.SpuDetailVO;

import java.util.List;

public interface SpuService {

    SpuDetailVO getSpuDetail(Long spuId);

    SpuInfo getSpuInfo(Long spuId);

    IPage<SpuInfo> listPage(Page<SpuInfo> page, String key, Integer status);

    void save(SpuInfo spu);

    void updateById(SpuInfo spu);

    void removeBatch(List<Long> ids);

    void publish(Long spuId, Integer status);
}
