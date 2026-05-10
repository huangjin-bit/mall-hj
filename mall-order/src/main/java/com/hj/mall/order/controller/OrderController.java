package com.hj.mall.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.common.result.Result;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.service.OrderService;
import com.hj.mall.order.vo.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ==================== 统计相关接口 ====================

    /**
     * 获取已完成订单总数
     */
    @GetMapping("/count")
    public Result<Long> getOrderCount() {
        return Result.ok(orderService.getOrderCount());
    }

    /**
     * 获取今日销售额（已支付/已发货/已完成订单）
     */
    @GetMapping("/today-sales")
    public Result<java.math.BigDecimal> getTodaySales() {
        return Result.ok(orderService.getTodaySales());
    }

    /**
     * 销售趋势（近7天/近1个月/近1年）
     */
    @GetMapping("/sales-trend")
    public Result<Map<String, Object>> getSalesTrend(
            @RequestParam(value = "range", defaultValue = "7d") String range) {
        return Result.ok(orderService.getSalesTrend(range));
    }

    // ==================== 用户订单流程接口 ====================

    /**
     * 获取确认订单页数据（地址列表、购物车选中项、库存、积分、防重令牌）
     */
    @GetMapping("/confirm")
    public Result<OrderConfirmVo> confirmOrder() {
        // TODO: 从UserContext获取当前登录用户ID
        Long memberId = 1L; // 临时硬编码
        OrderConfirmVo confirmVo = orderService.confirmOrder(memberId);
        return Result.ok(confirmVo);
    }

    /**
     * 提交订单（验令牌、验价、创建订单、锁库存、发MQ延迟关单）
     */
    @PostMapping("/submit")
    public Result<Order> submitOrder(@Valid @RequestBody OrderSubmitVo vo) {
        // TODO: 从UserContext获取当前登录用户ID
        Long memberId = 1L; // 临时硬编码
        SubmitOrderResponseVo response = orderService.submitOrder(memberId, vo);
        if (response.getCode() == 0) {
            return Result.ok(response.getOrder());
        }
        return Result.error(response.getCode(), "下单失败");
    }

    /**
     * 查询当前用户订单列表（包含订单项）
     */
    @GetMapping("/list-with-items")
    public Result<List<OrderWithItemsVo>> listMyOrders(
            @RequestParam(required = false) Integer status) {
        // TODO: 从UserContext获取当前登录用户ID
        Long memberId = 1L; // 临时硬编码
        return Result.ok(orderService.listWithItems(memberId, status));
    }

    /**
     * 根据订单号查询订单详情
     */
    @GetMapping("/sn/{orderSn}")
    public Result<OrderWithItemsVo> getOrderBySn(@PathVariable String orderSn) {
        // TODO: 从UserContext获取当前登录用户ID
        Long memberId = 1L; // 临时硬编码
        OrderWithItemsVo order = orderService.getOrderDetail(orderSn, memberId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.ok(order);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderSn}")
    public Result<Void> cancelOrder(@PathVariable String orderSn) {
        // TODO: 从UserContext获取当前登录用户ID
        Long memberId = 1L; // 临时硬编码
        orderService.cancelOrder(orderSn, memberId);
        return Result.ok();
    }

    /**
     * 确认收货
     */
    @PostMapping("/receive/{orderSn}")
    public Result<Void> confirmReceive(@PathVariable String orderSn) {
        // TODO: 从UserContext获取当前登录用户ID
        Long memberId = 1L; // 临时硬编码
        orderService.confirmReceive(orderSn, memberId);
        return Result.ok();
    }

    /**
     * 支付回调（简化版）
     */
    @PostMapping("/pay/notify")
    public Result<Void> payNotify(
            @RequestParam String orderSn,
            @RequestParam(defaultValue = "1") Integer payType) {
        orderService.handlePayResult(orderSn, payType);
        return Result.ok();
    }

    // ==================== 管理端接口 ====================

    /**
     * 分页查询订单列表
     */
    @GetMapping("/list")
    public Result<IPage<Order>> list(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String key,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) Long memberId) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> p =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        return Result.ok(orderService.listPage(p, key, status, memberId));
    }

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return Result.ok(orderService.getById(id));
    }

    /**
     * 根据订单号查询订单
     */
    @GetMapping("/sn/{orderSn}/admin")
    public Result<Order> getByOrderSn(@PathVariable String orderSn) {
        return Result.ok(orderService.getByOrderSn(orderSn));
    }

    /**
     * 查询订单项列表
     */
    @GetMapping("/{orderId}/items")
    public Result<List<OrderItem>> listOrderItems(@PathVariable Long orderId) {
        return Result.ok(orderService.listOrderItems(orderId));
    }

    /**
     * 创建订单（管理端）
     */
    @PostMapping
    public Result<Void> save(@RequestBody Order order) {
        orderService.save(order);
        return Result.ok();
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                      @RequestParam Integer status,
                                      @RequestParam(required = false) String note,
                                      @RequestParam(required = false) Long operateBy) {
        orderService.updateStatus(id, status, note, operateBy);
        return Result.ok();
    }

    /**
     * 批量删除订单
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        orderService.removeBatch(ids);
        return Result.ok();
    }
}
