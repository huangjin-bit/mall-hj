package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页专题实体
 */
@Data
@TableName("sms_home_subject")
public class HomeSubject {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 专题名称
     */
    private String name;

    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图片
     */
    private String img;

    /**
     * 状态：0->禁用；1->启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
