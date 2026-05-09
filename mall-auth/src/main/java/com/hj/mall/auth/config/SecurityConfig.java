package com.hj.mall.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * REST API 无状态认证，禁用 CSRF、表单登录、HTTP Basic
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * 配置安全规则
     * 认证服务本身不进行鉴权，由网关统一处理
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（REST API 不需要）
            .csrf(csrf -> csrf.disable())

            // 禁用 HTTP Basic
            .httpBasic(httpBasic -> httpBasic.disable())

            // 禁用表单登录
            .formLogin(formLogin -> formLogin.disable())

            // 无状态会话管理
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 配置授权规则
            .authorizeHttpRequests(auth -> auth
                // 认证相关接口全部允许访问
                .requestMatchers("/auth/**").permitAll()
                // Feign 内部调用接口允许访问
                .requestMatchers("/feign/**").permitAll()
                // 其他请求也允许访问（由网关统一鉴权）
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
