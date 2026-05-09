package com.hj.mall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pms_spu_audit_log")
public class SpuAuditLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long spuId;

    /** 1审核通过 2审核拒绝 */
    private Integer status;

    private String reason;

    private Long auditBy;

    private String auditByName;

    private LocalDateTime createTime;
}
