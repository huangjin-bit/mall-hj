package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.CommentReply;
import com.hj.mall.product.mapper.CommentReplyMapper;
import com.hj.mall.product.service.CommentReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentReplyServiceImpl implements CommentReplyService {

    private final CommentReplyMapper commentReplyMapper;

    @Override
    public IPage<CommentReply> listPage(Page<CommentReply> page) {
        return commentReplyMapper.selectPage(page,
                new LambdaQueryWrapper<CommentReply>().orderByDesc(CommentReply::getCreateTime));
    }

    @Override
    public void save(CommentReply reply) {
        commentReplyMapper.insert(reply);
    }

    @Override
    public void updateById(CommentReply reply) {
        commentReplyMapper.updateById(reply);
    }

    @Override
    public void removeById(Long id) {
        commentReplyMapper.deleteById(id);
    }
}
