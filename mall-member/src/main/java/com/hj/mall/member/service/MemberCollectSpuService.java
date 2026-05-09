package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberCollectSpu;

/**
 * 会员商品收藏服务接口
 */
public interface MemberCollectSpuService {

    /**
     * 保存商品收藏
     * @param collect 收藏信息
     */
    void save(MemberCollectSpu collect);

    /**
     * 取消收藏
     * @param memberId 会员ID
     * @param spuId 商品SPU ID
     */
    void remove(Long memberId, Long spuId);

    /**
     * 分页查询会员收藏列表
     * @param page 分页参数
     * @param memberId 会员ID
     * @return 分页结果
     */
    IPage<MemberCollectSpu> listPage(Page<MemberCollectSpu> page, Long memberId);
}
