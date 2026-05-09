package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.CouponHistory;
import com.hj.mall.seckill.mapper.CouponHistoryMapper;
import com.hj.mall.seckill.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券领取历史服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {

    private final CouponHistoryMapper couponHistoryMapper;

    @Override
    public IPage<CouponHistory> listPage(Page<CouponHistory> page, Long memberId, Integer status) {
        log.info("[CouponHistoryService] 分页查询领取历史, page={}, memberId={}, status={}", page.getCurrent(), memberId, status);

        LambdaQueryWrapper<CouponHistory> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(CouponHistory::getMemberId, memberId);
        }
        if (status != null) {
            wrapper.eq(CouponHistory::getStatus, status);
        }
        wrapper.orderByDesc(CouponHistory::getCreateTime);

        return couponHistoryMapper.selectPage(page, wrapper);
    }

    @Override
    public List<CouponHistory> listByMemberId(Long memberId) {
        log.info("[CouponHistoryService] 根据会员ID查询领取历史, memberId={}", memberId);

        LambdaQueryWrapper<CouponHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponHistory::getMemberId, memberId)
               .orderByDesc(CouponHistory::getCreateTime);

        return couponHistoryMapper.selectList(wrapper);
    }

    @Override
    public CouponHistory getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        CouponHistory history = couponHistoryMapper.selectById(id);
        if (history == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return history;
    }

    @Override
    public void save(CouponHistory history) {
        log.info("[CouponHistoryService] 保存领取历史, couponId={}, memberId={}", history.getCouponId(), history.getMemberId());
        couponHistoryMapper.insert(history);
        log.info("[CouponHistoryService] 领取历史保存成功, id={}", history.getId());
    }

    @Override
    public long countByMemberIdAndCouponId(Long memberId, Long couponId) {
        LambdaQueryWrapper<CouponHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponHistory::getMemberId, memberId)
               .eq(CouponHistory::getCouponId, couponId);

        return couponHistoryMapper.selectCount(wrapper);
    }
}
