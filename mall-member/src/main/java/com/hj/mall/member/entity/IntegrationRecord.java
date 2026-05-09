package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分记录实体
 */
@Data
@TableName("ums_integration_record")
public class IntegrationRecord {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 积分变化量（正数为增加，负数为减少）
     */
    private Integer changeAmount;

    /**
     * 变化后积分余额
     */
    private Integer currentAmount;

    /**
     * 来源类型：order->订单；comment->评价；sign->签到；system->系统调整等
     */
    private String sourceType;

    /**
     * 来源ID（如订单ID）
     */
    private Long sourceId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
