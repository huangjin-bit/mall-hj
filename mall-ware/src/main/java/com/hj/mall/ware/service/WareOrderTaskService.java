package com.hj.mall.ware.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.ware.entity.WareOrderTask;

import java.util.List;

public interface WareOrderTaskService {

    WareOrderTask getByOrderSn(String orderSn);

    WareOrderTask getById(Long id);

    void save(WareOrderTask task);

    void updateStatus(Long id, Integer status);

    IPage<WareOrderTask> listPage(Page<WareOrderTask> page);

    void updateById(WareOrderTask task);

    void removeBatch(List<Long> ids);
}
