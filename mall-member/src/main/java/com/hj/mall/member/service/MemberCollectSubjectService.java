package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberCollectSubject;

/**
 * 会员收藏专题服务接口
 */
public interface MemberCollectSubjectService {

    /**
     * 保存专题收藏
     * @param collectSubject 收藏信息
     */
    void save(MemberCollectSubject collectSubject);

    /**
     * 取消专题收藏
     * @param memberId 会员ID
     * @param subjectId 专题ID
     */
    void remove(Long memberId, Long subjectId);

    /**
     * 根据ID查询收藏信息
     * @param id 收藏信息ID
     * @return 收藏信息
     */
    MemberCollectSubject getById(Long id);

    /**
     * 分页查询会员专题收藏列表
     * @param page 分页参数
     * @param memberId 会员ID（可选）
     * @return 分页结果
     */
    IPage<MemberCollectSubject> listPage(Page<MemberCollectSubject> page, Long memberId);
}
