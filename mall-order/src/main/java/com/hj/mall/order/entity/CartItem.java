package com.hj.mall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("oms_cart_item")
public class CartItem {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long memberId;

    private Long skuId;

    private Long spuId;

    private String skuName;

    private String skuImg;

    private BigDecimal skuPrice;

    private Integer quantity;

    private Integer isChecked;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
