package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_category")
public class Category {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Long parentId;

    private Integer level;

    private String icon;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
