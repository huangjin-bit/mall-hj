package com.hj.mall.order.service;

/**
 * 支付服务接口
 */
public interface PayService {

    /**
     * 创建支付宝支付订单
     * @param orderSn 订单号
     * @return 支付宝HTML表单字符串
     */
    String createAlipayOrder(String orderSn);

    /**
     * 处理支付宝异步通知
     * @param params 支付宝回调参数
     * @return 处理结果
     */
    boolean handleAlipayNotify(java.util.Map<String, String> params);
}
