package com.hj.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员DTO - 用于Feign远程调用
 * 避免直接依赖mall-member模块的Member实体
 */
@Data
public class MemberDto {

    /** 会员id */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 手机号 */
    private String mobile;

    /** 邮箱 */
    private String email;

    /** 头像 */
    private String header;

    /** 性别 */
    private Integer gender;

    /** 生日 */
    private String birthday;

    /** 所在城市 */
    private String city;

    /** 职业 */
    private String job;

    /** 签名 */
    private String sign;

    /** 用户等级 */
    private Integer levelId;

    /** 积分 */
    private Integer integration;

    /** 成长值 */
    private Integer growth;

    /** 默认地址 */
    private Long defaultAddr;

    /** 状态 */
    private Integer status;
}