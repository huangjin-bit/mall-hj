package com.hj.mall.search.constant;

/**
 * Elasticsearch相关常量
 */
public class EsConstant {

    /**
     * 商品数据在ES中的索引名
     */
    public static final String PRODUCT_INDEX = "mall_product";

    /**
     * 搜索结果每页默认大小
     */
    public static final int PRODUCT_PAGE_SIZE = 16;

    /**
     * 搜索关键字高亮前缀标签
     */
    public static final String HIGHLIGHT_PRE_TAG = "<span style='color:red'>";

    /**
     * 搜索关键字高亮后缀标签
     */
    public static final String HIGHLIGHT_POST_TAG = "</span>";
}
