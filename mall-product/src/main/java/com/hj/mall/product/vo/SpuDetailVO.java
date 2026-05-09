package com.hj.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpuDetailVO {

    private Long id;

    private String spuName;

    private String spuDescription;

    private Long categoryId;

    private String categoryName;

    private Long brandId;

    private String brandName;

    private String brandLogo;

    private BigDecimal weight;

    private Integer publishStatus;

    private Integer newStatus;

    private Integer recommendStatus;

    /** 富文本详情 */
    private String description;

    /** SPU 图片列表 */
    private List<String> images;

    /** 基本属性列表 */
    private List<AttrItem> baseAttrs;

    /** SKU 列表（含销售属性） */
    private List<SkuItem> skus;

    @Data
    public static class AttrItem {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

    @Data
    public static class SkuItem {
        private Long skuId;
        private String skuName;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private String skuImg;
        private List<SaleAttrItem> saleAttrs;
        private List<String> images;
    }

    @Data
    public static class SaleAttrItem {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
