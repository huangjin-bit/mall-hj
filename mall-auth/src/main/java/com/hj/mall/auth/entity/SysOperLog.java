package com.hj.mall.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统操作日志实体
 */
@Data
@TableName("sys_oper_log")
public class SysOperLog {

    /**
     * 日志主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）
     */
    private Integer operType;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String operResult;

    /**
     * 操作人员ID
     */
    private Long operUserId;

    /**
     * 操作人员名称
     */
    private String operUserName;

    /**
     * 操作IP
     */
    private String operIp;

    /**
     * 操作状态：0-失败 1-成功
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 消耗时间（毫秒）
     */
    private Long costTime;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
