package com.hj.mall.auth.vo;

import com.hj.mall.auth.entity.SysDept;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 系统部门VO（带子部门）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysDeptVO implements Serializable {

    /**
     * 部门ID
     */
    private Long id;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态：0-禁用 1-正常
     */
    private Integer status;

    /**
     * 子部门
     */
    private List<SysDeptVO> children;

    /**
     * 从 SysDept 转换
     */
    public static SysDeptVO fromSysDept(SysDept dept) {
        return SysDeptVO.builder()
            .id(dept.getId())
            .parentId(dept.getParentId())
            .deptName(dept.getDeptName())
            .sort(dept.getSort())
            .leader(dept.getLeader())
            .phone(dept.getPhone())
            .email(dept.getEmail())
            .status(dept.getStatus())
            .build();
    }
}
