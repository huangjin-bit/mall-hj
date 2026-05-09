package com.hj.mall.common.context;

public record UserContext(Long userId, String username) {

    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    public static void set(UserContext ctx) {
        HOLDER.set(ctx);
    }

    public static UserContext get() {
        return HOLDER.get();
    }

    public static Long getUserId() {
        UserContext ctx = HOLDER.get();
        return ctx != null ? ctx.userId() : null;
    }

    public static void clear() {
        HOLDER.remove();
    }
}
