package com.hj.mall.member.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.member.entity.MemberReceiveAddress;
import com.hj.mall.member.service.MemberReceiveAddressService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员收货地址控制器
 */
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class MemberReceiveAddressController {

    private final MemberReceiveAddressService addressService;

    /**
     * 查询会员的所有收货地址
     */
    @GetMapping("/list")
    public Result<List<MemberReceiveAddress>> list(@RequestParam Long memberId) {
        List<MemberReceiveAddress> addresses = addressService.listByMemberId(memberId);
        return Result.ok(addresses);
    }

    /**
     * 根据ID查询收货地址
     */
    @GetMapping("/{id}")
    public Result<MemberReceiveAddress> getById(@PathVariable Long id) {
        MemberReceiveAddress address = addressService.getById(id);
        return Result.ok(address);
    }

    /**
     * 保存收货地址
     */
    @PostMapping
    public Result<Void> save(@RequestBody MemberReceiveAddress address) {
        addressService.save(address);
        return Result.ok();
    }

    /**
     * 更新收货地址
     */
    @PutMapping
    public Result<Void> update(@RequestBody MemberReceiveAddress address) {
        addressService.updateById(address);
        return Result.ok();
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressService.removeById(id);
        return Result.ok();
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id, @RequestParam Long memberId) {
        addressService.setDefault(id, memberId);
        return Result.ok();
    }
}
