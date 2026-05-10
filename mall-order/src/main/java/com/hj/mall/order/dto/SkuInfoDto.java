package com.hj.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * SKU信息DTO - 用于Feign远程调用
 * 避免直接依赖mall-product模块的SkuInfo实体
 */
@Data
public class SkuInfoDto {

    /** sku_id */
    private Long id;

    /** spu_id */
    private Long spuId;

    /** sku名称 */
    private String skuName;

    /** 购买价格 */
    private BigDecimal price;

    /** 库存 */
    private Integer stock;

    /** 图片列表 */
    private String skuImage;

    /** 检索属性 */
    private String skuAttr;

    /** 品牌 */
    private Long brandId;

    /** 分类 */
    private Long categoryId;

    /** 销量 */
    private Long saleCount;

    /** 创建时间 */
    private String createTime;
}