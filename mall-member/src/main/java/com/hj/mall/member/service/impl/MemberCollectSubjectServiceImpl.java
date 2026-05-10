package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberCollectSubject;
import com.hj.mall.member.mapper.MemberCollectSubjectMapper;
import com.hj.mall.member.service.MemberCollectSubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员收藏专题服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCollectSubjectServiceImpl implements MemberCollectSubjectService {

    private final MemberCollectSubjectMapper collectSubjectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MemberCollectSubject collectSubject) {
        log.info("[MemberCollectSubjectService] 保存专题收藏, memberId={}, subjectId={}",
                collectSubject.getMemberId(), collectSubject.getSubjectId());

        // 检查是否已收藏
        LambdaQueryWrapper<MemberCollectSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberCollectSubject::getMemberId, collectSubject.getMemberId())
               .eq(MemberCollectSubject::getSubjectId, collectSubject.getSubjectId());
        MemberCollectSubject exist = collectSubjectMapper.selectOne(wrapper);

        if (exist != null) {
            log.info("[MemberCollectSubjectService] 专题已收藏，无需重复收藏");
            return;
        }

        collectSubjectMapper.insert(collectSubject);
        log.info("[MemberCollectSubjectService] 专题收藏保存成功, id={}", collectSubject.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long memberId, Long subjectId) {
        log.info("[MemberCollectSubjectService] 取消专题收藏, memberId={}, subjectId={}", memberId, subjectId);

        if (memberId == null || subjectId == null) {
            throw new IllegalArgumentException("会员ID和专题ID不能为空");
        }

        LambdaQueryWrapper<MemberCollectSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberCollectSubject::getMemberId, memberId)
               .eq(MemberCollectSubject::getSubjectId, subjectId);

        int count = collectSubjectMapper.delete(wrapper);
        log.info("[MemberCollectSubjectService] 取消收藏成功, count={}", count);
    }

    @Override
    public MemberCollectSubject getById(Long id) {
        log.info("[MemberCollectSubjectService] 查询专题收藏信息, id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("收藏信息ID不能为空");
        }

        return collectSubjectMapper.selectById(id);
    }

    @Override
    public IPage<MemberCollectSubject> listPage(Page<MemberCollectSubject> page, Long memberId) {
        log.info("[MemberCollectSubjectService] 分页查询会员专题收藏列表, page={}, memberId={}",
                page.getCurrent(), memberId);

        LambdaQueryWrapper<MemberCollectSubject> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(MemberCollectSubject::getMemberId, memberId);
        }
        wrapper.orderByDesc(MemberCollectSubject::getCreateTime);

        return collectSubjectMapper.selectPage(page, wrapper);
    }
}
