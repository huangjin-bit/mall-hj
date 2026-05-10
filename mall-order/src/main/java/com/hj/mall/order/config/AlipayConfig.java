package com.hj.mall.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝配置
 * 从配置文件读取支付宝相关配置信息
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {

    /** 应用ID (沙盒) */
    private String appId;

    /** 应用私钥 */
    private String appPrivateKey;

    /** 支付宝公钥 */
    private String alipayPublicKey;

    /** 支付宝网关 (沙盒) */
    private String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    /** 同步回调地址 */
    private String returnUrl;

    /** 异步通知地址 */
    private String notifyUrl;

    /** 字符集 */
    private String charset = "UTF-8";

    /** 签名类型 */
    private String signType = "RSA2";

    /** 格式 */
    private String format = "json";
}
