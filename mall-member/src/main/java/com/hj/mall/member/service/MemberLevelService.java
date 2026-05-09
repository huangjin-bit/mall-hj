package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberLevel;

import java.util.List;

/**
 * 会员等级服务接口
 */
public interface MemberLevelService {

    /**
     * 分页查询会员等级列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<MemberLevel> listPage(Page<MemberLevel> page);

    /**
     * 根据ID查询会员等级
     * @param id 等级ID
     * @return 会员等级信息
     */
    MemberLevel getById(Long id);

    /**
     * 保存会员等级
     * @param memberLevel 会员等级信息
     */
    void save(MemberLevel memberLevel);

    /**
     * 更新会员等级信息
     * @param memberLevel 会员等级信息
     */
    void updateById(MemberLevel memberLevel);

    /**
     * 批量删除会员等级
     * @param ids 等级ID列表
     */
    void removeBatch(List<Long> ids);
}
