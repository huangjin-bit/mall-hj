package com.hj.mall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.order.entity.OrderReturnApply;

public interface OrderReturnApplyService {

    IPage<OrderReturnApply> listPage(Page<OrderReturnApply> page, Long memberId, Integer status);

    void save(OrderReturnApply apply);

    void handle(Long id, Integer status, Long handleBy, String handleNote);
}
