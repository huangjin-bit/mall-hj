package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.MemberCollectSpu;
import com.hj.mall.member.service.MemberCollectSpuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会员商品收藏控制器
 */
@RestController
@RequestMapping("/member-collect")
@RequiredArgsConstructor
public class MemberCollectSpuController {

    private final MemberCollectSpuService collectService;

    /**
     * 分页查询会员收藏列表
     */
    @GetMapping("/list")
    public Result<IPage<MemberCollectSpu>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Long memberId) {
        IPage<MemberCollectSpu> result = collectService.listPage(new Page<>(page, size), memberId);
        return Result.ok(result);
    }

    /**
     * 添加收藏
     */
    @PostMapping
    public Result<Void> save(@RequestBody MemberCollectSpu collect) {
        collectService.save(collect);
        return Result.ok();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping
    public Result<Void> remove(
            @RequestParam Long memberId,
            @RequestParam Long spuId) {
        collectService.remove(memberId, spuId);
        return Result.ok();
    }
}
