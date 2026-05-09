package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员统计信息实体
 */
@Data
@TableName("ums_member_statistics_info")
public class MemberStatisticsInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 累计消费金额
     */
    private BigDecimal consumeAmount;

    /**
     * 累计使用优惠券金额
     */
    private BigDecimal couponAmount;

    /**
     * 订单数量
     */
    private Integer orderCount;

    /**
     * 优惠券使用数量
     */
    private Integer couponCount;

    /**
     * 评价数量
     */
    private Integer commentCount;

    /**
     * 退货订单数量
     */
    private Integer returnOrderCount;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 参与活动次数
     */
    private Integer attendCount;

    /**
     * 粉丝数量
     */
    private Integer fansCount;

    /**
     * 收藏商品数量
     */
    private Integer collectProductCount;

    /**
     * 收藏专题数量
     */
    private Integer collectSubjectCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
