package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hj.mall.order.entity.RefundInfo;
import com.hj.mall.order.mapper.RefundInfoMapper;
import com.hj.mall.order.service.RefundInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundInfoServiceImpl implements RefundInfoService {

    private final RefundInfoMapper refundInfoMapper;

    @Override
    public void save(RefundInfo info) {
        info.setCreateTime(LocalDateTime.now());
        info.setRefundStatus(0); // 待退款
        refundInfoMapper.insert(info);
        log.info("[RefundInfoService] 退款信息创建成功, refundSn={}", info.getRefundSn());
    }

    @Override
    public RefundInfo getByOrderSn(String orderSn) {
        LambdaQueryWrapper<RefundInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundInfo::getOrderSn, orderSn);
        return refundInfoMapper.selectOne(wrapper);
    }

    @Override
    public void updateStatus(String refundSn, Integer status) {
        LambdaUpdateWrapper<RefundInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RefundInfo::getRefundSn, refundSn)
                .set(RefundInfo::getRefundStatus, status)
                .set(RefundInfo::getUpdateTime, LocalDateTime.now());
        refundInfoMapper.update(null, wrapper);
        log.info("[RefundInfoService] 退款状态更新成功, refundSn={}, status={}", refundSn, status);
    }
}
