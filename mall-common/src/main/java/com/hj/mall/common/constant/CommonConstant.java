package com.hj.mall.common.constant;

public final class CommonConstant {

    private CommonConstant() {}

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String USER_ID_KEY = "X-User-Id";
    public static final String USER_NAME_KEY = "X-User-Name";
    public static final String USER_SIGN_KEY = "X-User-Sign";

    public static final String SEATA_GROUP = "mall-tx-group";
}
