package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberCollectSpu;
import com.hj.mall.member.mapper.MemberCollectSpuMapper;
import com.hj.mall.member.service.MemberCollectSpuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员商品收藏服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCollectSpuServiceImpl implements MemberCollectSpuService {

    private final MemberCollectSpuMapper collectSpuMapper;

    @Override
    public void save(MemberCollectSpu collect) {
        log.info("[MemberCollectSpuService] 保存商品收藏, memberId={}, spuId={}", collect.getMemberId(), collect.getSpuId());

        // 检查是否已收藏
        LambdaQueryWrapper<MemberCollectSpu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberCollectSpu::getMemberId, collect.getMemberId())
               .eq(MemberCollectSpu::getSpuId, collect.getSpuId());
        MemberCollectSpu exist = collectSpuMapper.selectOne(wrapper);

        if (exist != null) {
            log.info("[MemberCollectSpuService] 商品已收藏，无需重复收藏");
            return;
        }

        collectSpuMapper.insert(collect);
        log.info("[MemberCollectSpuService] 商品收藏保存成功, id={}", collect.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long memberId, Long spuId) {
        log.info("[MemberCollectSpuService] 取消商品收藏, memberId={}, spuId={}", memberId, spuId);

        if (memberId == null || spuId == null) {
            throw new IllegalArgumentException("会员ID和商品ID不能为空");
        }

        LambdaQueryWrapper<MemberCollectSpu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberCollectSpu::getMemberId, memberId)
               .eq(MemberCollectSpu::getSpuId, spuId);

        int count = collectSpuMapper.delete(wrapper);
        log.info("[MemberCollectSpuService] 取消收藏成功, count={}", count);
    }

    @Override
    public IPage<MemberCollectSpu> listPage(Page<MemberCollectSpu> page, Long memberId) {
        log.info("[MemberCollectSpuService] 分页查询会员收藏列表, page={}, memberId={}", page.getCurrent(), memberId);

        LambdaQueryWrapper<MemberCollectSpu> wrapper = new LambdaQueryWrapper<>();
        if (memberId != null) {
            wrapper.eq(MemberCollectSpu::getMemberId, memberId);
        }
        wrapper.orderByDesc(MemberCollectSpu::getCreateTime);

        return collectSpuMapper.selectPage(page, wrapper);
    }
}
