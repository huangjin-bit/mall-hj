package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页广告实体
 */
@Data
@TableName("sms_home_adv")
public class HomeAdv {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 广告名称
     */
    private String name;

    /**
     * 广告图片
     */
    private String pic;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0->禁用；1->启用
     */
    private Integer status;

    /**
     * 类型：0->PC；1->APP
     */
    private Integer type;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
