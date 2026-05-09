package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("wms_purchase")
public class Purchase {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String purchaseSn;

    private Long wareId;

    private Integer status;

    private BigDecimal totalAmount;

    private Long createBy;

    private String createByName;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
