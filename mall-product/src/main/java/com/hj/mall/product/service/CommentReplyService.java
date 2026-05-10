package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.CommentReply;

public interface CommentReplyService {

    IPage<CommentReply> listPage(Page<CommentReply> page);

    void save(CommentReply reply);

    void updateById(CommentReply reply);

    void removeById(Long id);
}
