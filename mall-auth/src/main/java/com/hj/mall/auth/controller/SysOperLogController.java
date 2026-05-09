package com.hj.mall.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hj.mall.auth.entity.SysOperLog;
import com.hj.mall.auth.service.SysOperLogService;
import com.hj.mall.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 系统操作日志控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/oper-log")
@RequiredArgsConstructor
public class SysOperLogController {

    private final SysOperLogService sysOperLogService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/list")
    public Result<IPage<SysOperLog>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer operType) {

        log.debug("[SysOperLogController] 分页查询操作日志, current={}, size={}", current, size);

        IPage<SysOperLog> page = sysOperLogService.listPage(current, size, title, operType);
        return Result.ok(page);
    }
}
