package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.member.entity.MemberReceiveAddress;
import com.hj.mall.member.mapper.MemberReceiveAddressMapper;
import com.hj.mall.member.service.MemberReceiveAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员收货地址服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberReceiveAddressServiceImpl implements MemberReceiveAddressService {

    private final MemberReceiveAddressMapper addressMapper;

    @Override
    public List<MemberReceiveAddress> listByMemberId(Long memberId) {
        log.info("[MemberReceiveAddressService] 查询会员收货地址列表, memberId={}", memberId);

        if (memberId == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        LambdaQueryWrapper<MemberReceiveAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberReceiveAddress::getMemberId, memberId)
               .orderByDesc(MemberReceiveAddress::getDefaultStatus)
               .orderByDesc(MemberReceiveAddress::getCreateTime);

        return addressMapper.selectList(wrapper);
    }

    @Override
    public MemberReceiveAddress getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        MemberReceiveAddress address = addressMapper.selectById(id);
        if (address == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return address;
    }

    @Override
    public void save(MemberReceiveAddress address) {
        log.info("[MemberReceiveAddressService] 保存收货地址, memberId={}", address.getMemberId());

        // 如果设置为默认地址，先将该会员的其他地址设为非默认
        if (address.getDefaultStatus() != null && address.getDefaultStatus() == 1) {
            LambdaUpdateWrapper<MemberReceiveAddress> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(MemberReceiveAddress::getMemberId, address.getMemberId())
                   .set(MemberReceiveAddress::getDefaultStatus, 0);
            addressMapper.update(null, wrapper);
        }

        addressMapper.insert(address);
        log.info("[MemberReceiveAddressService] 收货地址保存成功, id={}", address.getId());
    }

    @Override
    public void updateById(MemberReceiveAddress address) {
        log.info("[MemberReceiveAddressService] 更新收货地址信息, id={}", address.getId());

        MemberReceiveAddress existAddress = addressMapper.selectById(address.getId());
        if (existAddress == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        // 如果设置为默认地址，先将该会员的其他地址设为非默认
        if (address.getDefaultStatus() != null && address.getDefaultStatus() == 1) {
            LambdaUpdateWrapper<MemberReceiveAddress> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(MemberReceiveAddress::getMemberId, address.getMemberId())
                   .ne(MemberReceiveAddress::getId, address.getId())
                   .set(MemberReceiveAddress::getDefaultStatus, 0);
            addressMapper.update(null, wrapper);
        }

        addressMapper.updateById(address);
        log.info("[MemberReceiveAddressService] 收货地址信息更新成功, id={}", address.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        log.info("[MemberReceiveAddressService] 删除收货地址, id={}", id);

        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        addressMapper.deleteById(id);
        log.info("[MemberReceiveAddressService] 收货地址删除成功, id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id, Long memberId) {
        log.info("[MemberReceiveAddressService] 设置默认地址, id={}, memberId={}", id, memberId);

        if (id == null || memberId == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        // 先将该会员的所有地址设为非默认
        LambdaUpdateWrapper<MemberReceiveAddress> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MemberReceiveAddress::getMemberId, memberId)
               .set(MemberReceiveAddress::getDefaultStatus, 0);
        addressMapper.update(null, wrapper);

        // 再将目标地址设为默认
        MemberReceiveAddress address = new MemberReceiveAddress();
        address.setId(id);
        address.setDefaultStatus(1);
        addressMapper.updateById(address);

        log.info("[MemberReceiveAddressService] 默认地址设置成功, id={}", id);
    }
}
