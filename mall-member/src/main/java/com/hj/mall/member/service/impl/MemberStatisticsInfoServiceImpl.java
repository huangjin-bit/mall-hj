package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberStatisticsInfo;
import com.hj.mall.member.mapper.MemberStatisticsInfoMapper;
import com.hj.mall.member.service.MemberStatisticsInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员统计信息服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberStatisticsInfoServiceImpl implements MemberStatisticsInfoService {

    private final MemberStatisticsInfoMapper statisticsInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MemberStatisticsInfo statisticsInfo) {
        log.info("[MemberStatisticsInfoService] 保存会员统计信息, memberId={}", statisticsInfo.getMemberId());

        if (statisticsInfo.getMemberId() == null) {
            throw new IllegalArgumentException("会员ID不能为空");
        }

        // 检查是否已存在统计信息
        LambdaQueryWrapper<MemberStatisticsInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberStatisticsInfo::getMemberId, statisticsInfo.getMemberId());
        MemberStatisticsInfo exist = statisticsInfoMapper.selectOne(wrapper);

        if (exist != null) {
            log.info("[MemberStatisticsInfoService] 会员统计信息已存在，请使用更新操作");
            throw new IllegalArgumentException("该会员的统计信息已存在，请使用更新操作");
        }

        statisticsInfoMapper.insert(statisticsInfo);
        log.info("[MemberStatisticsInfoService] 会员统计信息保存成功, id={}", statisticsInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MemberStatisticsInfo statisticsInfo) {
        log.info("[MemberStatisticsInfoService] 更新会员统计信息, id={}", statisticsInfo.getId());

        if (statisticsInfo.getId() == null) {
            throw new IllegalArgumentException("统计信息ID不能为空");
        }

        int count = statisticsInfoMapper.updateById(statisticsInfo);
        if (count == 0) {
            log.warn("[MemberStatisticsInfoService] 更新失败，统计信息不存在, id={}", statisticsInfo.getId());
            throw new IllegalArgumentException("统计信息不存在");
        }

        log.info("[MemberStatisticsInfoService] 会员统计信息更新成功, id={}", statisticsInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("[MemberStatisticsInfoService] 删除会员统计信息, id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("统计信息ID不能为空");
        }

        int count = statisticsInfoMapper.deleteById(id);
        log.info("[MemberStatisticsInfoService] 删除成功, count={}", count);
    }

    @Override
    public MemberStatisticsInfo getById(Long id) {
        log.info("[MemberStatisticsInfoService] 查询会员统计信息, id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("统计信息ID不能为空");
        }

        return statisticsInfoMapper.selectById(id);
    }

    @Override
    public IPage<MemberStatisticsInfo> listPage(Page<MemberStatisticsInfo> page, Long memberId) {
        log.info("[MemberStatisticsInfoService] 分页查询会员统计信息列表, page={}, memberId={}", page.getCurrent(), memberId);

        LambdaQueryWrapper<MemberStatisticsInfo> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(MemberStatisticsInfo::getMemberId, memberId);
        }
        wrapper.orderByDesc(MemberStatisticsInfo::getCreateTime);

        return statisticsInfoMapper.selectPage(page, wrapper);
    }
}
