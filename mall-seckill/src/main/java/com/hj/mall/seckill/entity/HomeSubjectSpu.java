package com.hj.mall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页专题商品关联实体
 */
@Data
@TableName("sms_home_subject_spu")
public class HomeSubjectSpu {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 专题ID
     */
    private Long subjectId;

    /**
     * 商品SPU ID
     */
    private Long spuId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
