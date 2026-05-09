package com.hj.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuVO {

    private Long id;

    private Long spuId;

    private String skuName;

    private String skuDesc;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String skuImg;

    private BigDecimal weight;

    private Integer publishStatus;

    /** SKU 图片列表 */
    private List<String> images;

    /** 销售属性 */
    private List<SaleAttrItem> saleAttrs;

    @Data
    public static class SaleAttrItem {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
