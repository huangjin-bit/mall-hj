package com.hj.mall.common.config;

import com.hj.mall.common.filter.UserContextFilter;
import com.hj.mall.common.utils.FeignRequestInterceptor;
import com.hj.mall.common.utils.HeaderSigner;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MallCommonAutoConfiguration {

    @Value("${mall.security.sign-key:}")
    private String signKey;

    @Bean
    @ConditionalOnMissingBean
    public HeaderSigner headerSigner() {
        return new HeaderSigner(signKey.isEmpty() ? "default-dev-key" : signKey);
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
