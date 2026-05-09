package com.hj.mall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 购物车服务启动类
 * 使用 Redis 存储购物车数据，无数据库依赖
 */
@SpringBootApplication(scanBasePackages = {"com.hj.mall.cart", "com.hj.mall.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class CartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
}
