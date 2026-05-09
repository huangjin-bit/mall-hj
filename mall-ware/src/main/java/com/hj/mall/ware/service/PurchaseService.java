package com.hj.mall.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.Purchase;

import java.util.List;

public interface PurchaseService {

    Purchase getById(Long id);

    IPage<Purchase> listPage(Page<Purchase> page, Long wareId, Integer status);

    void save(Purchase purchase);

    void updateById(Purchase purchase);

    void removeBatch(List<Long> ids);
}
