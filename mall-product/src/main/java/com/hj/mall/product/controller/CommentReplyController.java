package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.CommentReply;
import com.hj.mall.product.mapper.CommentReplyMapper;
import com.hj.mall.product.service.CommentReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment-reply")
@RequiredArgsConstructor
public class CommentReplyController {

    private final CommentReplyService commentReplyService;
    private final CommentReplyMapper commentReplyMapper;

    @GetMapping("/list")
    public Result<IPage<CommentReply>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(commentReplyService.listPage(new Page<>(page, size)));
    }

    @GetMapping("/info/{id}")
    public Result<CommentReply> info(@PathVariable Long id) {
        return Result.ok(commentReplyMapper.selectById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody CommentReply reply) {
        commentReplyService.save(reply);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody CommentReply reply) {
        commentReplyService.updateById(reply);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentReplyService.removeById(id);
        return Result.ok();
    }
}
