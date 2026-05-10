package com.hj.mall.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * 网关请求拦截过滤器
 *
 * <p>在鉴权之前执行，用于拦截恶意扫描器、爬虫和敏感路径访问</p>
 *
 * <ul>拦截范围：
 *   <li>敏感路径：/actuator, /swagger, /.git, /env 等不应该对外暴露的端点</li>
 *   <li>恶意 User-Agent：sqlmap, nikto, nmap 等已知扫描工具特征</li>
 * </ul>
 *
 * @author mall-gateway
 * @since 2.0.0
 */
@Slf4j
@Component
public class RequestBlockFilter implements GlobalFilter, Ordered {

    /**
     * 需要拦截的敏感路径前缀或关键词
     *
     * <p>包含常见的敏感端点、管理后台、配置文件、框架默认路径等</p>
     */
    private static final Set<String> BLOCKED_PATHS = Set.of(
            // Spring Boot 敏感端点
            "/actuator", "/env", "/configprops", "/beans", "/mappings",
            "/dump", "/health", "/info", "/metrics", "/trace", "/flyway", "/liquibase",
            "/config",

            // API 文档
            "/swagger", "/v2/api-docs", "/v3/api-docs", "/api-docs",

            // 版本控制/配置文件
            "/.git", "/.env", "/.svn", "/.DS_Store", "/WEB-INF", "/META-INF",

            // WordPress 扫描（常见的通用扫描路径）
            "/wp-admin", "/wp-login", "/xmlrpc.php",

            // 管理后台
            "/admin", "/console", "/manager", "/jolokia"
    );

    /**
     * 需要拦截的恶意 User-Agent 特征
     *
     * <p>包含常见的安全扫描工具、爬虫、爆破工具等</p>
     */
    private static final List<String> BLOCKED_UA_PATTERNS = List.of(
            // SQL 注入工具
            "sqlmap",

            // Web 扫描器
            "nikto", "nmap", "masscan", "dirbuster", "gobuster", "wfuzz",

            // 渗透测试工具
            "burpsuite", "burp suite", "zap", "w3af", "acunetix", "nessus", "openvas",

            // CMS 扫描工具
            "wpscan",

            // 暴力破解工具
            "hydra", "medusa", "ncrack",

            // 其他工具
            "crawler", "spider", "bot", "scanner"
    );

    /**
     * 过滤器执行逻辑
     *
     * @param exchange 当前请求交换对象
     * @param chain 过滤器链
     * @return Mono<Void> 异步完成信号
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath().toLowerCase();
        String userAgent = request.getHeaders().getFirst("User-Agent");
        String clientIp = getClientIp(request);

        // 检查敏感路径访问
        for (String blocked : BLOCKED_PATHS) {
            if (path.startsWith(blocked) || path.contains(blocked)) {
                log.warn("[RequestBlock] 拦截敏感路径访问 - IP: {}, Path: {}, UA: {}",
                        clientIp, request.getURI().getPath(), userAgent);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }

        // 检查恶意 User-Agent
        if (userAgent != null) {
            String lowerUA = userAgent.toLowerCase();
            for (String pattern : BLOCKED_UA_PATTERNS) {
                if (lowerUA.contains(pattern)) {
                    log.warn("[RequestBlock] 拦截恶意扫描器 - IP: {}, Path: {}, UA: {}",
                            clientIp, request.getURI().getPath(), userAgent);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
        }

        // 正常请求，放行到下一个过滤器（AuthGlobalFilter）
        return chain.filter(exchange);
    }

    /**
     * 获取客户端真实 IP 地址
     *
     * <p>按优先级从以下请求头获取：</p>
     * <ol>
     *   <li>X-Forwarded-For（代理服务器转发）</li>
     *   <li>X-Real-IP（Nginx 等反向代理设置）</li>
     *   <li>RemoteAddress（直连 IP）</li>
     * </ol>
     *
     * @param request 当前请求
     * @return 客户端 IP 地址，无法获取时返回 "unknown"
     */
    private String getClientIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddress() != null
                    ? request.getRemoteAddress().getAddress().getHostAddress()
                    : "unknown";
        }
        // X-Forwarded-For 可能包含多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 过滤器执行顺序
     *
     * <p>设置为 -2，确保在 AuthGlobalFilter（-1）之前执行</p>
     * <p>恶意请求在鉴权之前就被拦截，节省资源</p>
     *
     * @return -2
     */
    @Override
    public int getOrder() {
        return -2;
    }
}