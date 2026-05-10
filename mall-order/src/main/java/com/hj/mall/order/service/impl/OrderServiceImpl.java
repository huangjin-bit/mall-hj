package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.order.constant.OrderConstant;
import com.hj.mall.order.dto.OrderTo;
import com.hj.mall.order.entity.Order;
import com.hj.mall.order.entity.OrderItem;
import com.hj.mall.order.entity.OrderOperateHistory;
import com.hj.mall.order.feign.CartFeignClient;
import com.hj.mall.order.feign.MemberFeignClient;
import com.hj.mall.order.feign.ProductFeignClient;
import com.hj.mall.order.feign.WareFeignClient;
import com.hj.mall.order.mapper.OrderMapper;
import com.hj.mall.order.service.OrderItemService;
import com.hj.mall.order.service.OrderOperateHistoryService;
import com.hj.mall.order.service.OrderService;
import com.hj.mall.order.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final OrderOperateHistoryService historyService;
    private final CartFeignClient cartFeignClient;
    private final MemberFeignClient memberFeignClient;
    private final ProductFeignClient productFeignClient;
    private final WareFeignClient wareFeignClient;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate redisTemplate;

    // ==================== 基础CRUD ====================

    @Override
    public IPage<Order> listPage(Page<Order> page, String key, Integer status, Long memberId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(key)) {
            wrapper.like(Order::getOrderSn, key)
                    .or()
                    .like(Order::getReceiverName, key)
                    .or()
                    .like(Order::getReceiverPhone, key);
        }

        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }

        if (memberId != null) {
            wrapper.eq(Order::getMemberId, memberId);
        }

        wrapper.eq(Order::getDeleteStatus, 0)
                .orderByDesc(Order::getCreateTime);

        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public Order getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BizException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public Order getByOrderSn(String orderSn) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderSn, orderSn);
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public List<OrderItem> listOrderItems(Long orderId) {
        return orderItemService.listByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Order order) {
        orderMapper.insert(order);

        // 记录操作历史
        OrderOperateHistory history = new OrderOperateHistory();
        history.setOrderId(order.getId());
        history.setOrderSn(order.getOrderSn());
        history.setOperateType("创建订单");
        history.setNote("订单创建成功");
        history.setOperateBy(order.getMemberId());
        historyService.save(history);

        log.info("[OrderService] 订单创建成功, orderSn={}", order.getOrderSn());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long orderId, Integer status, String note, Long operateBy) {
        Order order = getById(orderId);
        order.setStatus(status);
        orderMapper.updateById(order);

        // 记录操作历史
        OrderOperateHistory history = new OrderOperateHistory();
        history.setOrderId(orderId);
        history.setOrderSn(order.getOrderSn());
        history.setOperateType("状态变更");
        history.setNote(note);
        history.setOperateBy(operateBy);
        historyService.save(history);

        log.info("[OrderService] 订单状态更新成功, orderId={}, status={}", orderId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        // 逻辑删除
        for (Long id : ids) {
            Order order = new Order();
            order.setId(id);
            order.setDeleteStatus(1);
            orderMapper.updateById(order);
        }
        log.info("[OrderService] 订单批量删除成功, ids={}", ids);
    }

    // ==================== 统计相关 ====================

    @Override
    public Long getOrderCount() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, OrderConstant.OrderStatus.RECEIVED)
                .eq(Order::getDeleteStatus, 0);
        return orderMapper.selectCount(wrapper);
    }

    @Override
    public BigDecimal getTodaySales() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getStatus, Arrays.asList(
                        OrderConstant.OrderStatus.PAYED,
                        OrderConstant.OrderStatus.SENDED,
                        OrderConstant.OrderStatus.RECEIVED
                ))
                .eq(Order::getDeleteStatus, 0)
                .ge(Order::getCreateTime, LocalDateTime.now().toLocalDate().atStartOfDay());
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream()
                .map(Order::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<String, Object> getSalesTrend(String range) {
        // TODO: 实现销售趋势查询
        // 根据range参数(7d/30d/1y)返回不同维度的销售数据
        Map<String, Object> result = new HashMap<>();
        result.put("dates", new ArrayList<>());
        result.put("sales", new ArrayList<>());
        result.put("counts", new ArrayList<>());
        return result;
    }

    // ==================== 订单流程 ====================

    @Override
    public OrderConfirmVo confirmOrder(Long memberId) {
        log.info("[OrderService] 开始获取确认订单页数据, memberId={}", memberId);

        OrderConfirmVo vo = new OrderConfirmVo();

        // TODO: 1. 获取收货地址列表
        // var addressResult = memberFeignClient.getAddresses(memberId);
        // vo.setAddresses(addressResult.getData());

        // TODO: 2. 获取购物车选中商品
        // var cartResult = cartFeignClient.getCheckedItems(memberId);
        // List<CartItem> cartItems = cartResult.getData();
        // 转换为OrderItemVo列表
        // vo.setItems(convertToOrderItemVo(cartItems));

        // TODO: 3. 获取库存信息
        // List<Long> skuIds = cartItems.stream().map(CartItem::getSkuId).collect(Collectors.toList());
        // var stockResult = wareFeignClient.getStockBySkuIds(skuIds);
        // vo.setStocks(stockResult.getData());

        // TODO: 4. 获取会员积分
        // var memberResult = memberFeignClient.getMemberById(memberId);
        // vo.setIntegration(memberResult.getData().getIntegration());

        // TODO: 5. 生成防重令牌
        // String token = UUID.randomUUID().toString();
        // redisTemplate.opsForValue().set(OrderConstant.ORDER_TOKEN_PREFIX + memberId, token,
        //         OrderConstant.ORDER_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        // vo.setOrderToken(token);

        // TODO: 6. 获取可用优惠券
        // var couponResult = couponFeignClient.getMemberAvailableCoupons(memberId);
        // vo.setCoupons(couponResult.getData());

        log.info("[OrderService] 获取确认订单页数据完成, memberId={}", memberId);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SubmitOrderResponseVo submitOrder(Long memberId, OrderSubmitVo vo) {
        log.info("[OrderService] 开始提交订单, memberId={}, orderToken={}", memberId, vo.getOrderToken());

        SubmitOrderResponseVo response = new SubmitOrderResponseVo();

        // TODO: 1. 验证防重令牌
        // String token = redisTemplate.opsForValue().get(OrderConstant.ORDER_TOKEN_PREFIX + memberId);
        // if (!vo.getOrderToken().equals(token)) {
        //     response.setCode(1);
        //     return response;
        // }
        // 删除令牌
        // redisTemplate.delete(OrderConstant.ORDER_TOKEN_PREFIX + memberId);

        // TODO: 2. 重新查询购物车数据并验价
        // var cartResult = cartFeignClient.getCheckedItems(memberId);
        // List<CartItem> cartItems = cartResult.getData();
        // BigDecimal total = calculateTotal(cartItems);
        // if (total.compareTo(vo.getPayPrice()) != 0) {
        //     response.setCode(2);
        //     return response;
        // }

        // TODO: 3. 创建订单
        // Order order = buildOrder(memberId, vo, cartItems);
        // orderMapper.insert(order);

        // TODO: 4. 创建订单项
        // List<OrderItem> orderItems = buildOrderItems(order.getId(), cartItems);
        // orderItemService.saveBatch(orderItems);

        // TODO: 5. 锁定库存
        // WareSkuLockVo lockVo = buildLockVo(order.getOrderSn(), orderItems);
        // var lockResult = wareFeignClient.lockStock(lockVo);
        // if (!lockResult.getData()) {
        //     throw new BizException("库存锁定失败");
        // }

        // TODO: 6. 清空购物车选中商品
        // cartFeignClient.clearChecked(memberId);

        // TODO: 7. 发送延迟消息（自动关单）
        // OrderTo orderTo = new OrderTo();
        // orderTo.setId(order.getId());
        // orderTo.setOrderSn(order.getOrderSn());
        // rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", orderTo);

        // 临时响应
        response.setCode(0);
        log.info("[OrderService] 订单提交成功, memberId={}", memberId);
        return response;
    }

    @Override
    public List<OrderWithItemsVo> listWithItems(Long memberId, Integer status) {
        log.info("[OrderService] 查询用户订单列表, memberId={}, status={}", memberId, status);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getMemberId, memberId)
                .eq(Order::getDeleteStatus, 0);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(order -> {
            OrderWithItemsVo vo = new OrderWithItemsVo();
            // TODO: 复制Order属性到vo
            // BeanUtils.copyProperties(order, vo);
            // List<OrderItem> items = orderItemService.listByOrderId(order.getId());
            // vo.setOrderItems(items);
            // vo.setTotalQuantity(items.stream().map(OrderItem::getSkuQuantity).reduce(0, Integer::sum));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderWithItemsVo getOrderDetail(String orderSn, Long memberId) {
        log.info("[OrderService] 查询订单详情, orderSn={}, memberId={}", orderSn, memberId);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderSn, orderSn)
                .eq(Order::getMemberId, memberId)
                .eq(Order::getDeleteStatus, 0);

        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            return null;
        }

        OrderWithItemsVo vo = new OrderWithItemsVo();
        // TODO: 复制Order属性到vo
        // BeanUtils.copyProperties(order, vo);
        // List<OrderItem> items = orderItemService.listByOrderId(order.getId());
        // vo.setOrderItems(items);
        // vo.setTotalQuantity(items.stream().map(OrderItem::getSkuQuantity).reduce(0, Integer::sum));

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderSn, Long memberId) {
        log.info("[OrderService] 取消订单, orderSn={}, memberId={}", orderSn, memberId);

        Order order = getByOrderSn(orderSn);
        if (!order.getMemberId().equals(memberId)) {
            throw new BizException("无权操作此订单");
        }

        if (order.getStatus() != OrderConstant.OrderStatus.CREATE) {
            throw new BizException("订单状态不允许取消");
        }

        order.setStatus(OrderConstant.OrderStatus.CANCELLED);
        orderMapper.updateById(order);

        // TODO: 释放库存
        // wareFeignClient.releaseStock(order.getId());

        // 记录操作历史
        saveOperateHistory(order.getId(), orderSn, "取消订单", "用户取消订单", memberId);

        log.info("[OrderService] 订单取消成功, orderSn={}", orderSn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceive(String orderSn, Long memberId) {
        log.info("[OrderService] 确认收货, orderSn={}, memberId={}", orderSn, memberId);

        Order order = getByOrderSn(orderSn);
        if (!order.getMemberId().equals(memberId)) {
            throw new BizException("无权操作此订单");
        }

        if (order.getStatus() != OrderConstant.OrderStatus.SENDED) {
            throw new BizException("订单状态不允许确认收货");
        }

        order.setStatus(OrderConstant.OrderStatus.RECEIVED);
        orderMapper.updateById(order);

        // 记录操作历史
        saveOperateHistory(order.getId(), orderSn, "确认收货", "用户确认收货", memberId);

        log.info("[OrderService] 确认收货成功, orderSn={}", orderSn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePayResult(String orderSn, Integer payType) {
        log.info("[OrderService] 处理支付结果, orderSn={}, payType={}", orderSn, payType);

        Order order = getByOrderSn(orderSn);
        if (order.getStatus() != OrderConstant.OrderStatus.CREATE) {
            log.warn("[OrderService] 订单状态异常, orderSn={}, status={}", orderSn, order.getStatus());
            return;
        }

        order.setStatus(OrderConstant.OrderStatus.PAYED);
        order.setPayType(payType);
        orderMapper.updateById(order);

        // 记录操作历史
        saveOperateHistory(order.getId(), orderSn, "支付成功", "支付方式:" + payType, order.getMemberId());

        log.info("[OrderService] 支付处理成功, orderSn={}", orderSn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOrder(Order order) {
        log.info("[OrderService] 定时关单, orderId={}", order.getId());

        Order dbOrder = orderMapper.selectById(order.getId());
        if (dbOrder == null) {
            log.warn("[OrderService] 订单不存在, orderId={}", order.getId());
            return;
        }

        if (dbOrder.getStatus() != OrderConstant.OrderStatus.CREATE) {
            log.info("[OrderService] 订单已支付，无需关单, orderId={}", order.getId());
            return;
        }

        dbOrder.setStatus(OrderConstant.OrderStatus.CANCELLED);
        orderMapper.updateById(dbOrder);

        // TODO: 释放库存
        // wareFeignClient.releaseStock(dbOrder.getId());

        // 记录操作历史
        saveOperateHistory(dbOrder.getId(), dbOrder.getOrderSn(), "超时关单", "订单超时未支付自动关闭", null);

        log.info("[OrderService] 定时关单成功, orderId={}", order.getId());
    }

    // ==================== 私有辅助方法 ====================

    private void saveOperateHistory(Long orderId, String orderSn, String operateType, String note, Long operateBy) {
        OrderOperateHistory history = new OrderOperateHistory();
        history.setOrderId(orderId);
        history.setOrderSn(orderSn);
        history.setOperateType(operateType);
        history.setNote(note);
        history.setOperateBy(operateBy);
        historyService.save(history);
    }
}
