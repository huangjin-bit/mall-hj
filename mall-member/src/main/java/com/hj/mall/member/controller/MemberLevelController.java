package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.MemberLevel;
import com.hj.mall.member.service.MemberLevelService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员等级控制器
 */
@RestController
@RequestMapping("/member-level")
@RequiredArgsConstructor
public class MemberLevelController {

    private final MemberLevelService memberLevelService;

    /**
     * 分页查询会员等级列表
     */
    @GetMapping("/list")
    public Result<IPage<MemberLevel>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<MemberLevel> result = memberLevelService.page(new Page<>(page, size));
        return Result.ok(result);
    }

    /**
     * 根据ID查询会员等级
     */
    @GetMapping("/info/{id}")
    public Result<MemberLevel> getById(@PathVariable Long id) {
        MemberLevel memberLevel = memberLevelService.getById(id);
        return Result.ok(memberLevel);
    }

    /**
     * 保存会员等级
     */
    @PostMapping
    public Result<Void> save(@RequestBody MemberLevel memberLevel) {
        memberLevelService.save(memberLevel);
        return Result.ok();
    }

    /**
     * 更新会员等级
     */
    @PutMapping
    public Result<Void> update(@RequestBody MemberLevel memberLevel) {
        memberLevelService.updateById(memberLevel);
        return Result.ok();
    }

    /**
     * 删除会员等级
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        memberLevelService.removeById(id);
        return Result.ok();
    }
}
