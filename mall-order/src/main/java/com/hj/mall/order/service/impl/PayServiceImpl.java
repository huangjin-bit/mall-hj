package com.hj.mall.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.order.config.AlipayConfig;
import com.hj.mall.order.constant.OrderConstant;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.PaymentInfo;
import com.hj.mall.order.service.OrderService;
import com.hj.mall.order.service.PayService;
import com.hj.mall.order.service.PaymentInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

    private final OrderService orderService;
    private final PaymentInfoService paymentInfoService;
    private final AlipayConfig alipayConfig;

    /**
     * 构建支付宝客户端
     */
    private AlipayClient buildAlipayClient() {
        return new DefaultAlipayClient(
                alipayConfig.getGatewayUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getSignType()
        );
    }

    @Override
    public String createAlipayOrder(String orderSn) {
        log.info("[PayService] 创建支付宝支付订单, orderSn={}", orderSn);

        // 查询订单信息
        Order order = orderService.getByOrderSn(orderSn);
        if (order == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }
        if (order.getStatus() != OrderConstant.OrderStatus.CREATE) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        // 保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.getByOrderSn(orderSn);
        if (paymentInfo == null) {
            paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(orderSn);
            paymentInfo.setOrderId(order.getId());
            paymentInfo.setPayType(OrderConstant.PayType.ALIPAY);
            paymentInfo.setPayAmount(order.getPayAmount());
            paymentInfo.setTotalAmount(order.getTotalAmount());
            paymentInfo.setSubject("商城订单支付-" + orderSn);
            paymentInfo.setStatus(0); // 待支付
            paymentInfoService.save(paymentInfo);
        }

        try {
            AlipayClient alipayClient = buildAlipayClient();
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(alipayConfig.getReturnUrl());
            request.setNotifyUrl(alipayConfig.getNotifyUrl());

            String bizContent = "{\"out_trade_no\":\"" + orderSn + "\"," +
                    "\"total_amount\":\"" + order.getPayAmount().toPlainString() + "\"," +
                    "\"subject\":\"" + paymentInfo.getSubject() + "\"," +
                    "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}";
            request.setBizContent(bizContent);

            String body = alipayClient.pageExecute(request).getBody();
            log.info("[PayService] 支付宝支付订单创建完成, orderSn={}", orderSn);
            return body;
        } catch (AlipayApiException e) {
            log.error("[PayService] 支付宝SDK调用失败: {}", e.getErrMsg(), e);
            throw new BizException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean handleAlipayNotify(Map<String, String> params) {
        log.info("[PayService] 处理支付宝异步通知");

        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType()
            );
            if (!signVerified) {
                log.warn("[PayService] 支付宝签名验证失败");
                return false;
            }
        } catch (AlipayApiException e) {
            log.error("[PayService] 支付宝签名验证异常: {}", e.getErrMsg(), e);
            return false;
        }

        String orderSn = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        String tradeNo = params.get("trade_no");

        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            // 更新支付记录
            paymentInfoService.updateStatus(orderSn, 1, params.toString());
            // 更新支付流水号
            PaymentInfo info = paymentInfoService.getByOrderSn(orderSn);
            if (info != null) {
                info.setAlipayTradeNo(tradeNo);
                info.setPaymentStatus(tradeStatus);
                info.setConfirmTime(LocalDateTime.now());
                info.setUpdateTime(LocalDateTime.now());
                // 更新订单状态
                orderService.handlePayResult(orderSn, OrderConstant.PayType.ALIPAY);
            }
            log.info("[PayService] 支付宝支付处理成功, orderSn={}, tradeNo={}", orderSn, tradeNo);
            return true;
        }

        log.warn("[PayService] 支付宝交易状态异常, tradeStatus={}", tradeStatus);
        return false;
    }
}
