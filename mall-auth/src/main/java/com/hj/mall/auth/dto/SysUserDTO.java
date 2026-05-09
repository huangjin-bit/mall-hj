package com.hj.mall.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统用户DTO
 */
@Data
public class SysUserDTO implements Serializable {

    /**
     * 用户ID（修改时需要）
     */
    private Long id;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空", groups = {Create.class})
    private String username;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

    /**
     * 密码（新增时必填，修改时可选）
     */
    @NotBlank(message = "密码不能为空", groups = {Create.class})
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
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
     * 角色ID列表
     */
    private Long[] roleIds;

    /**
     * 创建分组
     */
    public interface Create {}
}
