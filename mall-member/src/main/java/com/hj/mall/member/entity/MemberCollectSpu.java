package com.hj.mall.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员收藏商品实体
 */
@Data
@TableName("ums_member_collect_spu")
public class MemberCollectSpu {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 商品SPU ID
     */
    private Long spuId;

    /**
     * 商品SPU名称
     */
    private String spuName;

    /**
     * 商品SPU图片
     */
    private String spuImg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
