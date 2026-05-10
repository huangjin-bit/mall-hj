package com.hj.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * SPU信息DTO - 用于Feign远程调用
 * 避免直接依赖mall-product模块的SpuInfo实体
 */
@Data
public class SpuInfoDto {

    /** 商品id */
    private Long id;

    /** 商品名称 */
    private String spuName;

    /** 商品描述 */
    private String description;

    /** 分类id */
    private Long catalogId;

    /** 品牌id */
    private Long brandId;

    /** 图片 */
    private String spuImage;

    /** 单位 */
    private String unit;

    /** 数量 */
    private Integer quantity;

    /** 价格 */
    private BigDecimal price;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;
}