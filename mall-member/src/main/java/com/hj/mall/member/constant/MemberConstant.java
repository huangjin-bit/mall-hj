package com.hj.mall.member.constant;

/**
 * 会员模块常量
 */
public final class MemberConstant {

    private MemberConstant() {}

    /** 默认等级状态 */
    public static final int DEFAULT_LEVEL_STATUS = 1;

    /**
     * 会员性别
     */
    public static final class Gender {
        public static final int UNKNOWN = 0;
        public static final int MALE = 1;
        public static final int FEMALE = 2;
    }

    /**
     * 会员状态
     */
    public static final class Status {
        public static final int DISABLED = 0;
        public static final int ENABLED = 1;
    }
}
