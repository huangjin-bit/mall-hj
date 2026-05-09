package com.hj.mall.member.service;

import com.hj.mall.member.entity.CustomerServiceMessage;

import java.util.List;

/**
 * 客服消息服务接口
 */
public interface CustomerServiceMessageService {

    /**
     * 查询工单的所有消息
     * @param ticketId 工单ID
     * @return 消息列表
     */
    List<CustomerServiceMessage> listByTicketId(Long ticketId);

    /**
     * 保存消息
     * @param message 消息内容
     */
    void save(CustomerServiceMessage message);
}
