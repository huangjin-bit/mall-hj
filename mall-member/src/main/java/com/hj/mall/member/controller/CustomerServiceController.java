package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.CustomerServiceMessage;
import com.hj.mall.member.entity.CustomerServiceTicket;
import com.hj.mall.member.service.CustomerServiceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客服工单控制器
 */
@RestController
@RequestMapping("/customer-service")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService service;

    /**
     * 分页查询工单列表
     */
    @GetMapping("/ticket/list")
    public Result<IPage<CustomerServiceTicket>> listTicket(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Integer status) {
        IPage<CustomerServiceTicket> result = service.listTicketPage(new Page<>(page, size), memberId, status);
        return Result.ok(result);
    }

    /**
     * 查询工单详情
     */
    @GetMapping("/ticket/{id}")
    public Result<CustomerServiceTicket> getTicket(@PathVariable Long id) {
        CustomerServiceTicket ticket = service.getTicketById(id);
        return Result.ok(ticket);
    }

    /**
     * 创建工单
     */
    @PostMapping("/ticket")
    public Result<Void> createTicket(@RequestBody CustomerServiceTicket ticket) {
        service.createTicket(ticket);
        return Result.ok();
    }

    /**
     * 更新工单状态
     */
    @PutMapping("/ticket/{id}/status")
    public Result<Void> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String handler) {
        service.updateTicketStatus(id, status, handler);
        return Result.ok();
    }

    /**
     * 查询工单消息列表
     */
    @GetMapping("/ticket/{ticketId}/messages")
    public Result<List<CustomerServiceMessage>> listMessages(@PathVariable Long ticketId) {
        List<CustomerServiceMessage> messages = service.listMessages(ticketId);
        return Result.ok(messages);
    }

    /**
     * 发送工单消息
     */
    @PostMapping("/ticket/{ticketId}/message")
    public Result<Void> sendMessage(
            @PathVariable Long ticketId,
            @RequestBody CustomerServiceMessage message) {
        service.sendMessage(ticketId, message);
        return Result.ok();
    }
}
