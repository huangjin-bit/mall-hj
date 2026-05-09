package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.IntegrationRecord;
import com.hj.mall.member.service.IntegrationRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 积分记录控制器
 */
@RestController
@RequestMapping("/integration-record")
@RequiredArgsConstructor
public class IntegrationRecordController {

    private final IntegrationRecordService recordService;

    /**
     * 分页查询积分记录
     */
    @GetMapping("/list")
    public Result<IPage<IntegrationRecord>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Long memberId) {
        IPage<IntegrationRecord> result = recordService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }

    /**
     * 保存积分记录
     */
    @PostMapping
    public Result<Void> save(@RequestBody IntegrationRecord record) {
        recordService.save(record);
        return Result.ok();
    }
}
