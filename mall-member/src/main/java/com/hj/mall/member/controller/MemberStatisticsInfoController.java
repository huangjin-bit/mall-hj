package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.MemberStatisticsInfo;
import com.hj.mall.member.service.MemberStatisticsInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员统计信息控制器
 */
@RestController
@RequestMapping("/member-statistics")
@RequiredArgsConstructor
public class MemberStatisticsInfoController {

    private final MemberStatisticsInfoService statisticsInfoService;

    /**
     * 分页查询会员统计信息列表
     */
    @GetMapping("/list")
    public Result<IPage<MemberStatisticsInfo>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long memberId) {
        IPage<MemberStatisticsInfo> result = statisticsInfoService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }

    /**
     * 根据ID查询统计信息
     */
    @GetMapping("/{id}")
    public Result<MemberStatisticsInfo> getById(@PathVariable Long id) {
        MemberStatisticsInfo statisticsInfo = statisticsInfoService.getById(id);
        return Result.ok(statisticsInfo);
    }

    /**
     * 保存会员统计信息
     */
    @PostMapping
    public Result<Void> save(@RequestBody MemberStatisticsInfo statisticsInfo) {
        statisticsInfoService.save(statisticsInfo);
        return Result.ok();
    }

    /**
     * 更新会员统计信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody MemberStatisticsInfo statisticsInfo) {
        statisticsInfoService.update(statisticsInfo);
        return Result.ok();
    }

    /**
     * 删除统计信息
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        statisticsInfoService.delete(id);
        return Result.ok();
    }
}
