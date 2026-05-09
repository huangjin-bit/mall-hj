package com.hj.mall.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员登录日志
 * 记录会员登录信息用于安全审计
 */
@Data
@TableName("ums_member_login_log")
public class MemberLoginLog {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 登录类型：USERNAME-用户名密码, PHONE-手机验证码, WECHAT-微信, QQ-QQ
     */
    private String loginType;

    /**
     * 登录IP
     */
    private String ip;

    /**
     * 登录城市
     */
    private String city;

    /**
     * 用户代理（浏览器信息）
     */
    private String userAgent;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 登录状态：0-失败 1-成功
     */
    private Integer status;

    /**
     * 备注（失败原因等）
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
