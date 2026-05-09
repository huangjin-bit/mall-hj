package com.hj.mall.common.utils;

import com.hj.mall.common.constant.CommonConstant;
import com.hj.mall.common.context.UserContext;
import jakarta.servlet.http.HttpServletRequest;

public final class WebUtils {

    private WebUtils() {}

    /**
     * 从请求头解析用户信息并存入 ThreadLocal
     */
    public static void resolveUser(HttpServletRequest request) {
        String idStr = request.getHeader(CommonConstant.USER_ID_KEY);
        String username = request.getHeader(CommonConstant.USER_NAME_KEY);
        if (idStr != null) {
            UserContext.set(new UserContext(Long.parseLong(idStr), username));
        }
    }

    /**
     * 获取客户端真实 IP（支持代理透传）
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For 可能含多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
