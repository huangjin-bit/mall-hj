package com.hj.mall.auth.vo;

import com.hj.mall.auth.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 用户信息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {

    /**
     * 用户信息
     */
    private SysUser user;

    /**
     * 角色标识列表
     */
    private List<String> roles;

    /**
     * 权限标识集合
     */
    private Set<String> permissions;
}
