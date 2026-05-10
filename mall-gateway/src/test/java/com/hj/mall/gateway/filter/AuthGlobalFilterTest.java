package com.hj.mall.gateway.filter;

import com.hj.mall.gateway.config.JwtConfig;
import com.hj.mall.gateway.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AuthGlobalFilter 纯单元测试
 * 使用 Mockito 模拟依赖，测试过滤器的核心逻辑
 */
class AuthGlobalFilterTest {

    private AuthGlobalFilter filter;
    private JwtUtil jwtUtil;
    private JwtConfig jwtConfig;
    private GatewayFilterChain chain;

    @BeforeEach
    void setUp() {
        // 模拟 JwtUtil 依赖
        jwtUtil = mock(JwtUtil.class);

        // 配置 JwtConfig（使用真实对象，因为是简单的配置类）
        jwtConfig = new JwtConfig();
        jwtConfig.setHeader("Authorization");
        jwtConfig.setTokenPrefix("Bearer ");

        // 创建过滤器实例（使用构造函数注入，模拟 @RequiredArgsConstructor 行为）
        filter = new AuthGlobalFilter(jwtUtil, jwtConfig);

        // 模拟 GatewayFilterChain
        chain = mock(GatewayFilterChain.class);
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
    }

    @Test
    void whiteList_authPath_shouldPass() {
        // 测试 /auth/** 路径在白名单中，应该直接放行
        MockServerHttpRequest request = MockServerHttpRequest.get("/auth/login").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_sysLogin_shouldPass() {
        // 测试 /sys/login 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/sys/login").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_categories_shouldPass() {
        // 测试 /categories/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/categories/list").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_brands_shouldPass() {
        // 测试 /brands/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/brands/1").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_spu_shouldPass() {
        // 测试 /spu/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/spu/detail/123").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_sku_shouldPass() {
        // 测试 /sku/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/sku/456").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_feign_shouldPass() {
        // 测试 /feign/** 路径在白名单中（内部 Feign 调用）
        MockServerHttpRequest request = MockServerHttpRequest.get("/feign/sku/123").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_search_shouldPass() {
        // 测试 /search/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/search/product").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_homeAdv_shouldPass() {
        // 测试 /home-adv/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/home-adv/list").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_homeSubject_shouldPass() {
        // 测试 /home-subject/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/home-subject/1").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_seckillSession_shouldPass() {
        // 测试 /seckill-session/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/seckill-session/current").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_seckillSku_shouldPass() {
        // 测试 /seckill-sku/** 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/seckill-sku/list/1").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void whiteList_couponAvailable_shouldPass() {
        // 测试 /coupon/available 路径在白名单中
        MockServerHttpRequest request = MockServerHttpRequest.get("/coupon/available").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        verify(chain).filter(exchange);
        verify(jwtUtil, never()).validateToken(any());
    }

    @Test
    void noToken_shouldReturn401() {
        // 测试没有 token 的请求应该返回 401
        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        // 验证链没有被调用（请求被拦截）
        verify(chain, never()).filter(any());

        // 验证响应状态码为 401
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void validToken_shouldInjectHeaders() {
        // 测试有效 token 应该注入用户信息到请求头
        String token = "valid.jwt.token";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getMemberId(token)).thenReturn(42L);
        when(jwtUtil.getUsername(token)).thenReturn("testuser");

        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list")
                .header("Authorization", "Bearer " + token)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        // 捕获传递给 chain 的 exchange
        ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
        verify(chain).filter(captor.capture());

        ServerWebExchange filteredExchange = captor.getValue();

        // 验证用户信息被正确注入到请求头
        assertEquals("42", filteredExchange.getRequest().getHeaders().getFirst("X-User-Id"));
        assertEquals("testuser", filteredExchange.getRequest().getHeaders().getFirst("X-Username"));

        // 验证原始的 Authorization 头仍然存在
        assertEquals("Bearer " + token, filteredExchange.getRequest().getHeaders().getFirst("Authorization"));
    }

    @Test
    void invalidToken_shouldReturn401() {
        // 测试无效 token 应该返回 401
        String invalidToken = "bad.token.value";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list")
                .header("Authorization", "Bearer " + invalidToken)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        // 验证链没有被调用（请求被拦截）
        verify(chain, never()).filter(any());

        // 验证响应状态码为 401
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void tokenWithoutBearerPrefix_shouldReturn401() {
        // 测试没有 Bearer 前缀的 token 应该返回 401
        String token = "valid.jwt.token";
        when(jwtUtil.validateToken(token)).thenReturn(true);

        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list")
                .header("Authorization", token)  // 缺少 "Bearer " 前缀
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        // 验证链没有被调用（请求被拦截，因为 extractToken 返回 null）
        verify(chain, never()).filter(any());

        // 验证响应状态码为 401
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void emptyToken_shouldReturn401() {
        // 测试空 token 应该返回 401
        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list")
                .header("Authorization", "Bearer ")  // 空的 token
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        // 验证链没有被调用
        verify(chain, never()).filter(any());

        // 验证响应状态码为 401
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void customHeaderName_shouldWork() {
        // 测试自定义 header 名称应该正常工作
        jwtConfig.setHeader("X-Auth-Token");
        jwtConfig.setTokenPrefix("Token ");

        String token = "custom.jwt.token";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getMemberId(token)).thenReturn(99L);
        when(jwtUtil.getUsername(token)).thenReturn("customuser");

        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list")
                .header("X-Auth-Token", "Token " + token)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        filter.filter(exchange, chain).block();

        ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);
        verify(chain).filter(captor.capture());

        ServerWebExchange filteredExchange = captor.getValue();

        assertEquals("99", filteredExchange.getRequest().getHeaders().getFirst("X-User-Id"));
        assertEquals("customuser", filteredExchange.getRequest().getHeaders().getFirst("X-Username"));
    }

    @Test
    void order() {
        // 测试过滤器的顺序
        assertEquals(-1, filter.getOrder());
    }
}
