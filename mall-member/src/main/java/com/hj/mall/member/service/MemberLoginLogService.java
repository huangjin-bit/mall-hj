package com.hj.mall.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.member.entity.MemberLoginLog;

/**
 * 会员登录日志服务接口
 */
public interface MemberLoginLogService {

    /**
     * 保存登录日志
     * @param log 登录日志
     */
    void save(MemberLoginLog log);

    /**
     * 分页查询会员登录日志
     * @param page 分页参数
     * @param memberId 会员ID
     * @return 分页结果
     */
    IPage<MemberLoginLog> listPage(Page<MemberLoginLog> page, Long memberId);
}
