package com.hj.mall.ware.constant;

/**
 * 库存模块常量
 */
public final class WareConstant {

    private WareConstant() {}

    /**
     * 采购需求状态
     */
    public static final class PurchaseDetailStatus {
        public static final int NEW = 0;
        public static final int ASSIGNED = 1;
        public static final int BUYING = 2;
        public static final int FINISH = 3;
        public static final int FAIL = 4;
    }

    /**
     * 采购单状态
     */
    public static final class PurchaseStatus {
        public static final int NEW = 0;
        public static final int ASSIGNED = 1;
        public static final int RECEIVED = 2;
        public static final int FINISH = 3;
        public static final int ERROR = 4;
    }

    /**
     * 库存锁定状态
     */
    public static final class StockLockStatus {
        /** 已锁定 */
        public static final int LOCKED = 1;
        /** 已解锁 */
        public static final int UNLOCKED = 2;
        /** 已扣减 */
        public static final int DEDUCTED = 3;
    }
}
