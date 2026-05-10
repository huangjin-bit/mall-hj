package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.MemberCollectSubject;
import com.hj.mall.member.service.MemberCollectSubjectService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员收藏专题控制器
 */
@RestController
@RequestMapping("/member-collect-subject")
@RequiredArgsConstructor
public class MemberCollectSubjectController {

    private final MemberCollectSubjectService collectSubjectService;

    /**
     * 分页查询会员专题收藏列表
     */
    @GetMapping("/list")
    public Result<IPage<MemberCollectSubject>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long memberId) {
        IPage<MemberCollectSubject> result = collectSubjectService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }

    /**
     * 根据ID查询收藏信息
     */
    @GetMapping("/{id}")
    public Result<MemberCollectSubject> getById(@PathVariable Long id) {
        MemberCollectSubject collectSubject = collectSubjectService.getById(id);
        return Result.ok(collectSubject);
    }

    /**
     * 添加专题收藏
     */
    @PostMapping
    public Result<Void> save(@RequestBody MemberCollectSubject collectSubject) {
        collectSubjectService.save(collectSubject);
        return Result.ok();
    }

    /**
     * 取消专题收藏
     */
    @DeleteMapping
    public Result<Void> remove(
            @RequestParam Long memberId,
            @RequestParam Long subjectId) {
        collectSubjectService.remove(memberId, subjectId);
        return Result.ok();
    }
}
