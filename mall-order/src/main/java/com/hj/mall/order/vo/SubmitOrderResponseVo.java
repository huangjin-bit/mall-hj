package com.hj.mall.order.vo;

import com.hj.mall.order.entity.Order;
import lombok.Data;

/**
 * 提交订单响应VO
 */
@Data
public class SubmitOrderResponseVo {

    private Order order;

    /** 0-成功 */
    private Integer code;
}
