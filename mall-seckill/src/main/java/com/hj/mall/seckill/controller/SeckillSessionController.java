package com.hj.mall.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.seckill.entity.SeckillSession;
import com.hj.mall.seckill.service.SeckillSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 秒杀场次控制器
 */
@RestController
@RequestMapping("/seckill-session")
@RequiredArgsConstructor
public class SeckillSessionController {

    private final SeckillSessionService seckillSessionService;

    /**
     * 分页查询秒杀场次
     */
    @GetMapping("/list")
    public Result<IPage<SeckillSession>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        IPage<SeckillSession> result = seckillSessionService.listPage(new Page<>(page, size), name);
        return Result.ok(result);
    }

    /**
     * 查询当前启用的秒杀场次列表
     */
    @GetMapping("/active")
    public Result<List<SeckillSession>> listActiveSessions() {
        List<SeckillSession> list = seckillSessionService.listActiveSessions();
        return Result.ok(list);
    }

    /**
     * 根据ID查询场次
     */
    @GetMapping("/{id}")
    public Result<SeckillSession> getById(@PathVariable Long id) {
        SeckillSession session = seckillSessionService.getById(id);
        return Result.ok(session);
    }

    /**
     * 保存场次
     */
    @PostMapping
    public Result<Void> save(@RequestBody SeckillSession session) {
        seckillSessionService.save(session);
        return Result.ok();
    }

    /**
     * 更新场次
     */
    @PutMapping
    public Result<Void> update(@RequestBody SeckillSession session) {
        seckillSessionService.updateById(session);
        return Result.ok();
    }

    /**
     * 批量删除场次
     */
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        seckillSessionService.removeBatch(ids);
        return Result.ok();
    }
}
