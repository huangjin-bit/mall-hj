package com.hj.mall.common.filter;

import com.hj.mall.common.constant.CommonConstant;
import com.hj.mall.common.context.UserContext;
import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.common.utils.HeaderSigner;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UserContextFilter extends OncePerRequestFilter {

    private final HeaderSigner signer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserContextFilter(HeaderSigner signer) {
        this.signer = signer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(CommonConstant.USER_ID_KEY);
        String username = request.getHeader(CommonConstant.USER_NAME_KEY);
        String sign = request.getHeader(CommonConstant.USER_SIGN_KEY);

        if (userId != null) {
            if (signer != null) {
                // 有签名密钥 → 必须验签
                if (sign == null || !signer.verify(userId, username, sign)) {
                    reject(response);
                    return;
                }
            }
            UserContext.set(new UserContext(Long.parseLong(userId), username));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private void reject(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(ResultCode.TOKEN_INVALID);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
