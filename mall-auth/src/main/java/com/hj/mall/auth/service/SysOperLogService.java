package com.hj.mall.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.entity.SysOperLog;

/**
 * 系统操作日志服务接口
 */
public interface SysOperLogService {

    /**
     * 分页查询操作日志
     *
     * @param current   当前页
     * @param size      每页大小
     * @param title     模块标题（可选）
     * @param operType  业务类型（可选）
     * @return 操作日志分页列表
     */
    IPage<SysOperLog> listPage(long current, long size, String title, Integer operType);

    /**
     * 保存操作日志
     *
     * @param operLog   操作日志
     * @return 保存结果
     */
    boolean save(SysOperLog operLog);
}
