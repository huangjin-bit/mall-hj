package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.Coupon;
import com.hj.mall.seckill.entity.CouponHistory;
import com.hj.mall.seckill.mapper.CouponMapper;
import com.hj.mall.seckill.service.CouponService;
import com.hj.mall.seckill.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final CouponHistoryService couponHistoryService;

    @Override
    public IPage<Coupon> listPage(Page<Coupon> page, String name, Integer status) {
        log.info("[CouponService] 分页查询优惠券, page={}, name={}, status={}", page.getCurrent(), name, status);

        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Coupon::getName, name);
        }
        if (status != null) {
            wrapper.eq(Coupon::getStatus, status);
        }
        wrapper.orderByDesc(Coupon::getCreateTime);

        return couponMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Coupon> listAvailableCoupons() {
        log.info("[CouponService] 查询可领取的优惠券列表");

        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getStatus, 1)
               .le(Coupon::getStartTime, LocalDateTime.now())
               .ge(Coupon::getEndTime, LocalDateTime.now())
               .apply("total_count > received_count")
               .orderByDesc(Coupon::getCreateTime);

        return couponMapper.selectList(wrapper);
    }

    @Override
    public Coupon getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return coupon;
    }

    @Override
    public void save(Coupon coupon) {
        log.info("[CouponService] 保存优惠券, name={}", coupon.getName());

        // 参数校验
        if (coupon.getCouponType() == null) {
            throw new BizException(400, "优惠券类型不能为空");
        }
        if (StringUtils.isBlank(coupon.getName())) {
            throw new BizException(400, "优惠券名称不能为空");
        }
        if (coupon.getTotalCount() == null || coupon.getTotalCount() <= 0) {
            throw new BizException(400, "发行数量必须大于0");
        }
        if (coupon.getPerLimit() == null || coupon.getPerLimit() <= 0) {
            throw new BizException(400, "每人限领数量必须大于0");
        }
        if (coupon.getStartTime() == null || coupon.getEndTime() == null) {
            throw new BizException(400, "开始时间和结束时间不能为空");
        }
        if (coupon.getStartTime().isAfter(coupon.getEndTime())) {
            throw new BizException(400, "开始时间不能晚于结束时间");
        }

        // 根据类型校验必填字段
        if (coupon.getCouponType() == 0 || coupon.getCouponType() == 2) {
            // 满减券、立减券
            if (coupon.getAmount() == null || coupon.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new BizException(400, "优惠金额必须大于0");
            }
            if (coupon.getCouponType() == 0 && coupon.getMinPoint() == null) {
                throw new BizException(400, "满减券最低消费金额不能为空");
            }
        } else if (coupon.getCouponType() == 1) {
            // 折扣券
            if (coupon.getDiscount() == null || coupon.getDiscount().compareTo(java.math.BigDecimal.ZERO) <= 0
                || coupon.getDiscount().compareTo(new java.math.BigDecimal("100")) >= 0) {
                throw new BizException(400, "折扣必须大于0且小于100");
            }
        }

        coupon.setReceivedCount(0);
        coupon.setUsedCount(0);

        couponMapper.insert(coupon);
        log.info("[CouponService] 优惠券保存成功, id={}", coupon.getId());
    }

    @Override
    public void updateById(Coupon coupon) {
        log.info("[CouponService] 更新优惠券, id={}", coupon.getId());

        Coupon existCoupon = getById(coupon.getId());
        if (existCoupon == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        couponMapper.updateById(coupon);
        log.info("[CouponService] 优惠券更新成功, id={}", coupon.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[CouponService] 批量删除优惠券, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        couponMapper.deleteBatchIds(ids);
        log.info("[CouponService] 批量删除成功, count={}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimCoupon(Long couponId, Long memberId) {
        log.info("[CouponService] 领取优惠券, couponId={}, memberId={}", couponId, memberId);

        Coupon coupon = getById(couponId);

        // 校验优惠券状态
        if (coupon.getStatus() != 1) {
            throw new BizException(400, "优惠券不可领取");
        }

        // 校验时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new BizException(400, "不在领取时间内");
        }

        // 校验库存
        if (coupon.getReceivedCount() >= coupon.getTotalCount()) {
            throw new BizException(400, "优惠券已领完");
        }

        // 校验会员已领取数量
        long memberClaimedCount = couponHistoryService.countByMemberIdAndCouponId(memberId, couponId);
        if (memberClaimedCount >= coupon.getPerLimit()) {
            throw new BizException(400, "超过每人限领数量");
        }

        // 保存领取记录
        CouponHistory history = new CouponHistory();
        history.setCouponId(couponId);
        history.setMemberId(memberId);
        history.setCouponType(coupon.getCouponType());
        history.setAmount(coupon.getAmount());
        history.setStatus(0);
        couponHistoryService.save(history);

        // 更新优惠券已领取数量
        coupon.setReceivedCount(coupon.getReceivedCount() + 1);
        couponMapper.updateById(coupon);

        log.info("[CouponService] 优惠券领取成功, couponId={}, memberId={}", couponId, memberId);
    }
}
