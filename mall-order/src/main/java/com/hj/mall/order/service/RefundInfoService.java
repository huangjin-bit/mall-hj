package com.hj.mall.order.service;

import com.hj.mall.order.entity.RefundInfo;

public interface RefundInfoService {

    void save(RefundInfo info);

    RefundInfo getByOrderSn(String orderSn);

    void updateStatus(String refundSn, Integer status);
}
