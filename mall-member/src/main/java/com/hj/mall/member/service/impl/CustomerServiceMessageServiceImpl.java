package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.member.entity.CustomerServiceMessage;
import com.hj.mall.member.mapper.CustomerServiceMessageMapper;
import com.hj.mall.member.service.CustomerServiceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客服消息服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceMessageServiceImpl implements CustomerServiceMessageService {

    private final CustomerServiceMessageMapper messageMapper;

    @Override
    public List<CustomerServiceMessage> listByTicketId(Long ticketId) {
        log.info("[CustomerServiceMessageService] 查询工单消息列表, ticketId={}", ticketId);

        if (ticketId == null) {
            throw new IllegalArgumentException("工单ID不能为空");
        }

        LambdaQueryWrapper<CustomerServiceMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerServiceMessage::getTicketId, ticketId)
               .orderByAsc(CustomerServiceMessage::getCreateTime);

        return messageMapper.selectList(wrapper);
    }

    @Override
    public void save(CustomerServiceMessage message) {
        log.info("[CustomerServiceMessageService] 保存客服消息, ticketId={}, senderType={}",
                 message.getTicketId(), message.getSenderType());

        messageMapper.insert(message);
        log.info("[CustomerServiceMessageService] 客服消息保存成功, id={}", message.getId());
    }
}
