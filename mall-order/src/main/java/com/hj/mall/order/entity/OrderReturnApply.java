package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_order_return_apply")
public class OrderReturnApply {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String orderSn;

    private Long orderId;

    private Long skuId;

    private Long memberId;

    private Integer returnType;

    private String reason;

    private String description;

    private String proofImgs;

    private BigDecimal returnAmount;

    private Integer status;

    private Long handleBy;

    private String handleNote;

    private LocalDateTime handleTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 会员用户名
    private String memberUsername;

    // 退货人姓名
    private String returnName;

    // 退货人电话
    private String returnPhone;

    // SKU图片
    private String skuImg;

    // SKU名称
    private String skuName;

    // SKU品牌
    private String skuBrand;

    // SKU属性值
    private String skuAttrsVals;

    // 退货数量
    private Integer skuCount;

    // SKU价格
    private BigDecimal skuPrice;

    // SKU实际价格
    private BigDecimal skuRealPrice;

    // 凭证图片
    private String descPics;

    // 处理人
    private String handleMan;

    // 收货人
    private String receiveMan;

    // 收货电话
    private String receivePhone;

    // 收货备注
    private String receiveNote;

    // 收货地址
    private String companyAddress;
}
