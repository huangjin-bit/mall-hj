package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.HomeAdv;
import com.hj.mall.seckill.mapper.HomeAdvMapper;
import com.hj.mall.seckill.service.HomeAdvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 首页广告服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeAdvServiceImpl implements HomeAdvService {

    private final HomeAdvMapper homeAdvMapper;

    @Override
    public IPage<HomeAdv> listPage(Page<HomeAdv> page, String name, Integer type) {
        log.info("[HomeAdvService] 分页查询广告, page={}, name={}, type={}", page.getCurrent(), name, type);

        LambdaQueryWrapper<HomeAdv> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(HomeAdv::getName, name);
        }
        if (type != null) {
            wrapper.eq(HomeAdv::getType, type);
        }
        wrapper.orderByDesc(HomeAdv::getCreateTime);

        return homeAdvMapper.selectPage(page, wrapper);
    }

    @Override
    public List<HomeAdv> listActiveAds(Integer type) {
        log.info("[HomeAdvService] 查询当前启用的广告, type={}", type);

        LambdaQueryWrapper<HomeAdv> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomeAdv::getStatus, 1)
               .le(HomeAdv::getStartTime, LocalDateTime.now())
               .ge(HomeAdv::getEndTime, LocalDateTime.now());
        if (type != null) {
            wrapper.eq(HomeAdv::getType, type);
        }
        wrapper.orderByAsc(HomeAdv::getSort);

        return homeAdvMapper.selectList(wrapper);
    }

    @Override
    public HomeAdv getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        HomeAdv adv = homeAdvMapper.selectById(id);
        if (adv == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return adv;
    }

    @Override
    public void save(HomeAdv adv) {
        log.info("[HomeAdvService] 保存广告, name={}", adv.getName());

        // 参数校验
        if (StringUtils.isBlank(adv.getName())) {
            throw new BizException(400, "广告名称不能为空");
        }
        if (StringUtils.isBlank(adv.getPic())) {
            throw new BizException(400, "广告图片不能为空");
        }
        if (adv.getStartTime() == null || adv.getEndTime() == null) {
            throw new BizException(400, "开始时间和结束时间不能为空");
        }
        if (adv.getStartTime().isAfter(adv.getEndTime())) {
            throw new BizException(400, "开始时间不能晚于结束时间");
        }

        homeAdvMapper.insert(adv);
        log.info("[HomeAdvService] 广告保存成功, id={}", adv.getId());
    }

    @Override
    public void updateById(HomeAdv adv) {
        log.info("[HomeAdvService] 更新广告, id={}", adv.getId());

        HomeAdv existAdv = getById(adv.getId());
        if (existAdv == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        homeAdvMapper.updateById(adv);
        log.info("[HomeAdvService] 广告更新成功, id={}", adv.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[HomeAdvService] 批量删除广告, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        homeAdvMapper.deleteBatchIds(ids);
        log.info("[HomeAdvService] 批量删除成功, count={}", ids.size());
    }
}
