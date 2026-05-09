package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.Member;
import com.hj.mall.member.service.MemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员控制器
 */
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 分页查询会员列表
     */
    @GetMapping("/list")
    public Result<IPage<Member>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String key) {
        IPage<Member> result = memberService.listPage(new Page<>(page, size), key);
        return Result.ok(result);
    }

    /**
     * 根据ID查询会员
     */
    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        return Result.ok(member);
    }

    /**
     * 保存会员
     */
    @PostMapping
    public Result<Void> save(@RequestBody Member member) {
        memberService.save(member);
        return Result.ok();
    }

    /**
     * 更新会员信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody Member member) {
        memberService.updateById(member);
        return Result.ok();
    }

    /**
     * 批量删除会员
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        memberService.removeBatch(ids);
        return Result.ok();
    }

    /**
     * 根据手机号查询会员
     */
    @GetMapping("/phone/{phone}")
    public Result<Member> getByPhone(@PathVariable String phone) {
        Member member = memberService.getByPhone(phone);
        return Result.ok(member);
    }
}
