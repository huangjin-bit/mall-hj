package com.hj.mall.order.service;

import com.hj.mall.order.entity.OrderSetting;

public interface OrderSettingService {

    OrderSetting getSetting();

    void update(OrderSetting setting);
}
