package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.CustomerServiceTicket;

/**
 * 客服工单服务接口
 */
public interface CustomerServiceTicketService {

    /**
     * 分页查询客服工单
     * @param page 分页参数
     * @param memberId 会员ID（可选）
     * @param status 工单状态（可选）
     * @return 分页结果
     */
    IPage<CustomerServiceTicket> listPage(Page<CustomerServiceTicket> page, Long memberId, Integer status);

    /**
     * 根据ID查询工单
     * @param id 工单ID
     * @return 工单信息
     */
    CustomerServiceTicket getById(Long id);

    /**
     * 保存工单
     * @param ticket 工单信息
     */
    void save(CustomerServiceTicket ticket);

    /**
     * 更新工单状态
     * @param id 工单ID
     * @param status 新状态
     * @param handler 处理人
     */
    void updateStatus(Long id, Integer status, String handler);
}
