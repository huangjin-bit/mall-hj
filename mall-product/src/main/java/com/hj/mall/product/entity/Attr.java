package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_attr")
public class Attr {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    /** 1基本属性 2销售属性 */
    private Integer attrType;

    /** 0手动输入 1单选 2多选 */
    private Integer valueType;

    private String icon;

    private Integer sort;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
