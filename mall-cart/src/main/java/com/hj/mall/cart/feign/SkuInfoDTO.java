package com.hj.mall.cart.feign;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * SKU信息传输对象
 */
@Data
public class SkuInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * SKU 名称
     */
    private String skuName;

    /**
     * SPU 名称
     */
    private String spuName;

    /**
     * SKU 图片
     */
    private String skuDefaultImg;

    /**
     * SKU 价格
     */
    private BigDecimal price;

    /**
     * 分类ID
     */
    private Long catalogId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 销售属性值列表
     */
    private List<String> saleAttrs;
}
