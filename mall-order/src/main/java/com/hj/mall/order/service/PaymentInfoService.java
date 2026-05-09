package com.hj.mall.order.service;

import com.hj.mall.order.entity.PaymentInfo;

public interface PaymentInfoService {

    PaymentInfo getByOrderSn(String orderSn);

    void save(PaymentInfo info);

    void updateStatus(String orderSn, Integer status, String callbackContent);
}
