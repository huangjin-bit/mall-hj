package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_order")
public class Order {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderSn;

    private Long memberId;

    private Long couponId;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal freightAmount;

    private BigDecimal promotionAmount;

    private BigDecimal couponAmount;

    private Integer payType;

    private Integer status;

    private Integer orderType;

    private String receiverName;

    private String receiverPhone;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverDetailAddress;

    private LocalDateTime payTime;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private LocalDateTime cancelTime;

    private String remark;

    private Integer deleteStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 积分抵扣金额
    private BigDecimal integrationAmount;

    // 优惠金额
    private BigDecimal discountAmount;

    // 订单来源: 1=pc, 2=app
    private Integer sourceType;

    // 物流公司
    private String deliveryCompany;

    // 物流单号
    private String deliverySn;

    // 自动确认天数
    private Integer autoConfirmDay;

    // 可获得积分
    private Integer integration;

    // 可获得成长值
    private Integer growth;

    // 发票类型
    private Integer billType;

    // 发票抬头
    private String billHeader;

    // 发票内容
    private String billContent;

    // 收票人电话
    private String billReceiverPhone;

    // 收票人邮箱
    private String billReceiverEmail;

    // 用户名
    private String memberUsername;

    // 收货人邮编
    private String receiverPostCode;

    // 订单备注
    private String note;

    // 确认收货状态: 0=未确认, 1=已确认
    private Integer confirmStatus;

    // 使用积分
    private Integer useIntegration;

    // 评价时间
    private LocalDateTime commentTime;

    // 修改时间
    private LocalDateTime modifyTime;
}
