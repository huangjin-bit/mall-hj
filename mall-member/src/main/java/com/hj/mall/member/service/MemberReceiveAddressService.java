package com.hj.mall.member.service;

import com.hj.mall.member.entity.MemberReceiveAddress;

import java.util.List;

/**
 * 会员收货地址服务接口
 */
public interface MemberReceiveAddressService {

    /**
     * 查询会员的所有收货地址
     * @param memberId 会员ID
     * @return 收货地址列表
     */
    List<MemberReceiveAddress> listByMemberId(Long memberId);

    /**
     * 根据ID查询收货地址
     * @param id 地址ID
     * @return 收货地址信息
     */
    MemberReceiveAddress getById(Long id);

    /**
     * 保存收货地址
     * @param address 收货地址信息
     */
    void save(MemberReceiveAddress address);

    /**
     * 更新收货地址信息
     * @param address 收货地址信息
     */
    void updateById(MemberReceiveAddress address);

    /**
     * 删除收货地址
     * @param id 地址ID
     */
    void removeById(Long id);

    /**
     * 设置默认地址
     * @param id 地址ID
     * @param memberId 会员ID
     */
    void setDefault(Long id, Long memberId);
}
