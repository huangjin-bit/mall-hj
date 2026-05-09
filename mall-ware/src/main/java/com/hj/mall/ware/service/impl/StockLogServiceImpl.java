package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.StockLog;
import com.hj.mall.ware.mapper.StockLogMapper;
import com.hj.mall.ware.service.StockLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockLogServiceImpl implements StockLogService {

    private final StockLogMapper stockLogMapper;

    @Override
    public void save(StockLog log) {
        stockLogMapper.insert(log);
    }

    @Override
    public IPage<StockLog> listPage(Page<StockLog> page, Long wareId, Long skuId) {
        LambdaQueryWrapper<StockLog> wrapper = new LambdaQueryWrapper<>();
        if (wareId != null) {
            wrapper.eq(StockLog::getWareId, wareId);
        }
        if (skuId != null) {
            wrapper.eq(StockLog::getSkuId, skuId);
        }
        wrapper.orderByDesc(StockLog::getCreateTime);
        return stockLogMapper.selectPage(page, wrapper);
    }
}
