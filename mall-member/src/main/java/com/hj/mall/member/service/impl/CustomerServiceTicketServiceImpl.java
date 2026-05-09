package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.member.entity.CustomerServiceTicket;
import com.hj.mall.member.mapper.CustomerServiceTicketMapper;
import com.hj.mall.member.service.CustomerServiceTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客服工单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceTicketServiceImpl implements CustomerServiceTicketService {

    private final CustomerServiceTicketMapper ticketMapper;

    @Override
    public IPage<CustomerServiceTicket> listPage(Page<CustomerServiceTicket> page, Long memberId, Integer status) {
        log.info("[CustomerServiceTicketService] 分页查询客服工单, page={}, memberId={}, status={}",
                 page.getCurrent(), memberId, status);

        LambdaQueryWrapper<CustomerServiceTicket> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(CustomerServiceTicket::getMemberId, memberId);
        }
        if (status != null) {
            wrapper.eq(CustomerServiceTicket::getStatus, status);
        }
        wrapper.orderByDesc(CustomerServiceTicket::getCreateTime);

        return ticketMapper.selectPage(page, wrapper);
    }

    @Override
    public CustomerServiceTicket getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        CustomerServiceTicket ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return ticket;
    }

    @Override
    public void save(CustomerServiceTicket ticket) {
        log.info("[CustomerServiceTicketService] 保存客服工单, memberId={}, title={}",
                 ticket.getMemberId(), ticket.getTitle());

        // 新工单默认状态为待处理
        if (ticket.getStatus() == null) {
            ticket.setStatus(0);
        }

        ticketMapper.insert(ticket);
        log.info("[CustomerServiceTicketService] 客服工单保存成功, id={}", ticket.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status, String handler) {
        log.info("[CustomerServiceTicketService] 更新工单状态, id={}, status={}, handler={}", id, status, handler);

        if (id == null || status == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        CustomerServiceTicket existTicket = ticketMapper.selectById(id);
        if (existTicket == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        CustomerServiceTicket update = new CustomerServiceTicket();
        update.setId(id);
        update.setStatus(status);
        if (StringUtils.isNotBlank(handler)) {
            update.setHandler(handler);
        }

        ticketMapper.updateById(update);
        log.info("[CustomerServiceTicketService] 工单状态更新成功, id={}", id);
    }
}
