package com.hj.mall.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 前端路由VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterVO implements Serializable {

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 路由元信息
     */
    private Meta meta;

    /**
     * 子路由
     */
    private List<RouterVO> children;

    /**
     * 路由元信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta implements Serializable {

        /**
         * 菜单标题
         */
        private String title;

        /**
         * 菜单图标
         */
        private String icon;

        /**
         * 是否缓存：0-不缓存 1-缓存
         */
        private Integer noCache;

        /**
         * 链接地址
         */
        private String link;
    }
}
