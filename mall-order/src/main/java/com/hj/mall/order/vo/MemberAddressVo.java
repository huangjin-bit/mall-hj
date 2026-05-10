package com.hj.mall.order.vo;

import lombok.Data;

/**
 * 会员收货地址VO — 确认订单页展示地址列表
 */
@Data
public class MemberAddressVo {

    private Long id;
    private Long memberId;
    private String name;
    private String phone;
    private String postCode;
    private String province;
    private String city;
    private String region;
    private String detailAddress;
    private String areacode;
    private Integer defaultStatus;
}
