package com.hj.mall.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀商品VO
 */
@Data
public class SeckillSkuVO {

    /** 活动id */
    private Long promotionId;
    /** 活动场次id */
    private Long promotionSessionId;
    /** 商品skuId */
    private Long skuId;
    /** 秒杀价格 */
    private BigDecimal seckillPrice;
    /** 秒杀总量 */
    private Integer seckillCount;
    /** 每人限购数量 */
    private Integer seckillLimit;
    /** 排序 */
    private Integer seckillSort;
    /** 商品标题 */
    private String skuTitle;
    /** 商品图片 */
    private String skuDefaultImg;
    /** 秒杀开始时间 */
    private LocalDateTime startTime;
    /** 秒杀结束时间 */
    private LocalDateTime endTime;
    /** 秒杀随机码（防止脚本刷接口） */
    private String randomCode;
}
