package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员等级实体
 */
@Data
@TableName("ums_member_level")
public class MemberLevel {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 所需成长点数
     */
    private Integer growthPoint;

    /**
     * 是否为默认等级：0->不是；1->是
     */
    private Integer defaultStatus;

    /**
     * 免运费标准
     */
    private BigDecimal freeFreight;

    /**
     * 每次评价获取的成长值
     */
    private Integer commentExtraPoint;

    /**
     * 优惠折扣率
     */
    private BigDecimal discount;

    /**
     * 每次评价获取成长值
     */
    private Integer commentGrowthPoint;

    /**
     * 免邮特权：0->无；1->有
     */
    private Integer priviledgeFreeFreight;

    /**
     * 会员价格特权：0->无；1->有
     */
    private Integer priviledgeMemberPrice;

    /**
     * 生日特权：0->无；1->有
     */
    private Integer priviledgeBirthday;

    /**
     * 备注
     */
    private String note;

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
