package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员登录日志实体
 */
@Data
@TableName("ums_member_login_log")
public class MemberLoginLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 登录类型：password->密码登录；sms->短信登录；wechat->微信登录等
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
     * 用户代理（User-Agent）
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
     * 状态：0->失败；1->成功
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
