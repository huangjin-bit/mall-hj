package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.GrowthRecord;
import com.hj.mall.member.service.GrowthRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 成长值记录控制器
 */
@RestController
@RequestMapping("/growth-record")
@RequiredArgsConstructor
public class GrowthRecordController {

    private final GrowthRecordService recordService;

    /**
     * 分页查询成长值记录
     */
    @GetMapping("/list")
    public Result<IPage<GrowthRecord>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Long memberId) {
        IPage<GrowthRecord> result = recordService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }

    /**
     * 保存成长值记录
     */
    @PostMapping
    public Result<Void> save(@RequestBody GrowthRecord record) {
        recordService.save(record);
        return Result.ok();
    }
}
