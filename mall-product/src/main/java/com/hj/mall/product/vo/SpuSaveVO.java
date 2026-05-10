package com.hj.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * SPU保存VO
 */
@Data
public class SpuSaveVO {
    /**
     * SPU名称
     */
    private String spuName;

    /**
     * SPU描述
     */
    private String spuDescription;

    /**
     * 分类ID
     */
    private Long catalogId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 发布状态 0下架 1上架
     */
    private Integer publishStatus;

    /**
     * 描述图片列表
     */
    private List<String> decript;

    /**
     * SPU图片列表
     */
    private List<String> images;

    /**
     * 积分边界
     */
    private Bounds bounds;

    /**
     * 基础属性
     */
    private List<BaseAttrs> baseAttrs;

    /**
     * SKU列表
     */
    private List<Skus> skus;

    /**
     * 积分边界
     */
    @Data
    public static class Bounds {
        /**
         * 购买送积分初始值
         */
        private Integer buyBounds;

        /**
         * 购买送积分增长值
         */
        private Integer growBounds;
    }

    /**
     * 基础属性
     */
    @Data
    public static class BaseAttrs {
        /**
         * 属性ID
         */
        private Long attrId;

        /**
         * 属性值
         */
        private String attrValues;

        /**
         * 是否展示在介绍上 0-否 1-是
         */
        private Integer showDesc;
    }

    /**
     * SKU信息
     */
    @Data
    public static class Skus {
        /**
         * SKU图片列表
         */
        private List<String> images;

        /**
         * SKU默认图片
         */
        private String imgUrl;

        /**
         * SKU名称
         */
        private String skuName;

        /**
         * SKU价格
         */
        private BigDecimal price;

        /**
         * SKU库存
         */
        private Integer stock;

        /**
         * 销售属性
         */
        private List<Attr> attr;

        /**
         * 满减信息
         */
        private Integer fullPrice;
        private Integer reducePrice;
        private Integer fullCount;
        private List<Integer> memberPrice;

        /**
         * 销售属性
         */
        @Data
        public static class Attr {
            /**
             * 属性ID
             */
            private Long attrId;

            /**
             * 属性名称
             */
            private String attrName;

            /**
             * 属性值
             */
            private String attrValue;
        }
    }
}
