package com.hj.mall.gateway.filter;

import com.hj.mall.gateway.config.JwtConfig;
import com.hj.mall.gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final List<String> WHITE_LIST = List.of(
            "/auth/**",
            "/categories/**",
            "/brands/**",
            "/spu/**",
            "/sku/**",
            "/feign/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单路径放行
        for (String pattern : WHITE_LIST) {
            if (PATH_MATCHER.match(pattern, path)) {
                return chain.filter(exchange);
            }
        }

        // 提取 Token
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.warn("[Gateway] 未授权访问: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 解析用户信息并注入请求头
        Long memberId = jwtUtil.getMemberId(token);
        String username = jwtUtil.getUsername(token);

        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Id", String.valueOf(memberId))
                .header("X-Username", username)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private String extractToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(jwtConfig.getHeader());
        if (header != null && header.startsWith(jwtConfig.getTokenPrefix())) {
            return header.substring(jwtConfig.getTokenPrefix().length());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
