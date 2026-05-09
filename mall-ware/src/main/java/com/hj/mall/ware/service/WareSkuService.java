package com.hj.mall.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.WareSku;

import java.util.List;
import java.util.Map;

public interface WareSkuService {

    IPage<WareSku> listPage(Page<WareSku> page, Long wareId, Long skuId);

    WareSku getById(Long id);

    void save(WareSku wareSku);

    void updateById(WareSku wareSku);

    void removeBatch(List<Long> ids);

    Map<Long, Integer> getStockBySkuIds(List<Long> skuIds);

    boolean lockStock(Long orderId, String orderSn, List<Map<String, Object>> items);

    void deductStock(Long taskId);

    void releaseStock(Long taskId);
}
