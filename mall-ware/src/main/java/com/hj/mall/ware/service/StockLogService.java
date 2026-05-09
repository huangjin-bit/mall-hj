package com.hj.mall.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.StockLog;

public interface StockLogService {

    void save(StockLog log);

    IPage<StockLog> listPage(Page<StockLog> page, Long wareId, Long skuId);
}
