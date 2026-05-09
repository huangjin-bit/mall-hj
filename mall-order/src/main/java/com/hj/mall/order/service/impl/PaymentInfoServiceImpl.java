package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hj.mall.order.entity.PaymentInfo;
import com.hj.mall.order.mapper.PaymentInfoMapper;
import com.hj.mall.order.service.PaymentInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final PaymentInfoMapper paymentInfoMapper;

    @Override
    public PaymentInfo getByOrderSn(String orderSn) {
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOrderSn, orderSn);
        return paymentInfoMapper.selectOne(wrapper);
    }

    @Override
    public void save(PaymentInfo info) {
        info.setCreateTime(LocalDateTime.now());
        paymentInfoMapper.insert(info);
        log.info("[PaymentInfoService] 保存支付信息成功, orderSn={}", info.getOrderSn());
    }

    @Override
    public void updateStatus(String orderSn, Integer status, String callbackContent) {
        LambdaUpdateWrapper<PaymentInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PaymentInfo::getOrderSn, orderSn)
                .set(PaymentInfo::getStatus, status)
                .set(PaymentInfo::getCallbackContent, callbackContent)
                .set(PaymentInfo::getCallbackTime, LocalDateTime.now())
                .set(PaymentInfo::getUpdateTime, LocalDateTime.now());
        paymentInfoMapper.update(null, wrapper);
        log.info("[PaymentInfoService] 更新支付状态成功, orderSn={}, status={}", orderSn, status);
    }
}
