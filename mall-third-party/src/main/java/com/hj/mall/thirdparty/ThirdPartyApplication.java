package com.hj.mall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 第三方服务启动类
 * 提供 MinIO 文件上传、对象存储等功能
 */
@SpringBootApplication(scanBasePackages = {"com.hj.mall.thirdparty", "com.hj.mall.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hj.mall.thirdparty.feign")
public class ThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyApplication.class, args);
    }
}
