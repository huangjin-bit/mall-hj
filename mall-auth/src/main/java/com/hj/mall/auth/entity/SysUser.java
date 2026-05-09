package com.hj.mall.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 系统用户实体
 */
@Data
@TableName("sys_user")
public class SysUser {

    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码（BCrypt加密）
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别：0-男 1-女 2-未知
     */
    private Integer gender;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 帐号状态：0-禁用 1-正常
     */
    private Integer status;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 用户角色（非数据库字段）
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 用户权限标识集合（非数据库字段）
     */
    @TableField(exist = false)
    private Set<String> permissions;
}
