package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.CustomerServiceMessage;
import com.hj.mall.member.entity.CustomerServiceTicket;

import java.util.List;

/**
 * 客服工单服务接口
 */
public interface CustomerServiceService {

    /**
     * 分页查询工单列表
     * @param page 分页参数
     * @param memberId 会员ID（可选）
     * @param status 工单状态（可选）
     * @return 分页结果
     */
    IPage<CustomerServiceTicket> listTicketPage(Page<CustomerServiceTicket> page, Long memberId, Integer status);

    /**
     * 查询工单详情
     * @param id 工单ID
     * @return 工单信息
     */
    CustomerServiceTicket getTicketById(Long id);

    /**
     * 创建工单
     * @param ticket 工单信息
     */
    void createTicket(CustomerServiceTicket ticket);

    /**
     * 更新工单状态
     * @param id 工单ID
     * @param status 状态
     * @param handler 处理人（可选）
     */
    void updateTicketStatus(Long id, Integer status, String handler);

    /**
     * 查询工单消息列表
     * @param ticketId 工单ID
     * @return 消息列表
     */
    List<CustomerServiceMessage> listMessages(Long ticketId);

    /**
     * 发送工单消息
     * @param ticketId 工单ID
     * @param message 消息内容
     */
    void sendMessage(Long ticketId, CustomerServiceMessage message);
}
