package com.hj.mall.common.utils;

import com.hj.mall.common.constant.CommonConstant;
import com.hj.mall.common.context.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign 调用时自动透传当前用户信息到下游服务
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Long userId = UserContext.getUserId();
        if (userId != null) {
            template.header(CommonConstant.USER_ID_KEY, String.valueOf(userId));
            template.header(CommonConstant.USER_NAME_KEY,
                    String.valueOf(UserContext.get().username()));
        }
        // 透传原始 Token
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String token = request.getHeader(CommonConstant.TOKEN_HEADER);
            if (token != null) {
                template.header(CommonConstant.TOKEN_HEADER, token);
            }
        }
    }
}
