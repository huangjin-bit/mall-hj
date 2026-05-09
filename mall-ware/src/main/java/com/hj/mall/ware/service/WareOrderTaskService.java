package com.hj.mall.ware.service;

import com.hj.mall.ware.entity.WareOrderTask;

public interface WareOrderTaskService {

    WareOrderTask getByOrderSn(String orderSn);

    WareOrderTask getById(Long id);

    void save(WareOrderTask task);

    void updateStatus(Long id, Integer status);
}
