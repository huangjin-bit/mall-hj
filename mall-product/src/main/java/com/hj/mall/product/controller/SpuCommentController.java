package com.hj.mall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.SpuComment;
import com.hj.mall.product.mapper.SpuCommentMapper;
import com.hj.mall.product.service.SpuCommentService;
import com.hj.mall.product.vo.SpuCommentSubmitVO;
import com.hj.mall.product.vo.SpuCommentVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spu-comment")
@RequiredArgsConstructor
public class SpuCommentController {

    private final SpuCommentService spuCommentService;
    private final SpuCommentMapper spuCommentMapper;

    /** 管理后台 - 分页列表 */
    @GetMapping("/list")
    public Result<IPage<SpuComment>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(spuCommentService.listPage(new Page<>(page, size)));
    }

    /** 前台 - 提交评论 */
    @PostMapping("/submit")
    public Result<Void> submit(
            @RequestHeader(value = "X-User-Id", defaultValue = "0") Long memberId,
            @RequestHeader(value = "X-User-Name", defaultValue = "匿名用户") String memberName,
            @RequestHeader(value = "X-User-Icon", defaultValue = "") String memberAvatar,
            @Valid @RequestBody SpuCommentSubmitVO vo) {
        spuCommentService.submitComment(memberId, memberName, memberAvatar, vo);
        return Result.ok();
    }

    /** 前台 - 按SPU查评论（商品详情页） */
    @GetMapping("/spu/{spuId}")
    public Result<IPage<SpuComment>> listBySpu(
            @PathVariable Long spuId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(spuCommentService.queryBySpuId(new Page<>(page, size), spuId));
    }

    /** 前台 - 按SKU查评论 */
    @GetMapping("/sku/{skuId}")
    public Result<IPage<SpuComment>> listBySku(
            @PathVariable Long skuId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(spuCommentService.queryBySkuId(new Page<>(page, size), skuId));
    }

    /** 评论详情（含回复列表） */
    @GetMapping("/detail/{id}")
    public Result<SpuCommentVO> detail(@PathVariable Long id) {
        return Result.ok(spuCommentService.getCommentDetail(id));
    }

    /** 点赞评论 */
    @PostMapping("/like/{id}")
    public Result<Void> like(@PathVariable Long id) {
        spuCommentService.likeComment(id);
        return Result.ok();
    }

    /** 管理后台 - 查看单条 */
    @GetMapping("/info/{id}")
    public Result<SpuComment> info(@PathVariable Long id) {
        return Result.ok(spuCommentMapper.selectById(id));
    }

    /** 管理后台 - 新增 */
    @PostMapping
    public Result<Void> save(@RequestBody SpuComment comment) {
        spuCommentMapper.insert(comment);
        return Result.ok();
    }

    /** 管理后台 - 更新 */
    @PutMapping
    public Result<Void> update(@RequestBody SpuComment comment) {
        spuCommentMapper.updateById(comment);
        return Result.ok();
    }

    /** 管理后台 - 删除 */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody List<Long> ids) {
        spuCommentService.removeBatch(ids);
        return Result.ok();
    }
}
