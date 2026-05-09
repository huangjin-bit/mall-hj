package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SpuAuditLog;
import com.hj.mall.product.service.SpuAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * SPU审核日志管理控制器
 * 记录SPU商品的审核历史操作
 */
@RestController
@RequestMapping("/spu-audit-log")
@RequiredArgsConstructor
public class SpuAuditLogController {

    private final SpuAuditLogService spuAuditLogService;

    /**
     * 分页查询审核日志列表
     * 可选根据spuId过滤特定SPU的审核记录
     */
    @GetMapping("/list")
    public Result<IPage<SpuAuditLog>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) Long spuId) {
        return Result.ok(spuAuditLogService.listPage(new Page<>(page, size), spuId));
    }
}
