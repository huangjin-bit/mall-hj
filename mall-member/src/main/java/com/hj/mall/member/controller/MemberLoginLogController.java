package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.MemberLoginLog;
import com.hj.mall.member.service.MemberLoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员登录日志控制器
 */
@RestController
@RequestMapping("/member-login-log")
@RequiredArgsConstructor
public class MemberLoginLogController {

    private final MemberLoginLogService loginLogService;

    /**
     * 分页查询登录日志
     */
    @GetMapping("/list")
    public Result<IPage<MemberLoginLog>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long memberId) {
        IPage<MemberLoginLog> result = loginLogService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }
}
