package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 秒杀商品提醒实体
 */
@Data
@TableName("sms_seckill_sku_notice")
public class SeckillSkuNotice {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 商品SKU ID
     */
    private Long skuId;

    /**
     * 秒杀场次ID
     */
    private Long sessionId;

    /**
     * 订阅时间
     */
    private LocalDateTime subscribeTime;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 通知类型：0->短信；1->站内信；2->APP推送
     */
    private Integer noticeType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
