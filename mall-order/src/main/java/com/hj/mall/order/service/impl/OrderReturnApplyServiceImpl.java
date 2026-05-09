package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.order.entity.OrderReturnApply;
import com.hj.mall.order.mapper.OrderReturnApplyMapper;
import com.hj.mall.order.service.OrderReturnApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderReturnApplyServiceImpl implements OrderReturnApplyService {

    private final OrderReturnApplyMapper returnApplyMapper;

    @Override
    public IPage<OrderReturnApply> listPage(Page<OrderReturnApply> page, Long memberId, Integer status) {
        LambdaQueryWrapper<OrderReturnApply> wrapper = new LambdaQueryWrapper<>();

        if (memberId != null) {
            wrapper.eq(OrderReturnApply::getMemberId, memberId);
        }

        if (status != null) {
            wrapper.eq(OrderReturnApply::getStatus, status);
        }

        wrapper.orderByDesc(OrderReturnApply::getCreateTime);

        return returnApplyMapper.selectPage(page, wrapper);
    }

    @Override
    public void save(OrderReturnApply apply) {
        apply.setCreateTime(LocalDateTime.now());
        apply.setStatus(0); // 待处理
        returnApplyMapper.insert(apply);
        log.info("[OrderReturnApplyService] 退货申请创建成功, orderSn={}", apply.getOrderSn());
    }

    @Override
    public void handle(Long id, Integer status, Long handleBy, String handleNote) {
        OrderReturnApply apply = returnApplyMapper.selectById(id);
        if (apply == null) {
            throw new RuntimeException("退货申请不存在");
        }

        apply.setStatus(status);
        apply.setHandleBy(handleBy);
        apply.setHandleNote(handleNote);
        apply.setHandleTime(LocalDateTime.now());
        returnApplyMapper.updateById(apply);

        log.info("[OrderReturnApplyService] 退货申请处理成功, id={}, status={}", id, status);
    }
}
