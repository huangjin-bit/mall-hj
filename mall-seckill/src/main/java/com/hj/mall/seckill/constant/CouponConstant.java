package com.hj.mall.seckill.constant;

/**
 * 优惠券模块常量
 * （原 mall-coupon 的常量，现合并到 mall-seckill）
 */
public final class CouponConstant {

    private CouponConstant() {}

    /**
     * 优惠券类型
     */
    public static final class CouponType {
        /** 全场通用 */
        public static final int ALL = 0;
        /** 会员券 */
        public static final int MEMBER = 1;
        /** 购物券 */
        public static final int SHOPPING = 2;
        /** 注册赠券 */
        public static final int REGISTER = 3;
    }

    /**
     * 优惠券使用状态
     */
    public static final class UseStatus {
        /** 未使用 */
        public static final int UNUSED = 0;
        /** 已使用 */
        public static final int USED = 1;
        /** 已过期 */
        public static final int EXPIRED = 2;
    }

    /**
     * 秒杀活动状态
     */
    public static final class SeckillStatus {
        /** 未开始 */
        public static final int NOT_STARTED = 0;
        /** 进行中 */
        public static final int ONGOING = 1;
        /** 已结束 */
        public static final int ENDED = 2;
    }
}
