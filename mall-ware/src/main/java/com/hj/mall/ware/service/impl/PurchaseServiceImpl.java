package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.ware.entity.Purchase;
import com.hj.mall.ware.mapper.PurchaseMapper;
import com.hj.mall.ware.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseMapper purchaseMapper;

    @Override
    public Purchase getById(Long id) {
        Purchase purchase = purchaseMapper.selectById(id);
        if (purchase == null) {
            throw new BizException(ResultCode.WARE_NOT_FOUND);
        }
        return purchase;
    }

    @Override
    public IPage<Purchase> listPage(Page<Purchase> page, Long wareId, Integer status) {
        LambdaQueryWrapper<Purchase> wrapper = new LambdaQueryWrapper<>();
        if (wareId != null) {
            wrapper.eq(Purchase::getWareId, wareId);
        }
        if (status != null) {
            wrapper.eq(Purchase::getStatus, status);
        }
        wrapper.orderByDesc(Purchase::getCreateTime);
        return purchaseMapper.selectPage(page, wrapper);
    }

    @Override
    public void save(Purchase purchase) {
        purchaseMapper.insert(purchase);
    }

    @Override
    public void updateById(Purchase purchase) {
        purchaseMapper.updateById(purchase);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        purchaseMapper.deleteBatchIds(ids);
    }
}
