package com.hj.mall.gateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * RequestBlockFilter 单元测试
 *
 * <p>测试敏感路径拦截、恶意 User-Agent 识别等安全防护功能</p>
 *
 * @author mall-gateway
 * @since 2.0.0
 */
@DisplayName("网关请求拦截过滤器测试")
class RequestBlockFilterTest {

    private RequestBlockFilter filter;
    private GatewayFilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new RequestBlockFilter();
        chain = mock(GatewayFilterChain.class);
        // 模拟正常过滤器链返回
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("正常业务请求应该通过")
    void normalRequest_shouldPass() {
        // Given: 正常的业务请求路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该调用过滤器链的下一个过滤器
        verify(chain).filter(exchange);
        assertNotEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /actuator 路径应该被拦截")
    void actuatorPath_shouldBlock() {
        // Given: Spring Boot 敏感端点
        MockServerHttpRequest request = MockServerHttpRequest.get("/actuator/health").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 不应该调用后续过滤器，返回 403
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /.git 路径应该被拦截")
    void gitPath_shouldBlock() {
        // Given: Git 配置文件路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/.git/config").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /swagger 路径应该被拦截")
    void swaggerPath_shouldBlock() {
        // Given: API 文档路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/swagger-ui.html").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /wp-admin 路径应该被拦截")
    void wpAdminPath_shouldBlock() {
        // Given: WordPress 管理后台扫描路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/wp-admin/admin.php").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /env 路径应该被拦截")
    void envPath_shouldBlock() {
        // Given: 环境变量路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/env").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("sqlmap User-Agent 应该被拦截")
    void sqlmapUA_shouldBlock() {
        // Given: sqlmap 扫描工具的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/product/list")
                .header("User-Agent", "sqlmap/1.5")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 即使路径正常，恶意 UA 也应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("nikto User-Agent 应该被拦截")
    void niktoUA_shouldBlock() {
        // Given: nikto 扫描器的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/index.html")
                .header("User-Agent", "Mozilla/5.0 Nikto/2.1.5")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("正常浏览器 User-Agent 应该通过")
    void normalUA_shouldPass() {
        // Given: 正常浏览器的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/order/list")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该正常通过
        verify(chain).filter(exchange);
        assertNotEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("nmap User-Agent 应该被拦截")
    void nmapUA_shouldBlock() {
        // Given: nmap 端口扫描工具的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/product/list")
                .header("User-Agent", "Nmap Scripting Engine")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("burpsuite User-Agent 应该被拦截")
    void burpsuiteUA_shouldBlock() {
        // Given: Burp Suite 渗透测试工具的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/search/keyword")
                .header("User-Agent", "Burp Suite")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("wpscan User-Agent 应该被拦截")
    void wpscanUA_shouldBlock() {
        // Given: WPScan WordPress 扫描工具的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header("User-Agent", "WPScan/3.8.0")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("没有 User-Agent 头的请求应该通过")
    void nullUA_shouldPass() {
        // Given: 没有 User-Agent 的请求（某些合法客户端可能不发送）
        MockServerHttpRequest request = MockServerHttpRequest.get("/cart/list").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该正常通过
        verify(chain).filter(exchange);
    }

    @Test
    @DisplayName("访问 /v2/api-docs 应该被拦截")
    void apiDocsPath_shouldBlock() {
        // Given: API 文档路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/v2/api-docs").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /console 路径应该被拦截")
    void consolePath_shouldBlock() {
        // Given: 控制台路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/console").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("过滤器顺序应该是 -2")
    void order_shouldBeMinus2() {
        // Given & When & Then: 确保在 AuthGlobalFilter (-1) 之前执行
        assertEquals(-2, filter.getOrder(),
                "RequestBlockFilter 应该在 AuthGlobalFilter 之前执行");
    }

    @Test
    @DisplayName("路径包含敏感词应该被拦截")
    void pathContainingSensitiveKeyword_shouldBlock() {
        // Given: 路径中间包含敏感词
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/.env/config").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("bot User-Agent 应该被拦截")
    void botUA_shouldBlock() {
        // Given: 包含 bot 关键词的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/product/detail")
                .header("User-Agent", "Googlebot-Search")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("hydra User-Agent 应该被拦截")
    void hydraUA_shouldBlock() {
        // Given: hydra 暴力破解工具的 User-Agent
        MockServerHttpRequest request = MockServerHttpRequest.get("/login")
                .header("User-Agent", "THC-Hydra")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /admin 路径应该被拦截")
    void adminPath_shouldBlock() {
        // Given: 管理后台路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/admin/dashboard").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("访问 /jolokia 路径应该被拦截")
    void jolokiaPath_shouldBlock() {
        // Given: JMX HTTP 桥接路径
        MockServerHttpRequest request = MockServerHttpRequest.get("/jolokia/list").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该被拦截
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("正常带参数的请求应该通过")
    void normalRequestWithParams_shouldPass() {
        // Given: 正常的带查询参数的请求
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/order/list?page=1&size=10&status=1")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When: 执行过滤器
        filter.filter(exchange, chain).block();

        // Then: 应该正常通过
        verify(chain).filter(exchange);
        assertNotEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
    }
}