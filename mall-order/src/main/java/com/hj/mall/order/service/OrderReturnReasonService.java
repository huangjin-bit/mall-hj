package com.hj.mall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.order.entity.OrderReturnReason;

import java.util.List;

public interface OrderReturnReasonService {

    IPage<OrderReturnReason> listPage(Page<OrderReturnReason> page);

    OrderReturnReason getById(Long id);

    void save(OrderReturnReason reason);

    void updateById(OrderReturnReason reason);

    void removeBatch(List<Long> ids);
}
