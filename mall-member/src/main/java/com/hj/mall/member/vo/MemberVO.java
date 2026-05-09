package com.hj.mall.member.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 会员视图对象
 * 用于返回会员信息给前端，排除敏感字段（如密码）
 */
@Data
public class MemberVO {

    /** 会员ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像URL */
    private String avatar;

    /** 性别：0-未知，1-男，2-女 */
    private Integer gender;

    /** 生日 */
    private LocalDate birthday;

    /** 积分 */
    private Integer integration;

    /** 成长值 */
    private Integer growth;

    /** 会员等级ID */
    private Long levelId;

    /** 会员等级名称（来自 member_level 表） */
    private String levelName;

    /** 状态：0-禁用，1-正常 */
    private Integer status;
}
