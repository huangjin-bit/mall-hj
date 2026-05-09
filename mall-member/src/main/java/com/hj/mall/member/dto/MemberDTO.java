package com.hj.mall.member.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 会员数据传输对象
 * 用于会员注册和更新操作的参数接收
 */
@Data
public class MemberDTO {

    /**
     * 用户名
     * 注册时必填
     */
    @NotBlank(message = "用户名不能为空", groups = {Save.class})
    private String username;

    /**
     * 手机号
     * 注册时必填，格式必须为 11 位手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {Save.class})
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     * 可选，格式必须为有效邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 性别：0-未知，1-男，2-女 */
    private Integer gender;

    /** 生日 */
    private LocalDate birthday;

    /**
     * 密码
     * 仅在注册时必填，长度 6-20 位
     */
    @NotBlank(message = "密码不能为空", groups = {Save.class})
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    /**
     * 分组验证接口 - 保存操作
     */
    public interface Save {}

    /**
     * 分组验证接口 - 更新操作
     */
    public interface Update {}
}
