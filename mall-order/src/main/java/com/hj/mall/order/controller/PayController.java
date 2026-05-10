package com.hj.mall.order.controller;

import com.hj.mall.order.service.PayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付控制器
 *
 * 提供：
 * 1. 支付宝网页支付（返回HTML表单自动跳转）
 * 2. 支付宝异步通知回调
 */
@Slf4j
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 支付宝网页支付 — 返回HTML表单
     * 前端拿到后直接渲染，浏览器会自动跳转到支付宝支付页面
     */
    @GetMapping(value = "/alipay/{orderSn}", produces = "text/html;charset=UTF-8")
    public String alipay(@PathVariable String orderSn) {
        log.info("[PayController] 创建支付宝支付, orderSn={}", orderSn);
        return payService.createAlipayOrder(orderSn);
    }

    /**
     * 支付宝异步通知回调
     * 支付宝服务器POST调用此接口通知支付结果
     */
    @PostMapping("/alipay/notify")
    public String alipayNotify(HttpServletRequest request) {
        log.info("[PayController] 收到支付宝异步通知");

        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            StringBuilder valueStr = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                valueStr.append(i == values.length - 1 ? values[i] : values[i] + ",");
            }
            params.put(name, valueStr.toString());
        }

        boolean success = payService.handleAlipayNotify(params);
        return success ? "success" : "failure";
    }
}
