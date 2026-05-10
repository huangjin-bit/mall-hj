package com.hj.mall.product.vo;

import com.hj.mall.product.entity.Category;
import com.hj.mall.product.entity.SkuInfo;
import lombok.Data;

import java.util.List;

/**
 * 首页数据VO
 */
@Data
public class HomeIndexVO {

    /**
     * 轮播图列表
     */
    private List<BannerVO> banners;

    /**
     * 专题列表
     */
    private List<SubjectVO> subjects;

    /**
     * 顶级分类列表
     */
    private List<Category> topCategories;

    /**
     * 秒杀商品列表
     */
    private List<SkuInfo> flashSaleProducts;

    /**
     * 推荐商品列表
     */
    private List<SkuInfo> recommendProducts;

    /**
     * 楼层列表
     */
    private List<FloorVO> floors;

    /**
     * 轮播图VO
     */
    @Data
    public static class BannerVO {
        /**
         * ID
         */
        private Long id;

        /**
         * 标题
         */
        private String title;

        /**
         * 图片
         */
        private String pic;

        /**
         * 跳转链接
         */
        private String url;
    }

    /**
     * 楼层VO
     */
    @Data
    public static class FloorVO {
        /**
         * 分类ID
         */
        private Long categoryId;

        /**
         * 分类名称
         */
        private String categoryName;

        /**
         * 商品列表
         */
        private List<SkuInfo> products;
    }

    /**
     * 专题VO
     */
    @Data
    public static class SubjectVO {
        /**
         * ID
         */
        private Long id;

        /**
         * 名称
         */
        private String name;

        /**
         * 标题
         */
        private String title;

        /**
         * 副标题
         */
        private String subTitle;

        /**
         * 图片
         */
        private String img;

        /**
         * 跳转链接
         */
        private String url;
    }
}
