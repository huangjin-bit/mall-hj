package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员实体
 */
@Data
@TableName("ums_member")
public class Member {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员等级ID
     */
    private Long levelId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别：0->未知；1->男；2->女
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 积分
     */
    private Integer integration;

    /**
     * 成长值
     */
    private Integer growth;

    /**
     * 年龄组
     */
    private String ageGroup;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 价格敏感度：1->敏感；2->一般；3->不敏感
     */
    private Integer priceSensitivity;

    /**
     * 偏好品类
     */
    private String favoriteCategories;

    /**
     * 购物偏好
     */
    private String shoppingPreference;

    /**
     * 状态：0->禁用；1->启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
