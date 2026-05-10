package com.hj.mall.order.constant;

/**
 * 订单模块常量
 */
public final class OrderConstant {

    private OrderConstant() {}

    /**
     * 订单状态
     */
    public static final class OrderStatus {
        /** 待付款 */
        public static final int CREATE = 0;
        /** 已付款/待发货 */
        public static final int PAYED = 1;
        /** 已发货 */
        public static final int SENDED = 2;
        /** 已完成 */
        public static final int RECEIVED = 3;
        /** 已关闭 */
        public static final int CANCELLED = 4;
        /** 无效订单 */
        public static final int INVALID = 5;
    }

    /** 防重令牌前缀 */
    public static final String ORDER_TOKEN_PREFIX = "order:token:";

    /** 订单超时时间（分钟） */
    public static final int ORDER_TIMEOUT_MINUTES = 30;

    /**
     * 支付方式
     */
    public static final class PayType {
        public static final int ALIPAY = 1;
        public static final int WECHAT = 2;
        public static final int UNIONPAY = 3;
        public static final int COD = 4;
    }
}
