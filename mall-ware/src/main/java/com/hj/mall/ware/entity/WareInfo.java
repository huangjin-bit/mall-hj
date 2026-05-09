package com.hj.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("wms_ware_info")
public class WareInfo {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String address;

    private String province;

    private String city;

    private String district;

    private String phone;

    private Integer isDefault;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
