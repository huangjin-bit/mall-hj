package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.member.entity.MemberLevel;
import com.hj.mall.member.mapper.MemberLevelMapper;
import com.hj.mall.member.service.MemberLevelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会员等级服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberLevelServiceImpl implements MemberLevelService {

    private final MemberLevelMapper memberLevelMapper;

    @Override
    public IPage<MemberLevel> listPage(Page<MemberLevel> page) {
        log.info("[MemberLevelService] 分页查询会员等级列表, page={}", page.getCurrent());

        LambdaQueryWrapper<MemberLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(MemberLevel::getGrowthPoint);

        return memberLevelMapper.selectPage(page, wrapper);
    }

    @Override
    public MemberLevel getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        MemberLevel memberLevel = memberLevelMapper.selectById(id);
        if (memberLevel == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return memberLevel;
    }

    @Override
    public void save(MemberLevel memberLevel) {
        log.info("[MemberLevelService] 保存会员等级, name={}", memberLevel.getName());

        memberLevelMapper.insert(memberLevel);
        log.info("[MemberLevelService] 会员等级保存成功, id={}", memberLevel.getId());
    }

    @Override
    public void updateById(MemberLevel memberLevel) {
        log.info("[MemberLevelService] 更新会员等级信息, id={}", memberLevel.getId());

        MemberLevel existLevel = memberLevelMapper.selectById(memberLevel.getId());
        if (existLevel == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        memberLevelMapper.updateById(memberLevel);
        log.info("[MemberLevelService] 会员等级信息更新成功, id={}", memberLevel.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[MemberLevelService] 批量删除会员等级, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        memberLevelMapper.deleteBatchIds(ids);
        log.info("[MemberLevelService] 批量删除成功, count={}", ids.size());
    }
}
