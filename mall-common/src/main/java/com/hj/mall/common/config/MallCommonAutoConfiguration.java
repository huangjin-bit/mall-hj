package com.hj.mall.common.config;

import com.hj.mall.common.filter.UserContextFilter;
import com.hj.mall.common.utils.FeignRequestInterceptor;
import com.hj.mall.common.utils.HeaderSigner;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MallCommonAutoConfiguration {

    @Value("${mall.security.sign-key:}")
    private String signKey;

    @Bean
    public HeaderSigner headerSigner() {
        if (signKey.isEmpty()) {
            return null; // 未配置密钥则跳过签名校验（开发模式）
        }
        return new HeaderSigner(signKey);
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public UserContextFilter userContextFilter(HeaderSigner signer) {
        return new UserContextFilter(signer);
    }

    @Bean
    @ConditionalOnClass(RequestInterceptor.class)
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
