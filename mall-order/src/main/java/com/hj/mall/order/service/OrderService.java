package com.hj.mall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.vo.OrderConfirmVo;
import com.hj.mall.order.vo.OrderSubmitVo;
import com.hj.mall.order.vo.OrderWithItemsVo;
import com.hj.mall.order.vo.SubmitOrderResponseVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {

    // ==================== 基础CRUD ====================

    IPage<Order> listPage(Page<Order> page, String key, Integer status, Long memberId);

    Order getById(Long id);

    Order getByOrderSn(String orderSn);

    List<OrderItem> listOrderItems(Long orderId);

    void save(Order order);

    void updateStatus(Long orderId, Integer status, String note, Long operateBy);

    void removeBatch(List<Long> ids);

    // ==================== 统计相关 ====================

    /**
     * 获取已完成订单总数
     */
    Long getOrderCount();

    /**
     * 获取今日销售额
     */
    BigDecimal getTodaySales();

    /**
     * 获取销售趋势
     * @param range 7d=近7天, 30d=近1个月, 1y=近1年
     */
    Map<String, Object> getSalesTrend(String range);

    // ==================== 订单流程 ====================

    /**
     * 获取确认订单页数据
     */
    OrderConfirmVo confirmOrder(Long memberId);

    /**
     * 提交订单
     */
    SubmitOrderResponseVo submitOrder(Long memberId, OrderSubmitVo vo);

    /**
     * 查询用户订单列表（含订单项）
     */
    List<OrderWithItemsVo> listWithItems(Long memberId, Integer status);

    /**
     * 查询订单详情
     */
    OrderWithItemsVo getOrderDetail(String orderSn, Long memberId);

    /**
     * 取消订单
     */
    void cancelOrder(String orderSn, Long memberId);

    /**
     * 确认收货
     */
    void confirmReceive(String orderSn, Long memberId);

    /**
     * 处理支付结果
     */
    void handlePayResult(String orderSn, Integer payType);

    /**
     * 关闭订单（定时任务/MQ调用）
     */
    void closeOrder(Order order);
}
