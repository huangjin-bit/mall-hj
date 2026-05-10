package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.SpuComment;
import com.hj.mall.product.vo.SpuCommentSubmitVO;
import com.hj.mall.product.vo.SpuCommentVO;

import java.util.List;

public interface SpuCommentService {

    IPage<SpuComment> listPage(Page<SpuComment> page);

    void submitComment(Long memberId, String memberName, String memberAvatar, SpuCommentSubmitVO vo);

    IPage<SpuComment> queryBySpuId(Page<SpuComment> page, Long spuId);

    IPage<SpuComment> queryBySkuId(Page<SpuComment> page, Long skuId);

    void likeComment(Long commentId);

    SpuCommentVO getCommentDetail(Long commentId);

    void removeBatch(List<Long> ids);
}
