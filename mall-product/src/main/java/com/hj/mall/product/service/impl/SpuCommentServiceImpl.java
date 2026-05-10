package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.product.entity.CommentReply;
import com.hj.mall.product.entity.SpuComment;
import com.hj.mall.product.mapper.CommentReplyMapper;
import com.hj.mall.product.mapper.SpuCommentMapper;
import com.hj.mall.product.service.SpuCommentService;
import com.hj.mall.product.vo.CommentReplyVO;
import com.hj.mall.product.vo.SpuCommentSubmitVO;
import com.hj.mall.product.vo.SpuCommentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpuCommentServiceImpl implements SpuCommentService {

    private final SpuCommentMapper spuCommentMapper;
    private final CommentReplyMapper commentReplyMapper;

    @Override
    public IPage<SpuComment> listPage(Page<SpuComment> page) {
        return spuCommentMapper.selectPage(page,
                new LambdaQueryWrapper<SpuComment>().orderByDesc(SpuComment::getCreateTime));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitComment(Long memberId, String memberName, String memberAvatar, SpuCommentSubmitVO vo) {
        if (vo.getStar() == null || vo.getStar() < 1 || vo.getStar() > 5) {
            throw new BizException("评分必须在1-5之间");
        }
        if (vo.getContent() == null || vo.getContent().trim().isEmpty()) {
            throw new BizException("评论内容不能为空");
        }

        // 同一用户同一SKU只能评论一次
        long existCount = spuCommentMapper.selectCount(
                new LambdaQueryWrapper<SpuComment>()
                        .eq(SpuComment::getMemberId, memberId)
                        .eq(SpuComment::getSkuId, vo.getSkuId()));
        if (existCount > 0) {
            throw new BizException("您已评论过该商品");
        }

        SpuComment comment = new SpuComment();
        comment.setSkuId(vo.getSkuId());
        comment.setSpuId(vo.getSpuId());
        comment.setSpuName(vo.getSpuName());
        comment.setMemberId(memberId);
        comment.setMemberNickname(memberName);
        comment.setMemberAvatar(memberAvatar);
        comment.setStar(vo.getStar());
        comment.setContent(vo.getContent().trim());
        comment.setResources(vo.getResources());
        comment.setSkuAttributes(vo.getSpuAttributes());
        comment.setShowStatus(1);
        comment.setCommentType(0);
        comment.setLikesCount(0);
        comment.setReplyCount(0);

        spuCommentMapper.insert(comment);
        log.info("[商品评论] 提交成功: memberId={}, skuId={}, commentId={}", memberId, vo.getSkuId(), comment.getId());
    }

    @Override
    public IPage<SpuComment> queryBySpuId(Page<SpuComment> page, Long spuId) {
        return spuCommentMapper.selectPage(page,
                new LambdaQueryWrapper<SpuComment>()
                        .eq(SpuComment::getSpuId, spuId)
                        .eq(SpuComment::getShowStatus, 1)
                        .orderByDesc(SpuComment::getCreateTime));
    }

    @Override
    public IPage<SpuComment> queryBySkuId(Page<SpuComment> page, Long skuId) {
        return spuCommentMapper.selectPage(page,
                new LambdaQueryWrapper<SpuComment>()
                        .eq(SpuComment::getSkuId, skuId)
                        .eq(SpuComment::getShowStatus, 1)
                        .orderByDesc(SpuComment::getCreateTime));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeComment(Long commentId) {
        SpuComment comment = spuCommentMapper.selectById(commentId);
        if (comment == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }
        comment.setLikesCount(comment.getLikesCount() == null ? 1 : comment.getLikesCount() + 1);
        spuCommentMapper.updateById(comment);
    }

    @Override
    public SpuCommentVO getCommentDetail(Long commentId) {
        SpuComment entity = spuCommentMapper.selectById(commentId);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        SpuCommentVO vo = new SpuCommentVO();
        BeanUtils.copyProperties(entity, vo);

        List<CommentReply> replies = commentReplyMapper.selectList(
                new LambdaQueryWrapper<CommentReply>()
                        .eq(CommentReply::getCommentId, commentId)
                        .orderByAsc(CommentReply::getCreateTime));

        vo.setReplies(replies.stream().map(r -> {
            CommentReplyVO rv = new CommentReplyVO();
            BeanUtils.copyProperties(r, rv);
            return rv;
        }).toList());

        return vo;
    }

    @Override
    public void removeBatch(List<Long> ids) {
        spuCommentMapper.deleteBatchIds(ids);
    }
}
